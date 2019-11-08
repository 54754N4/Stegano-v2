package image;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import file.Payload;
import model.ImageMetafier;

public class AlphaMetafier extends ImageMetafier {
	public final String hexSep;
	
	public AlphaMetafier(String sep) {
		super(sep);
		hexSep = byte2hex(sep.getBytes(Charset.forName("UTF-8"))[0]);
	}
	
	@Override
	public byte[] metafy(Payload payload) {
		return subdivide(super.metafy(payload));
	}
	
	@Override
	public byte[] merge(byte[] unpacked) {
		System.out.println(String.format("Packing %d into %d bytes", unpacked.length, unpacked.length/2));
		byte[] packed = new byte[unpacked.length/2];
		int c=0;
		for (int i=0; i<unpacked.length-1; i+=2)
			packed[c++] = merge(unpacked[i], unpacked[i+1]);
		return packed;
	}
	
	@Override
	public int verify(List<Byte> unmergedBytes) {
		return verify(0, unmergedBytes);
	}
	
	@Override
	public int verify(byte[] unmergedBytes) {
		return verify(0, unmergedBytes);
	}
	
	// Have to duplicate code cause converting from byte[] to List<Byte> requires 2 passes over data
	@Override
	public int verify(int start, byte[] unmergedBytes) {
		String pattern = "", match = "";
		for (int i=0; i<5; i++) pattern += hexSep;
		int pos = NOT_FOUND, count = 10; // cause chain = 5 and packets get split into 2 so 2*5
		for (int i=start; i<unmergedBytes.length-count; i++, match="") {
			for (int di=0; di<count; di++) match += unmergedBytes[i+di];
			if (match.equals(pattern)) return i;
		}
		return pos;
	}
	
	/**
	 * @param unmergedBytes
	 * @return index where chain pattern matched
	 */
	@Override
	public int verify(int start, List<Byte> unmergedBytes) {
		String pattern = "", match = "";
		for (int i=0; i<5; i++) pattern += hexSep;
		int pos = NOT_FOUND, count = 10; // cause chain = 5 and packets get split into 2 so 2*5
		for (int i=start; i<unmergedBytes.size()-count; i++, match="") {
			for (int di=0; di<count; di++) match += unmergedBytes.get(i+di);
			if (match.equals(pattern)) return i;
		}
		return pos;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		String filename = "aFile.txt";
		Payload payload = new Payload(filename);
		AlphaMetafier m = new AlphaMetafier("#");
		System.out.println(m.buildHeader(payload));
		byte[] bytes = m.metafy(payload);
		int index = m.verify(5, bytes);
		for (int b : m.sublist(index,index+10, bytes)) 
			System.out.println(b);
	}
}
