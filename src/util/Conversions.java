package util;

public class Conversions {

	public static String[] bytes2hex(byte[] bytes) {
		String[] hex = new String[bytes.length];
		int i=0;
		for (byte b : bytes) hex[i++] = Conversions.byte2hex(b);
		return hex;
	}

	public static byte[] ints2bytes(int[] ints) {
		byte[] bytes = new byte[ints.length];
		for (int i=0; i<ints.length; i++)
			bytes[i] = (byte) ints[i];
		return bytes;
	}

	public static int[] bytes2ints(byte[] bytes) {
		int[] ints = new int[bytes.length];
		for (int i=0; i<bytes.length; i++)
			ints[i] = bytes[i];
		return ints;
	}

	public static String byte2hex(byte b) {
		return String.format("%02X", b);	// makes sure single digit are 0 padded
	}

	public static byte halfHex2byte(char halfByte) {
		return Conversions.halfHex2byte(""+halfByte);
	}

	public static byte halfHex2byte(String halfByte) {
		String alphabet = "0123456789ABCDEF";
		if (!alphabet.contains(halfByte))
			throw new IllegalArgumentException("Valid hex alphabet is : "+alphabet);
		switch (halfByte) {
			case "A": return 10;
			case "B": return 11;
			case "C": return 12;
			case "D": return 13;
			case "E": return 14;
			case "F": return 15;
			default: return (byte) Integer.parseInt(halfByte);
		}
	}

	public static byte hex2byte(String hex) {
		if (hex.length() == 1)
			return halfHex2byte(hex);
		else if (hex.length() == 2) 
			return (byte) (halfHex2byte(hex.charAt(0)) * 16 + halfHex2byte(hex.charAt(1)));
		else
			throw new IllegalArgumentException("Hex is bigger than a byte (>2 digits) :"+hex);
	}

	public static String singleHex(int b) {
		if (b < 0 || b > 15) throw new IllegalArgumentException("Hex values are [0;15]");
		else if (b < 10) return ""+b;
		else if (b == 10) return "A";
		else if (b == 11) return "B";
		else if (b == 12) return "C";
		else if (b == 13) return "D";
		else if (b == 14) return "E";
		else if (b == 15) return "F";
		throw new IllegalArgumentException("Invalid byte argument "+b);
	}

	public static byte merge(byte high, byte low) {
		String hex = String.format("%s%s", singleHex(high), singleHex(low));
		return hex2byte(hex);
	}

	public static void main(String[] args) {
		byte[] bytes = {0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF};
		for (int i=0; i<bytes.length; i++)
			for (int j=0; j<bytes.length; j++)
				System.out.println(
					String.format(
						"%s + %s = %s", 
						singleHex(bytes[i]), 
						singleHex(bytes[j]),
						byte2hex(merge(bytes[i], bytes[j]))));
	}
}
