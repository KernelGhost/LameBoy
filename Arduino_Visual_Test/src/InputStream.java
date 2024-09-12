import java.util.ArrayList;
import java.util.Arrays;
import com.fazecast.jSerialComm.SerialPort;

public class InputStream implements Runnable {
	public SerialPort port = null;
	public byte[] byteTempPacket = null;
	public boolean boolRun;
	
	public void run() {
		// Start the thread
		boolRun = true;
		StartStream();
    }
	
	public void Terminate() {
		// Stop the thread
        boolRun = false;
	}
	
	// For use in preparing serial connection before starting thread
	public boolean ConnectPort(SerialPort Arduino, int intBaudRate) {
		boolean boolConnect = true;
		port = Arduino;
		
		// Try and open the port
		if (port.openPort()) {
			// Configure baud and timeout options
			port.setBaudRate(intBaudRate);
			port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		} else {
			boolConnect = false;
		}
		
		return boolConnect;
	}
	
	// Begin processing data stream
	private void StartStream() {
		byte[] newData;	// Stores data received from the serial connection
		int numRead;	// Stores the length of the data received
		
		while (boolRun) {
			// Is there new data?
			if (port.bytesAvailable() != 0) {
				// Receive the new data and store the length
				newData = new byte[port.bytesAvailable()];
				numRead = port.readBytes(newData, newData.length) - 1;
				
				// Break the continuous stream of data into payloads
				ArrayList<byte[]> byteNewData = SplitStream(newData, numRead);
				
				// Update GUI using payloads
				if (byteNewData.size() != 0) {
					UpdateGUI(byteNewData);
				}
				
				// Sleep (JPanel refuses to repaint (update) without this :()
				try {Thread.sleep(50);} catch (InterruptedException e) {}
			}
		}
		
		port.closePort();	// Close the connection
		return;				// Terminate the thread
	}
	
	private void UpdateGUI(ArrayList<byte[]> byteGUIData) {
		for (int intCtr = 0; intCtr < byteGUIData.size(); intCtr++) {
			Main.frmMainWindow.GraphicsPanel.UpdateGraphics(byteGUIData.get(intCtr));
		}
	}
	
	// Takes in the received data stream and breaks it into discrete packets
	private ArrayList<byte[]> SplitStream(byte[] byteData, int intLength) {
		ArrayList<byte[]> ArrlistNewPackets = new ArrayList<byte[]>();
		ArrayList<byte[]> ArrlistNewPayloads = new ArrayList<byte[]>();
		
		// Stores the current position within the array of received data
		int intCtr = 0;
		
		// We expect a header of 0xAA, OxBB, 0xCC, 0xDD followed by a payload of size 256 and then a checksum of one byte
		// Go though all the new received bytes that have not been split yet
		while (intCtr <= intLength) {
			// It is safe to proceed to read the next 4 bytes (potentially [0xAA] [0xBB] [0xCC] [0xDD])
			if (intCtr + 3 <= intLength) {
				// Is this the start of a data packet?
				if ((byteData[intCtr] == (byte) 0xAA) & (byteData[intCtr + 1] == (byte) 0xBB) & (byteData[intCtr + 2] == (byte) 0xCC) & (byteData[intCtr + 3] == (byte) 0xDD)) {
					// Create byte array of the correct length to store the packet [Header (4) + Payload (256) = 260]
					byte[] bytePacket = new byte[261];
					
					// Is the packet fully formed? (i.e. Does everything up to and including the checksum exist?)
					if (intCtr + 260 <= intLength) {
						// Store the entire data packet
						int intCheckSum = 0;
						for (int intPCtr = 0; intPCtr <= 260; intPCtr++) {
							bytePacket[intPCtr] = byteData[intCtr + intPCtr];
							if ((intPCtr >= 4) & (intPCtr <= 259)) {
								intCheckSum += (int) (bytePacket[intPCtr] & 0xFF);
							}
						}
						
						if ((int) (intCheckSum & 0xFF) == (int) (bytePacket[260] & 0xFF)) {
							// Successfully grabbed packet.
							ArrlistNewPackets.add(bytePacket);
						}
						
						// Set counter to start position of next packet (if not available yet, loop will exit).
						intCtr += 260;
					} else {
						// The packet is not fully formed. This means this is the last packet in the stream.
						// The rest of this packet can be expected in the next byte array, so we store it.
						byteTempPacket = new byte[intLength - intCtr + 1];
						for (int intTemp = 0; intTemp < (intLength - intCtr + 1); intTemp++) {
							byteTempPacket[intTemp] = byteData[intCtr + intTemp];
						}
						
						// Adjust the counter to break out of the while loop
						intCtr = intLength + 1;
					}
				} else {
					// Was there a semi-formed packet before this?
					if (byteTempPacket == null) {
						// There was no previous packet. This must be the start of the stream.
						// We wait for the beginning of a valid packet. Increment counter.
						intCtr += 1;
					} else {
						// There was a packet before this. Add it to the start of the new array.
						
						int intTempLen = byteTempPacket.length;
				        byte[] byteTempResult = new byte[intTempLen + intLength];
				        
				        System.arraycopy(byteTempPacket, 0, byteTempResult, 0, intTempLen);
				        System.arraycopy(byteData, 0, byteTempResult, intTempLen, intLength);
						
				        byteData = new byte[intTempLen + intLength];
				        byteData = byteTempResult;
						
				        // Set it back to null since we have incorporated it now
						byteTempPacket = null;
					}
				}
			} else {
				// The packet is not fully formed. This means this is the last packet in the stream.
				// The rest of this packet can be expected in the next byte array, so we store it.
				byteTempPacket = new byte[intLength - intCtr + 1];
				for (int intTemp = 0; intTemp < byteTempPacket.length; intTemp++) {
					byteTempPacket[intTemp] = byteData[intCtr + intTemp];
				}
				
				// Adjust the counter to break out of the while loop
				intCtr += byteTempPacket.length;
			}
		}
		
		// Trim to just the payload
		for (int intMCtr = 0; intMCtr < ArrlistNewPackets.size(); intMCtr++) {
			ArrlistNewPayloads.add(Arrays.copyOfRange(ArrlistNewPackets.get(intMCtr), 4, ArrlistNewPackets.get(intMCtr).length - 1));
		}
		
		// Return all new payloads
		return ArrlistNewPayloads;
	}
}