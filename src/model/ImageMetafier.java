package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import image.FastRGB;
import image.Pixel;
import image.errors.SmallResolutionException;

public abstract class ImageMetafier extends Metafier {
	public static byte[] before;
	
	public ImageMetafier(String sep) {
		super(sep);
	}
	
	protected abstract Pixel hide(Pixel pixel, byte b);
	protected abstract byte unhide(Pixel pixel);
	
	public void write(BufferedImage bimage, byte[] bytes) throws IOException {
		Pixel[] pixels = extractPixels(bimage);
		before = bytes;
		int c = 0;	//written bytes count
		for (int x = 0; x < bimage.getWidth(); x++) 
            for (int y = 0; y < bimage.getHeight(); y++) 
            	if (c < bytes.length)
            		bimage.setRGB(x, y, hide(pixels[c], bytes[c++]).toARGB());
		System.out.println(String.format("Affected %d/%d pixels.", c, bimage.getWidth()*bimage.getHeight()));
	}
	
	public byte[] extractHidden(File image) throws IOException {
		BufferedImage bimage = getWriteableImage(image);	
		Pixel[] pixels = extractPixels(bimage);
		byte[] bytes = new byte[pixels.length];
		int c=0;
		for (Pixel pixel : pixels) bytes[c++] = unhide(pixel);
		for (int i=0; i<before.length; i++)	// verification code
			if (before[i] != bytes[i]) 
				System.err.println(String.format("%d: %s != %s", i, before[i], bytes[i]));
		return bytes;
	}
	
	public void verifySize(BufferedImage bimage, byte[] bytes) throws SmallResolutionException {
		int totalPixels = bimage.getWidth() * bimage.getHeight();
		if (bytes.length > totalPixels)
			throw new SmallResolutionException(bytes.length, totalPixels);
		System.out.println("Total bytes/pixels = "+bytes.length+"/"+totalPixels);
		System.out.println(String.format("Image width/height/ratio = %d/%d/%f",bimage.getWidth(), bimage.getHeight(), bimage.getWidth()/(double) bimage.getHeight()));
	}
	
	public static BufferedImage getWriteableImage(File image) throws IOException {		//https://www.codeproject.com/Questions/542826/getRBGplusdoesn-tplusreturnplusvalueplussetplusb
		BufferedImage src = ImageIO.read(image),
			writeable = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		writeable.getGraphics().drawImage(src, 0, 0, null);	//null cause not using an ImageObserver
		return writeable;
	}
	
	public static Pixel[] extractPixels(BufferedImage bimage) throws IOException {
		FastRGB frgb = new FastRGB(bimage);
		Pixel[] colours = new Pixel[bimage.getWidth()*bimage.getHeight()];
		int count = 0;
		for (int x = 0; x < bimage.getWidth(); x++)
            for (int y = 0; y < bimage.getHeight(); y++)
            	colours[count++] = new Pixel(new Color(frgb.getRGB(x, y), true));
        return colours;
	}
}
