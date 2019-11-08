package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import image.FastRGB;
import image.Pixel;
import image.errors.SmallResolutionException;

public abstract class ImageMetafier extends Metafier {

	public ImageMetafier(String sep) {
		super(sep);
	}
	
	protected abstract PixelHidingHandler getHidingHandler();
	protected abstract PixelUnhidingHandlder getUnhidingHandler();
	
	public void write(BufferedImage bimage, byte[] bytes) throws IOException {
		List<Pixel> pixels = extractPixels(bimage);
		PixelHidingHandler handler = getHidingHandler();
		int c = 0;	//written bytes count
		for (int x = 0; x < bimage.getWidth(); x++) 
            for (int y = 0; y < bimage.getHeight(); y++) 
            	if (c < bytes.length)
            		bimage.setRGB(x, y, handler.hide(pixels.get(c), bytes[c++]).toARGB());
		System.out.println(String.format("Affected %d/%d pixels.", c, bimage.getWidth()*bimage.getHeight()));
	}
	public void verifySize(BufferedImage bimage, byte[] bytes) throws SmallResolutionException {
		int totalPixels = bimage.getWidth() * bimage.getHeight(),
			totalBytes = bytes.length;
		if (totalBytes > totalPixels)
			throw new SmallResolutionException(totalBytes, totalPixels);
		System.out.println("Total pixels/bytes = "+totalPixels+"/"+totalBytes);
		System.out.println(String.format("Image width/height/ratio = %d/%d/%f",bimage.getWidth(), bimage.getHeight(), bimage.getWidth()/(double) bimage.getHeight()));
	}
	
	public static BufferedImage getWriteableImage(File image) throws IOException {		//https://www.codeproject.com/Questions/542826/getRBGplusdoesn-tplusreturnplusvalueplussetplusb
		BufferedImage src = ImageIO.read(image),
			writeable = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		writeable.getGraphics().drawImage(src, 0, 0, null);	//null cause not using an ImageObserver
		return writeable;
	}
	
	public byte[] extractHidden(File image) throws FileNotFoundException, IOException {
		BufferedImage bimage = getWriteableImage(image);	
		List<Pixel> pixels = extractPixels(bimage);
		PixelUnhidingHandlder handler = getUnhidingHandler();
		byte[] bytes = new byte[pixels.size()];
		int c=0;
		for (Pixel pixel : pixels) bytes[c++] = handler.unhide(pixel);
		return bytes;
	}
	
	public static List<Pixel> extractPixels(BufferedImage bimage) throws IOException {
		FastRGB frgb = new FastRGB(bimage);
		List<Pixel> colours = new ArrayList<>();
		for (int x = 0; x < bimage.getWidth(); x++)
            for (int y = 0; y < bimage.getHeight(); y++)
            	colours.add(new Pixel(new Color(frgb.getRGB(x, y), true)));
        return colours;
	}
	
	@FunctionalInterface
	public static interface PixelHidingHandler extends HidingHandler {
		public Pixel hide(Pixel pixel, byte data);
	}
	
	@FunctionalInterface
	public static interface PixelUnhidingHandlder extends UnhidingHandler {
		public byte unhide(Pixel pixel);
	}
}
