package image;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import file.Payload;

public class AlphaTranscoder extends PixelTranscoder {
	
	public AlphaTranscoder(String sep, int subdivisions) {
		super(sep, subdivisions);
	}
	
	@Override
	protected Pixel hide(Pixel pixel, byte b) throws Exception {
		return pixel.hideAlpha(b);
	}

	@Override
	protected byte unhide(Pixel pixel) {
		return pixel.unhide();
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		String filename = "aFile.txt";
		Payload payload = new Payload(filename);
		AlphaTranscoder m = new AlphaTranscoder("#", 2);
		System.out.println(m.buildHeader(payload));
		byte[] bytes = m.metafy(payload);
		int index = m.verify(5, bytes);
		for (byte b : m.sublist(index,index+10, bytes)) 
			System.out.println(b);
	}
}
