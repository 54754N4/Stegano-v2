package model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import file.Payload;

public abstract class Metafier {
	public static final int NOT_FOUND = -1;
	public final String sep;
	
	public Metafier(String sep) {
		this.sep = sep;
	}
	
	public int[] metafy(Payload payload) {
		System.out.println(String.format("Pre-Infusion SHA256=%s", payload.hexChecksum()));
		String header = buildHeader(payload);
//		System.out.println(String.format("Header=%s", header));
		byte[] headerBytes = header.getBytes(Charset.forName("UTF-8"));
		int[] total = new int[headerBytes.length+payload.fileBytes.length];
		int c = 0;
		for (byte b : headerBytes) total[c++] = b;
		for (byte b : payload.fileBytes) total[c++] = b;
		return total;
	}
	
	protected String buildHeader(Payload payload) {
		String chain = chain(5);
		StringBuilder sb = new StringBuilder();
		sb.append(chain);
		sb.append(2);
		sb.append(sep);
		sb.append(payload.format);
		sb.append(sep);
		sb.append(payload.hexChecksum());
		sb.append(sep);
		sb.append(payload.fileBytes.length);
		sb.append(chain);
		return sb.toString();
	}
	
	// x2 storage space we gain that all bytes are <16 now
	// since we convert each byte to a 2 digit hex string
	public int[] subdivide(int[] bytes) {
		int[] divided = new int[bytes.length*2];
		int i=0;
		for (String hexByte : bytes2hex(bytes)) {
			divided[i++] = hex2byte(""+hexByte.charAt(0));
			divided[i++] = hex2byte(""+hexByte.charAt(1));
		}
		System.out.println(String.format("Encoded %d bytes", divided.length));
		return divided;
	}
	
	public static String singleHex(byte b) {
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
	
	public static int merge(int high, int low) {
		String hex = String.format("%s%s", singleHex((byte) (0x0F & high)), singleHex((byte) (0x0F & low)));
		return hex2byte(hex);
	}
	
	public int[] merge(int[] unpacked) {
		return unpacked;
	}
	
	public static String[] bytes2hex(int[] bytes) {
		String[] hex = new String[bytes.length];
		int i=0;
		for (int b : bytes) hex[i++] = byte2hex(b);
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
	
	public static String byte2hex(int aByte) {
		return String.format("%02X", aByte);	// makes sure single digit are 0 padded
	}
	
	public static byte halfHex2byte(char halfByte) {
		return halfHex2byte(""+halfByte);
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
	
	public static int hex2byte(String hex) {
		if (hex.length() == 1)
			return halfHex2byte(hex);
		else if (hex.length() == 2) 
			return (halfHex2byte(hex.charAt(0)) * 16 + halfHex2byte(hex.charAt(1)));
		else
			throw new IllegalArgumentException("Hex is bigger than a byte (>2 digits) :"+hex);
	}
	
	public static byte[] bytes2bits(byte[] bytes) { // very wasteful in space, don't use preferably
		byte[] bits = new byte[bytes.length*8];
		int c=0;
		for (byte b : bytes) 
			for (int offset=0; offset<8; offset++)
				bits[c++] = (byte) getBit(b, offset);
		return bits;
	}
	
	public static int getBit(int n, int offset) {
		return ((n >> offset) & 1);
	}
	
	public static int setBit(int n, int offset) {
		return (n |= 1 << offset);
	}
	
	public static int clearBit(int n, int offset) {
		return (n &= ~(1 << offset));
	}
	
	public String chain(int n) {
		StringBuilder sb = new StringBuilder();
		while (n-->0) sb.append(sep);
		return sb.toString();
	}
	
	public int[] sublist(int start, int end, int[] bytes) {
		if (start > bytes.length || end < 0) return new int[0];
		if (start < 0) start = 0;
		if (end >= bytes.length) end = bytes.length;
		int[] sublist = new int[end-start];
		for (int i=start; i<end; i++)
			sublist[i-start] = bytes[i];
		return sublist;
	}
	
	public int verify(int[] bytes) {
		return verify(0, bytes);
	}
	
	public static void writeToFile(String name, byte[] bytes) {	//debug
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name))))) {
			for (byte b : bytes) pw.println(b);		// for diffing purposes
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param unmergedBytes
	 * @return index where chain pattern matched
	 */
	public int verify(int start, int[] bytes) {
		String pattern = "", match = "";
		for (int i=0; i<5; i++) pattern += sep;
		int pos = NOT_FOUND, count = pattern.length();
		for (int i=start; i<bytes.length-count; i++, match="") {
			for (int di=0; di<count; di++) match += bytes[i+di];
			if (match.equals(pattern)) return i;
		}
		return pos;
	}
}