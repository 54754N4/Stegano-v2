package model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import file.Payload;
import util.Conversions;

public abstract class Metafier {
	public static final int NOT_FOUND = -1, CHAIN_SIZE = 5;
	public final String sep, hexSep;
	
	public Metafier(String sep) {
		this.sep = sep;
		hexSep = Conversions.byte2hex(sep.getBytes(Charset.forName("UTF-8"))[0]);
	}
	
	public byte[] metafy(Payload payload) {
		System.out.println(String.format("Pre-Infusion SHA256=%s", payload.hexChecksum()));
		String header = buildHeader(payload);
		byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8),
			total = new byte[headerBytes.length+payload.fileBytes.length];
		int c = 0;
		for (byte b : headerBytes) total[c++] = b;
		for (byte b : payload.fileBytes) total[c++] = b;
		return total;
	}
	
	protected String buildHeader(Payload payload) {
		String chain = chain(CHAIN_SIZE);
		StringBuilder sb = new StringBuilder();
		sb.append(chain);
		sb.append(2);
		sb.append(sep);
		sb.append(payload.format);				// has to be size 3
		sb.append(sep);
		sb.append(payload.hexChecksum());
		sb.append(sep);
		sb.append(payload.fileBytes.length);	// length has to be last, cause it's variable length
		sb.append(chain);
		return sb.toString();
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
		for (int i=0; i<CHAIN_SIZE; i++) pattern += hexSep;
		int pos = NOT_FOUND, count = pattern.length();
		for (int i=start; i<bytes.length-count; i++, match="") {
			for (int di=0; di<count; di++) {
				match += Integer.toHexString(bytes[i+di]);
				if (match.equals(pattern)) 
					return i;
			}
		}
		return pos;
	}
}