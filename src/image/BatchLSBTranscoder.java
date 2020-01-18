package image;

import image.Pixel.Target;
import util.BitsUtil;

public class BatchLSBTranscoder extends BatchTranscoder {
	private static final Target[] TARGETS = Target.values();
	
	public BatchLSBTranscoder(String sep, int subdivisions) {
		super(sep, subdivisions);
	}

	@Override
	protected Pixel batchHide(Pixel pixel, byte[] bytes) throws Exception {
		for (int i=0; i<TARGETS.length; i++)
			pixel.set(
				TARGETS[i], 
				BitsUtil.copy(pixel.get(TARGETS[i]), bytes[i], subdivisions, 0));
		return pixel;
	}

	@Override
	protected byte[] batchUnhide(Pixel pixel) {
		byte[] revealed = new byte[4];
		for (int i=0; i<TARGETS.length; i++)
			revealed[i] = 
				(byte) BitsUtil.copy(0, pixel.get(TARGETS[i]), subdivisions, 0);
		return revealed;
	}
}
