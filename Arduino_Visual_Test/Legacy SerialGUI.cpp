// LEGACY SERIAL DEBUG CODE
byte byteHeader[4] = {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD};
byte byteDispOut[256];
byte byteCheckSum = 0;
byte byteResult = 0;

for (unsigned int intCtr = 0; intCtr < 256; intCtr++) {
  byteResult = 0;
  for (unsigned int intArr = 0; intArr < 8; intArr++) {
    if (graphics[(intCtr * 8) + intArr]) {
      byteResult |= (1 << intArr);
    }
  }
  byteDispOut[intCtr] = byteResult;
}

for (unsigned int intCtr = 0; intCtr <= 255; intCtr++) {
  byteCheckSum += byteDispOut[intCtr];
}

Serial.write(byteHeader, sizeof(byteHeader));
Serial.write(byteDispOut, sizeof(byteDispOut));
Serial.write(byteCheckSum);
