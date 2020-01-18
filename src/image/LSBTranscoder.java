package image;

import image.Pixel.Target;
import util.BitsUtil;

public class LSBTranscoder extends PixelTranscoder {
	private Target target;
	
	public LSBTranscoder(String sep, int subdivisions, Target target) {
		super(sep, subdivisions);
		this.target = target;
	}

	@Override
	protected Pixel hide(Pixel pixel, byte b) throws Exception {
		return pixel.set(
			target, 
			BitsUtil.copy(pixel.get(target), b, subdivisions, 0));
	}

	@Override
	protected byte unhide(Pixel pixel) {
		return (byte) BitsUtil.copy(0, pixel.get(target), subdivisions, 0);
	}

}