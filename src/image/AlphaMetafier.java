package image;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import file.Payload;
import model.ImageMetafier;
import util.Conversions;

public class AlphaMetafier extends ImageMetafier {
	public final String hexSep;
	
	public AlphaMetafier(String sep) {
		super(sep);
		hexSep = Conversions.byte2hex(sep.getBytes(Charset.forName("UTF-8"))[0]);
	}
	
	@Override
	protected Pixel hide(Pixel pixel, byte b) {
		return pixel.hideAlpha(b);
	}

	@Override
	protected byte unhide(Pixel pixel) {
		return pixel.unhide();
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
		for (int i=0; i<unpacked.length-1; i+=2, c++)
			packed[c] = Conversions.merge(unpacked[i], unpacked[i+1]);
		return packed;
	}
	
	/** 
	 * @param unmergedBytes
	 * @return index where chain pattern matched
	 */
	@Override
	public int verify(int start, byte[] unmergedBytes) {
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
		byte[] bytes = m.metafy(payload);
		int index = m.verify(5, bytes);
		for (byte b : m.sublist(index,index+10, bytes)) 
			System.out.println(b);
	}
}
