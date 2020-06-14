package model.text;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import error.image.ImageWriterNotFoundException;
import error.image.SmallResolutionException;
import error.text.NotEnoughTextException;
import model.Metafier;
import model.image.Pixel;
import util.FastRGB;

public abstract class LineTranscoder extends Metafier {
	
	public LineTranscoder(String sep, int subdivisions) {
		super(sep, subdivisions);
	}
	
	protected abstract String hide(String line, byte b) throws Exception;
	protected abstract byte unhide(String line);
	
	public void write(String[] lines, byte[] bytes) throws Exception {
		int c = 0;	//written bytes count
		for (int x = 0; x < bimage.getWidth(); x++) 
            for (int y = 0; y < bimage.getHeight(); y++) 
            	if (c < bytes.length)
            		bimage.setRGB(x, y, hide(pixels[c], bytes[c++]).toARGB());
		System.out.println(String.format("Affected %d/%d pixels.", c, bimage.getWidth()*bimage.getHeight()));
	}
	
	@Override
	public byte[] extractHidden(File carrier) throws IOException {
		List<String> lines = Files.readAllLines(
			Paths.get(carrier.getAbsolutePath()), 
			StandardCharsets.UTF_8);
		
		byte[] bytes = new byte[pixels.length];
		int c=0;
		for (Pixel pixel : pixels) bytes[c++] = unhide(pixel);
		return bytes;
	}
	
	public void verifySize(String[] lines, byte[] bytes) throws NotEnoughTextException {
		long total = 0;
		for (String line : lines)
			total += countSpaces(line);
		if (bytes.length > (total /= 8))
			throw new NotEnoughTextException(bytes.length, total);
		System.out.println("Total bytes/space = "+bytes.length+"/"+total);
	}
	
	public static long countSpaces(String line) {
		long c = 0;
		for (char character : line.toCharArray())
			if (character == ' ')
				c++;
		return c;
	}

	public File save(String[] lines, String name) throws IOException {
		File out = new File(name);
		try (PrintWriter pw = new PrintWriter(
			new BufferedWriter(
				new OutputStreamWriter(
					new FileOutputStream(out))), false)) { // disable autoflush
			for (String line: lines)
				pw.println(line);
			pw.flush();
		}
		return out;
	}
}