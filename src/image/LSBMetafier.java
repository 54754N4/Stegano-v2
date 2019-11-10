package image;

public class LSBMetafier extends AlphaMetafier {
	
	public LSBMetafier(String sep) {
		super(sep);
	}

	@Override
	protected Pixel hide(Pixel pixel, byte b) {
		pixel.hideLSB(b);
		if (pixel.unhideLSB() != b) {
			System.out.println(pixel);
			System.out.println(String.format("%d != %d", b, pixel.unhideLSB()));
		}
		return pixel;
	}

	@Override
	protected byte unhide(Pixel pixel) {
		if (pixel.unhideLSB() > 15) System.out.println(pixel+" + "+pixel.unhideLSB());
		for (int component : pixel.getComponents())
			if (component < 0)
				System.out.println("U"+pixel);
		return pixel.unhideLSB();
	}
}
