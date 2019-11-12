package image;

import util.Conversions;

public class LSBMetafier extends AlphaMetafier {

	public LSBMetafier(String sep) {
		super(sep);
	}

	@Override
	protected Pixel hide(Pixel pixel, byte b) {
		return pixel.hideLSB(b);
	}

	@Override
	protected byte unhide(Pixel pixel) {
		return pixel.unhideLSB();
	}
	
	@Override
	public byte[] merge(byte[] unpacked) {	// somehow bytes aren't the same afterwards.. = broken
		System.out.println(String.format("Packing %d into %d bytes", unpacked.length, unpacked.length/2));
		byte[] packed = new byte[unpacked.length/2];
		int c=0;
		for (int i=0; i<unpacked.length-1; i+=2)
			packed[c++] = Conversions.merge(unpacked[i], unpacked[i+1]);
		return packed;
	}
}
