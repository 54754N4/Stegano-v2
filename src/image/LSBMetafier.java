package image;

public class LSBMetafier extends AlphaMetafier {

	public LSBMetafier(String sep) {
		super(sep);
	}

	@Override
	protected Pixel hide(Pixel pixel, int aByte) {
		return pixel.hideLSB(aByte);
	}

	@Override
	protected int unhide(Pixel pixel) {
		return pixel.unhideLSB();
	}
}
