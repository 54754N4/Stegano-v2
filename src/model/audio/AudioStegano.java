package model.audio;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import file.Payload;
import model.Stegano;

public class AudioStegano extends Stegano {
	private PCMTranscoder metafier;
	
	public AudioStegano(PCMTranscoder metafier) {
		super(metafier);
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

	public static AudioFileFormat.Type[] getAudioFileTypes() {
		return AudioSystem.getAudioFileTypes();
	}
	
	public static void main(String[] args) {
		for (AudioFileFormat.Type type : getAudioFileTypes())
			System.out.println(type);
	}
}
