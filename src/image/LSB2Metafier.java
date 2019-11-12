package image;

public class LSB2Metafier extends ImageMetafier {

	public LSB2Metafier(String sep) {
		super(sep);
	}

	@Override
	protected Pixel hide(Pixel pixel, byte b) throws Exception {
		return pixel.hideLSB2(b);
	}

	@Override
	protected byte unhide(Pixel pixel) {
		return pixel.unhideLSB2();
	}

}
