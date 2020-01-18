package model;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import audio.PCMTranscoder;
import file.Payload;
import image.error.InvalidChecksumException;
import image.error.NothingToExtractException;

public class AudioStegano {
	private PCMTranscoder metafier;
	
	public AudioStegano(PCMTranscoder metafier) {
		this.metafier = metafier;
	}
	
	public File hide(File payload, File carrier, String name, String format) throws Exception {
		AudioInputStream ais = AudioSystem.getAudioInputStream(carrier);
		byte[] bytes = metafier.metafy(new Payload(payload)),
			rawPCM = PCMTranscoder.extractPCM(ais);
		metafier.verifySize(rawPCM, bytes);
		System.out.println("Size is valid, Encoding data..");
		rawPCM = metafier.write(rawPCM, bytes);
		System.out.println("Encoded data, saving image..");
		File out = metafier.save(
			rawPCM, 
			name, 
			ais.getFormat(), 
			AudioSystem.getAudioFileFormat(carrier).getType()
		);
		ais.close();
		return out;
	}
	
	public ParsedResults extractFile(File carrier) throws UnsupportedAudioFileException, IOException, NoSuchAlgorithmException, InvalidChecksumException {
		byte[] hidden = metafier.extractHidden(carrier);
		if (!hasHiddenData(hidden)) 
			throw new NothingToExtractException();
		return new ParsedResults(metafier, hidden);
	}
	
	// verifies if our encoded metafier chain is unpacked in the first bytes
	public boolean hasHiddenData(byte[] bytes) throws NoSuchAlgorithmException, IOException {
		byte[] topBytes = metafier.sublist(0, 400, bytes);
		return metafier.verify(topBytes) != Metafier.NOT_FOUND;
	}

	public static AudioFileFormat.Type[] getAudioFileTypes() {
		return AudioSystem.getAudioFileTypes();
	}
	
//	public static void main(String[] args) {
//		for (AudioFileFormat.Type type : getAudioFileTypes())
//			System.out.println(type);
//	}
}
