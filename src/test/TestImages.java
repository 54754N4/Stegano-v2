package test;

import java.io.File;

import image.AlphaTranscoder;
import image.BatchLSBTranscoder;
import image.LSBTranscoder;
import image.Pixel.Target;
import model.ImageStegano;
import model.ParsedResults;

//check dickbaldwin.com
public class TestImages {
	private static final File image = new File("res/bigimagepf.bmp"),						// image to hide into
		payload = new File("res/aFile.txt");							// file to hide
	
	public static void main(String[] args) throws Exception {
		testAlpha();
		System.out.println();
		testLSB();
		System.out.println();
//		testLSB2();
	}
	
	public static void testAlpha() throws Exception {
		ImageStegano is = new ImageStegano(new AlphaTranscoder("#", 2));		// separator-> metadata + payload 
		File carrier = is.hide(payload, image, "res/newImage.png","png"); 
		System.out.println("Carrier="+carrier.getName());			
		
		ParsedResults extracted = is.extractFile(carrier);				// extracts data from carrier file
		System.out.println("Hidden="+extracted.file.getName()); 		// Also contains other parsed results
	}
	
	public static void testLSB() throws Exception {
		ImageStegano is = new ImageStegano(new LSBTranscoder("#", 2, Target.BLUE));		// separator-> metadata + payload 
		File carrier = is.hide(payload, image, "res/newImage.png","png"); 
		System.out.println("Carrier="+carrier.getName());			
		
		ParsedResults extracted = is.extractFile(carrier);				// extracts data from carrier file
		System.out.println("Hidden="+extracted.file.getName()); 		// Also contains other parsed results
	}
	
	public static void testLSB2() throws Exception {
		ImageStegano is = new ImageStegano(new BatchLSBTranscoder("#", 2));		// separator-> metadata + payload 
		File carrier = is.hide(payload, image, "res/newImage.png","png"); 
		System.out.println("Carrier="+carrier.getName());			
		
		ParsedResults extracted = is.extractFile(carrier);				// extracts data from carrier file
		System.out.println("Hidden="+extracted.file.getName()); 		// Also contains other parsed results
	}

}
