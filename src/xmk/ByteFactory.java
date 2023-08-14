package xmk;

import java.math.BigInteger;

public class ByteFactory {

	public static long parseBytesToDecimal(String byteSpaces) {
		String[] split = byteSpaces.split("[ ]");
		String hexStringByte = "";
		for (int token = 0; token < split.length; token++) {
			hexStringByte = Integer.toHexString(Byte.toUnsignedInt(Byte.valueOf(split[token])));
			split[token] = hexStringByte;
		}
		String hexString = "";
		for(int i = 0; i < split.length; i++) {
			hexString = hexString + split[i];
		}
		long value = new BigInteger(hexString, 16).longValue();
//		System.out.println(value);
		return new BigInteger(hexString, 16).longValue();
	}
	
}
