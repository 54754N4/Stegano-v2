package examples;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import image.ImageStegano;
import image.errors.ImageWriterNotFoundException;
import image.errors.InvalidChecksumException;
import model.ParsedResults;

public class TestImages {

	public static void main(String[] args) throws ImageWriterNotFoundException, NoSuchAlgorithmException, IOException, InvalidChecksumException {
		File image = new File("bigimagepf.bmp"),				// image to hide into
			payload = new File("aFile.txt");					// file to hide 
		ImageStegano is = new ImageStegano("#");				// separator-> metadata + payload 
		File carrier = is.hide(payload, image, "newImage.png","png"); 
		System.out.println("Carrier="+carrier.getName());			
		
		ParsedResults extracted = is.extractFile(carrier);		// extracts data from carrier file
		System.out.println("Hidden="+extracted.file.getName()); // Also contains other parsed results 
	}

}
