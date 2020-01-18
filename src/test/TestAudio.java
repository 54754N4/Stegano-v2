package test;

import java.io.File;

import audio.LSBTranscoder;
import model.AudioStegano;
import model.ParsedResults;

public class TestAudio {
	private static final File song = new File("Under - Alex Hepburn.wav"),	// audio to hide into
		payload = new File("aFile.txt");									// file to hide
	
	public static void main(String[] args) throws Exception {
		testWAV();
	}
	
	public static void testWAV() throws Exception {
		AudioStegano is = new AudioStegano(new LSBTranscoder("#", 1));	// separator-> metadata + payload 
		File carrier = is.hide(payload, song, "carrier.wav","WAV"); 
		System.out.println("Carrier="+carrier.getName());			
		
		ParsedResults extracted = is.extractFile(carrier);				// extracts data from carrier file
		System.out.println("Hidden="+extracted.file.getName()); 		// Also contains other parsed results
	}
	
}
