package image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;

import file.Payload;
import image.errors.ImageWriterNotFoundException;
import image.errors.InvalidChecksumException;
import image.errors.NothingToExtractException;
import model.ImageMetafier;
import model.Metafier;
import model.ParsedResults;

public class ImageStegano {
	private ImageMetafier metafier;
	
	public ImageStegano(ImageMetafier metafier) {
		this.metafier = metafier;
	}
	
	public File hide(File payload, File image, String name, String format) throws ImageWriterNotFoundException, IOException, NoSuchAlgorithmException {
		BufferedImage bimage = ImageMetafier.getWriteableImage(image);
		byte[] bytes = metafier.metafy(new Payload(payload));
		metafier.verifySize(bimage, bytes);
		System.out.println("Size is valid, Encoding data..");
		metafier.write(bimage, bytes, (pixel, data) -> pixel.hide(data));
		System.out.println("Encoded data, saving image..");
		return save(bimage, name, format);
	}
	
	public ParsedResults extractFile(File image) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidChecksumException {
		byte[] hidden = metafier.extractHidden(image, (pixel) -> (byte) pixel.unhide());
		if (!hasHiddenData(hidden)) 
			throw new NothingToExtractException();
		return new ParsedResults(metafier, hidden);
	}
	
	// verifies if our encoded metafier chain is unpacked in the first pixels
	public boolean hasHiddenData(byte[] bytes) throws NoSuchAlgorithmException, IOException {
		byte[] topBytes = metafier.sublist(0, 100, bytes);
		return metafier.verify(topBytes) != Metafier.NOT_FOUND;
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
}
