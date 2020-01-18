package model.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import util.Conversions;
import util.FastRGB;

public abstract class BatchTranscoder extends PixelTranscoder {
	
	public BatchTranscoder(String sep, int subdivisions) {
		super(sep, subdivisions);
	}
	
	protected abstract Pixel batchHide(Pixel pixel, byte[] b) throws Exception;
	protected abstract byte[] batchUnhide(Pixel pixel);
	
	@Override
	public void write(BufferedImage bimage, byte[] bytes) throws Exception {
		FastRGB frgb = new FastRGB(bimage);
		int c = 0;	//written bytes count
		byte[][] packed = Conversions.pack(bytes);
		for (int x = 0; x < bimage.getWidth(); x++) 
            for (int y = 0; y < bimage.getHeight(); y++) 
            	if (c < packed.length)
            		bimage.setRGB(x, y, batchHide(new Pixel(frgb.getRGB(x, y)), packed[c++]).toARGB());
		System.out.println(String.format("Affected %d/%d pixels.", c, bimage.getWidth()*bimage.getHeight()));
	}
	
	@Override
	public byte[] extractHidden(File image) throws IOException {
		BufferedImage bimage = getWriteableImage(image);	
		FastRGB frgb = new FastRGB(bimage);
		int total = bimage.getHeight()*bimage.getWidth();
		byte[][] bytes = new byte[total][];
		for (int i=0; i<total; i++) 
			bytes[i] = batchUnhide(
				new Pixel(
					frgb.getRGB(
						i/bimage.getHeight(), 
						i%bimage.getHeight())));
		return Conversions.flatten(bytes);
	}
	
	@Override
	protected Pixel hide(Pixel pixel, byte b) throws Exception {
		throw new IllegalAccessError();
	}
	
	@Override
	protected byte unhide(Pixel pixel) {
		throw new IllegalAccessError();
	}
}
