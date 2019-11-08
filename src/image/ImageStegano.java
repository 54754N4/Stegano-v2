package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;

import file.Payload;
import image.errors.ImageWriterNotFoundException;
import image.errors.InvalidChecksumException;
import image.errors.NothingToExtractException;
import image.errors.SmallResolutionException;
import model.BytesParser;
import model.Metafier;

public class ImageStegano {
	private Metafier metafier;
	
	public ImageStegano(String sep) {
		metafier = new Metafier(sep);
	}
	
	public static void main(String[] args) throws ImageWriterNotFoundException, NoSuchAlgorithmException, IOException, InvalidChecksumException {
		File image = new File("bigimagepf.bmp"),
			payload = new File("aFile.txt"),
			carrier;
		String name = "newImage.png", format = "png"; // png is lossless <3
		ImageStegano is = new ImageStegano("#");
		carrier = is.hide(payload, image, name, format);
		System.out.println(carrier);
		System.out.println(is.extractFile(carrier));
	}
	
	public BytesParser extractFile(File image) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidChecksumException {
		byte[] hidden = extractHidden(image);
		if (!hasHiddenData(hidden)) 
			throw new NothingToExtractException();
		return new BytesParser(metafier, hidden);
	}
	
	// verifies if our encoded metafier chain is unpacked in the first pixels
	public boolean hasHiddenData(byte[] bytes) throws NoSuchAlgorithmException, IOException {
		byte[] topBytes = metafier.sublist(0, 100, bytes);
		return metafier.verify(topBytes) != Metafier.NOT_FOUND;
	} 
	
	public static byte[] extractHidden(File image) throws FileNotFoundException, IOException {
		BufferedImage bimage = getWriteableImage(image);
		List<Pixel> pixels = extractPixels(bimage);
		byte[] bytes = new byte[pixels.size()];
		int c=0;
		for (Pixel pixel : pixels) bytes[c++] = (byte) pixel.unhide();
		return bytes;
	}
	
	public File hide(File payload, File image, String name, String format) throws ImageWriterNotFoundException, IOException, NoSuchAlgorithmException {
		BufferedImage bimage = getWriteableImage(image);
		byte[] bytes = metafier.metafy(new Payload(payload));
		verifySize(bimage, bytes);
		System.out.println("Size is valid, Encoding data..");
		bimage = write(bimage, bytes);
		System.out.println("Encoded data, saving image..");
		return save(bimage, name, format);
	}
	
	public static void printAvailableReaders(String format) {
		Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(format.toUpperCase());
	    while (readers.hasNext())
	        System.out.println("reader: " + readers.next());
	}
	
	public static void printAvailableWriters(String format) {
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format.toUpperCase());
		while (writers.hasNext())
			System.out.println("writer: "+ writers.next());
	}
	
	public File save(BufferedImage bimage, String name, String format) throws IOException {
		File out = new File(name);
		if (!ImageIO.write(bimage, format, out))
			throw new ImageWriterNotFoundException(format);
		return out;
	}
	
	public void verifySize(BufferedImage bimage, byte[] bytes) throws SmallResolutionException {
		int totalPixels = bimage.getWidth() * bimage.getHeight(),
			totalBytes = bytes.length;
		if (totalBytes > totalPixels)
			throw new SmallResolutionException(totalBytes, totalPixels);
		System.out.println("Total pixels/bytes = "+totalPixels+"/"+totalBytes);
		System.out.println(String.format("Image width/height/ratio = %d/%d/%f",bimage.getWidth(), bimage.getHeight(), bimage.getWidth()/(double) bimage.getHeight()));
	}
	
	public static BufferedImage write(BufferedImage bimage, byte[] bytes) throws IOException {
		List<Pixel> pixels = extractPixels(bimage);
		int c = 0;	//written bytes count
		for (int x = 0; x < bimage.getWidth(); x++) 
            for (int y = 0; y < bimage.getHeight(); y++) 
            	if (c < bytes.length) {
            		bimage.setRGB(x, y, pixels.get(c).hide(bytes[c]).toARGB());
            		c++;
            	}
		System.out.println(String.format("Affected %d/%d pixels.", c, bimage.getWidth()*bimage.getHeight()));
		return bimage;
	}
	
	public static List<Pixel> extractPixels(BufferedImage bimage) throws IOException {
		FastRGB frgb = new FastRGB(bimage);
		List<Pixel> colours = new ArrayList<>();
		for (int x = 0; x < bimage.getWidth(); x++)
            for (int y = 0; y < bimage.getHeight(); y++)
            	colours.add(new Pixel(new Color(frgb.getRGB(x, y), true)));
        return colours;
	}
	
	public static BufferedImage getWriteableImage(File image) throws IOException {		//https://www.codeproject.com/Questions/542826/getRBGplusdoesn-tplusreturnplusvalueplussetplusb
		BufferedImage src = ImageIO.read(image),
			writeable = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		writeable.getGraphics().drawImage(src, 0, 0, null);	//null cause not using an ImageObserver
		return writeable;
	}
}
