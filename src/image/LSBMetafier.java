package image;

public class LSBMetafier extends AlphaMetafier {

	public LSBMetafier(String sep) {
		super(sep);
	}

	@Override
	protected Pixel hide(Pixel pixel, byte b) {
		if (b > 0xF) System.out.println(b);
		return pixel.hideLSB(b);
	}

	@Override
	protected int unhide(Pixel pixel) {
		return pixel.unhideLSB();
	}
}
