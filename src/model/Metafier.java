package model;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import file.Payload;
import util.BitsUtil;

public abstract class Metafier {
	private static final List<Integer> VALID_SUBDIVISIONS = Arrays.asList(1,2,4,8);
	public static final int NOT_FOUND = -1, CHAIN_SIZE = 5;
	public final String sep, encodedChain;
	protected final int subdivisions, packets;
	
	public Metafier(String sep, int subdivisions) {
		this.sep = sep;
		if (!VALID_SUBDIVISIONS.contains(subdivisions))
			throw new IllegalArgumentException("Has to be a power of 2 <= 8");
		this.subdivisions = subdivisions;
		packets = 8/subdivisions;
		String encodedSep = "", pattern = "";
		for (int b : BitsUtil.subdivide(
				sep.getBytes(StandardCharsets.UTF_8)[0], 
				subdivisions))
			encodedSep += b;
		for (int i=0; i<CHAIN_SIZE; i++) 
			pattern += encodedSep;
		encodedChain = pattern;
	}
	
	public byte[] metafy(Payload payload) {
		System.out.println(String.format("Pre-Infusion SHA256=%s", payload.hexChecksum()));
		String header = buildHeader(payload);
		byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8),
			total = new byte[headerBytes.length+payload.fileBytes.length];
		int c = 0;
		for (byte b : headerBytes) total[c++] = b;
		for (byte b : payload.fileBytes) total[c++] = b;
		return subdivide(total);
	}
	
	protected String buildHeader(Payload payload) {
		String chain = chain(CHAIN_SIZE);
		StringBuilder sb = new StringBuilder();
		sb.append(chain);
		sb.append(subdivisions);
		sb.append(sep);
		sb.append(payload.format);				// has to be size 3
		sb.append(sep);
		sb.append(payload.hexChecksum());
		sb.append(sep);
		sb.append(payload.fileBytes.length);	// length has to be last, cause it's variable length
		sb.append(chain);
		return sb.toString();
	}
	
	public String chain(int n) {
		StringBuilder sb = new StringBuilder();
		while (n-->0) sb.append(sep);
		return sb.toString();
	}
	
	public byte[] merge(byte[] unpacked) {
		System.out.println(String.format("Packing %d into %d bytes", unpacked.length, unpacked.length/packets));
		byte[] packed = new byte[unpacked.length/packets];
		int[] buffer = new int[packets];
		for (int i=0, c=0; i<unpacked.length; i+=packets) {
			for (int j=0; j<packets; j++) 
				buffer[j] = unpacked[i+j];
			packed[c++] = (byte) BitsUtil.merge(buffer, subdivisions);
		}
		return packed;
	}
	
	public byte[] subdivide(byte[] bytes) {
		System.out.println(String.format("Subdividing %d into %d bytes", bytes.length, bytes.length*packets));
		int c = 0;
		byte[] divided = new byte[bytes.length*packets];
		for (byte aByte : bytes)
			for (int b : BitsUtil.subdivide(aByte, subdivisions))
				divided[c++] = (byte) b;
		return divided;
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
		String match = "";
		int pos = NOT_FOUND, count = encodedChain.length();
		for (int i=start; i<bytes.length-count; i++, match="") {
			for (int di=0; di<count; di++) {
				match += bytes[i+di];
				if (match.equals(encodedChain)) 
					return i;
			}
		}
		return pos;
	}
}