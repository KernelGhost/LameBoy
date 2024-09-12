import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fazecast.jSerialComm.SerialPort;

public class frmWindow extends JFrame {
	private static final long serialVersionUID = 525784312732977664L;
	
	public GUIPanel GraphicsPanel = new GUIPanel();
	private InputStream input_stream;
	private SerialPort ports[];
	private JButton btnRefresh;
	private JButton btnConnect;
	private JComboBox<String> comboBox = new JComboBox<String>();
	private JPanel contentPane;

	private void ScanPorts() {
		ports = SerialPort.getCommPorts();
		
		comboBox.removeAllItems();
		for (SerialPort portAvail : ports) {
			comboBox.addItem(portAvail.getSystemPortName());
		}
	}
	
	private boolean ConnectPort(int intArduino) {
		input_stream = new InputStream();
		boolean boolArduino = input_stream.ConnectPort(ports[intArduino], 57600);
		
		if(!boolArduino) {
			input_stream = null;
			// Display error message
			JOptionPane.showMessageDialog(null, "Could not connect to " + ports[intArduino].getSystemPortName() + ".", "Visual Output Test Window", JOptionPane.ERROR_MESSAGE, null);
		} else {
			// Start new thread and process the data
			Thread input_stream_thread = new Thread(input_stream);
			input_stream_thread.start();
		}
		
	return boolArduino;
	}
	
	private void GUIHandler() {
		btnConnect.setEnabled(false);
		btnRefresh.setEnabled(false);
		comboBox.setEnabled(false);
	}
	
	private void Close() {
		// Close the input stream thread if it was created
    	if (input_stream != null) {
    		input_stream.Terminate();
    	}
	}
	
	public frmWindow() {
		setTitle("Visual Output Test Window");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 525, 322);
		setLocationRelativeTo(null);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	Close();
		    }
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// COMBOBOXES
		// comboBox
		comboBox.setBounds(64, 6, 258, 27);
		contentPane.add(comboBox);
		
		// LABELS
		// lblArduino
		JLabel lblArduino = new JLabel("Arduino:");
		lblArduino.setBounds(6, 10, 61, 16);
		contentPane.add(lblArduino);
		
		// BUTTONS
		// btnConnect
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ConnectPort(comboBox.getSelectedIndex())) {
					GUIHandler();
				}
			}
		});
		btnConnect.setBounds(320, 5, 100, 29);
		contentPane.add(btnConnect);
		
		// btnRefresh
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScanPorts();
			}
		});
		btnRefresh.setBounds(418, 5, 100, 29);
		contentPane.add(btnRefresh);
		
		// PANELS
		// GraphicsPanel
		GraphicsPanel.setBounds(6, 38, 512, 256);
		GraphicsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(GraphicsPanel);
		
		// Run first scan
		ScanPorts();
	}
}