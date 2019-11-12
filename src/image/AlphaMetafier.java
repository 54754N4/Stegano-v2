package image;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import file.Payload;
import util.Conversions;

public class AlphaMetafier extends ImageMetafier {
	
	public AlphaMetafier(String sep) {
		super(sep);
	}
	
	@Override
	protected Pixel hide(Pixel pixel, byte b) throws Exception {
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
	
	@Override
	public byte[] merge(byte[] unpacked) {
		System.out.println(String.format("Packing %d into %d bytes", unpacked.length, unpacked.length/2));
		byte[] packed = new byte[unpacked.length/2];
		int c=0;
		for (int i=0; i<unpacked.length-1; i+=2)
			packed[c++] = Conversions.merge(unpacked[i], unpacked[i+1]);
		return packed;
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
