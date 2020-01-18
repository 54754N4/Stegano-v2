package model;

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
import image.PixelTranscoder;
import image.error.InvalidChecksumException;
import image.error.NothingToExtractException;

public class ImageStegano {
	private PixelTranscoder metafier;
	
	public ImageStegano(PixelTranscoder metafier) {
		this.metafier = metafier;
	}
	
	public File hide(File payload, File image, String name, String format) throws Exception {
		BufferedImage bimage = PixelTranscoder.getWriteableImage(image); // isAlphaPre false ???
		byte[] bytes = metafier.metafy(new Payload(payload));
		metafier.verifySize(bimage, bytes);
		System.out.println("Size is valid, Encoding data..");
		metafier.write(bimage, bytes);
		System.out.println("Encoded data, saving image..");
		return metafier.save(bimage, name, format);
	}
	
	public ParsedResults extractFile(File image) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidChecksumException {
		byte[] hidden = metafier.extractHidden(image);
		if (!hasHiddenData(hidden)) 
			throw new NothingToExtractException();
		return new ParsedResults(metafier, hidden);
	}
	
	// verifies if our encoded metafier chain is unpacked in the first bytes
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
	
	public static void main(String[] args) {
		String format = "dwg";
		System.out.println("Readers:");
		ImageStegano.printAvailableReaders(format);
		System.out.println("Writers:");
		ImageStegano.printAvailableWriters(format);
	}
}
