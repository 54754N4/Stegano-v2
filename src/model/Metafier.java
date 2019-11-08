package model;

import java.nio.charset.Charset;
import java.util.List;

import file.Payload;

public abstract class Metafier {
	public static final int NOT_FOUND = -1;
	public final String sep;
	
	public Metafier(String sep) {
		this.sep = sep;
	}
	
	public byte[] metafy(Payload payload) {
		System.out.println(String.format("Pre-Infusion SHA256=%s", payload.hexChecksum()));
		byte[] header = buildHeader(payload).getBytes(Charset.forName("UTF-8")),
			total = new byte[header.length+payload.file.length];
		int c = 0;
		for (byte b : header) total[c++] = b;
		for (byte b : payload.file) total[c++] = b;
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
		sb.append(payload.file.length);
		sb.append(chain);
		return sb.toString();
	}
	
	// x2 storage space we gain that all bytes are <16 now
	// since we convert each byte to a 2 digit hex string
	public byte[] subdivide(byte[] bytes) {
		byte[] divided = new byte[bytes.length*2];
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
	
	public byte merge(byte high, byte low) {
		return hex2byte(String.format("%s%s", singleHex(high), singleHex(low)));
	}
	
	public byte[] merge(byte[] unpacked) {
		return unpacked;
	}
	
	public static String[] bytes2hex(byte[] bytes) {
		String[] hex = new String[bytes.length];
		int i=0;
		for (Byte b : bytes) hex[i++] = byte2hex(b);
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
	
	public static byte hex2byte(String c) {
		return (byte) Integer.parseInt(c, 16);
	}
	
	public String chain(int n) {
		StringBuilder sb = new StringBuilder();
		while (n-->0) sb.append(sep);
		return sb.toString();
	}
	
	public byte[] sublist(int start, int end, byte[] bytes) {
		if (start > bytes.length || end < 0) return new byte[0];
		if (start < 0) start = 0;
		if (end >= bytes.length) end = bytes.length;
		byte[] sublist = new byte[end-start];
		for (int i=start; i<end; i++)
			sublist[i-start] = bytes[i];
		return sublist;
	}
	
	public int verify(List<Byte> bytes) {
		return verify(0, bytes);
	}
	
	
	public int verify(byte[] bytes) {
		return verify(0, bytes);
	}
	
	// Have to duplicate code cause converting from byte[] to List<Byte> requires 2 passes over data
	public int verify(int start, byte[] bytes) {
		String pattern = "", match = "";
		for (int i=0; i<5; i++) pattern += sep;
		int pos = NOT_FOUND, count = pattern.length();
		for (int i=start; i<bytes.length-count; i++, match="") {
			for (int di=0; di<count; di++) match += bytes[i+di];
			if (match.equals(pattern)) return i;
		}
		return pos;
	}
	
	/**
	 * @param unmergedBytes
	 * @return index where chain pattern matched
	 */
	public int verify(int start, List<Byte> unmergedBytes) {
		String pattern = "", match = "";
		for (int i=0; i<5; i++) pattern += sep;
		int pos = NOT_FOUND, count = pattern.length();
		for (int i=start; i<unmergedBytes.size()-count; i++, match="") {
			for (int di=0; di<count; di++) match += unmergedBytes.get(i+di);
			if (match.equals(pattern)) return i;
		}
		return pos;
	}
	
	public static interface HidingHandler {}
	
	public static interface UnhidingHandler {}
}