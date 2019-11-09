package image;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import file.Payload;
import model.ImageMetafier;

public class AlphaMetafier extends ImageMetafier {
	public final String hexSep;
	
	public AlphaMetafier(String sep) {
		super(sep);
		hexSep = byte2hex(sep.getBytes(Charset.forName("UTF-8"))[0]);
	}
	
	@Override
	protected Pixel hide(Pixel pixel, int aByte) {
		return pixel.hideAlpha(aByte);
	}

	@Override
	protected int unhide(Pixel pixel) {
		return pixel.unhide();
	}
	
	@Override
	public int[] metafy(Payload payload) {
		return subdivide(super.metafy(payload));
	}
	
	@Override
	public int[] merge(int[] unpacked) {
		System.out.println(String.format("Packing %d into %d bytes", unpacked.length, unpacked.length/2));
		int[] packed = new int[unpacked.length/2];
		int c=0;
		for (int i=0; i<unpacked.length-1; i+=2, c++)
			packed[c] = merge(unpacked[i], unpacked[i+1]);
		return packed;
	}
	
	/** 
	 * Have to duplicate code cause converting from byte[] 
	 * to List<Byte> would require 2 passes over data
	 * @param unmergedBytes
	 * @return index where chain pattern matched
	 */
	@Override
	public int verify(int start, int[] unmergedBytes) {
		String pattern = "", match = "";
		for (int i=0; i<5; i++) pattern += hexSep;
		int pos = NOT_FOUND, count = pattern.length();
		for (int i=start; i<unmergedBytes.length-count; i++, match="") {
			for (int di=0; di<count; di++) match += unmergedBytes[i+di];
			if (match.equals(pattern)) return i;
		}
		return pos;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		String filename = "aFile.txt";
		Payload payload = new Payload(filename);
		AlphaMetafier m = new AlphaMetafier("#");
		System.out.println(m.buildHeader(payload));
		int[] bytes = m.metafy(payload);
		int index = m.verify(5, bytes);
		for (int b : m.sublist(index,index+10, bytes)) 
			System.out.println(b);
	}
}
