package examples;

import java.io.File;

import image.AlphaMetafier;
import image.LSB2Metafier;
import image.LSBMetafier;
import model.ImageStegano;
import model.ParsedResults;

public class TestImages {
	private static final File image = new File("bigimagepf.bmp"),						// image to hide into
		payload = new File("aFile.txt");							// file to hide
	
	public static void main(String[] args) throws Exception {
		testAlpha();
		System.out.println();
		testLSB2();
	}
	
	public static void testAlpha() throws Exception {
		ImageStegano is = new ImageStegano(new AlphaMetafier("#"));		// separator-> metadata + payload 
		File carrier = is.hide(payload, image, "newImage.png","png"); 
		System.out.println("Carrier="+carrier.getName());			
		
		ParsedResults extracted = is.extractFile(carrier);				// extracts data from carrier file
		System.out.println("Hidden="+extracted.file.getName()); 		// Also contains other parsed results
	}
	
	public static void testLSB() throws Exception {
		ImageStegano is = new ImageStegano(new LSBMetafier("#"));		// separator-> metadata + payload 
		File carrier = is.hide(payload, image, "newImage.png","png"); 
		System.out.println("Carrier="+carrier.getName());			
		
		ParsedResults extracted = is.extractFile(carrier);				// extracts data from carrier file
		System.out.println("Hidden="+extracted.file.getName()); 		// Also contains other parsed results
	}
	
	public static void testLSB2() throws Exception {
		ImageStegano is = new ImageStegano(new LSB2Metafier("#"));		// separator-> metadata + payload 
		File carrier = is.hide(payload, image, "newImage.png","png"); 
		System.out.println("Carrier="+carrier.getName());			
		
		ParsedResults extracted = is.extractFile(carrier);				// extracts data from carrier file
		System.out.println("Hidden="+extracted.file.getName()); 		// Also contains other parsed results
	}

}
