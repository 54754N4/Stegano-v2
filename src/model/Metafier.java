package model;

import java.nio.charset.Charset;

import file.Payload;
import util.Conversions;

public abstract class Metafier {
	public static final int NOT_FOUND = -1;
	public final String sep;
	
	public Metafier(String sep) {
		this.sep = sep;
	}
	
	public byte[] metafy(Payload payload) {
		System.out.println(String.format("Pre-Infusion SHA256=%s", payload.hexChecksum()));
		String header = buildHeader(payload);
		byte[] headerBytes = header.getBytes(Charset.forName("UTF-8")),
			total = new byte[headerBytes.length+payload.fileBytes.length];
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
	public byte[] subdivide(byte[] bytes) {
		byte[] divided = new byte[bytes.length*2];
		int i=0;
		for (String hexByte : Conversions.bytes2hex(bytes)) {
			divided[i++] = Conversions.hex2byte(""+hexByte.charAt(0));
			divided[i++] = Conversions.hex2byte(""+hexByte.charAt(1));
		}
		System.out.println(String.format("Encoded %d bytes", divided.length));
		return divided;
	}
	
	public byte[] merge(byte[] unpacked) {
		return unpacked;
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
	
	public int verify(byte[] bytes) {
		return verify(0, bytes);
	}
	
	/**
	 * @param unmergedBytes
	 * @return index where chain pattern matched
	 */
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
}