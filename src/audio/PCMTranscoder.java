package audio;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import audio.error.SmallAudioCarrierException;
import model.Metafier;

public abstract class PCMTranscoder extends Metafier {
	
	public PCMTranscoder(String sep, int subdivisions) {
		super(sep, subdivisions);
	}
	
	protected abstract byte hide(byte rawPCM, byte b) throws Exception;
	protected abstract byte unhide(byte rawPCM);
	
	public void verifySize(byte[] rawPCM, byte[] bytes) throws SmallAudioCarrierException {
		if (bytes.length > rawPCM.length)
			throw new SmallAudioCarrierException(bytes.length, rawPCM.length);
		System.out.println("Total bytes/pcm bytes = "+bytes.length+"/"+rawPCM.length);
	}
	
	public byte[] write(byte[] rawPCM, byte[] bytes) throws Exception {
		for (int i=0; i<bytes.length; i++) rawPCM[i] = hide(rawPCM[i], bytes[i]);
		return rawPCM;
	}
	
	public byte[] extractHidden(File carrier) throws IOException, UnsupportedAudioFileException {
		return extractHidden(AudioSystem.getAudioInputStream(carrier));
	}
	
	public byte[] extractHidden(AudioInputStream ais) throws IOException, UnsupportedAudioFileException {
		byte[] rawPCM = extractPCM(ais);
		for (int i=0; i<rawPCM.length; i++) rawPCM[i] = unhide(rawPCM[i]);
		return rawPCM;
	}
	
	public static byte[] extractPCM(AudioInputStream ais) throws UnsupportedAudioFileException, IOException {
		try (
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			BufferedInputStream bis = new BufferedInputStream(ais) ) {
			int read;
			byte[] buffer = new byte[1024];
			while ((read = bis.read(buffer)) > 0) bos.write(buffer, 0, read);
			bos.flush();
			return bos.toByteArray();
		}
	}
	
	public File save(byte[] rawPCM, String name, AudioFormat fileFormat, AudioFileFormat.Type format) throws IOException {
		File out = new File(name);
		AudioInputStream stream = new AudioInputStream(
			new ByteArrayInputStream(rawPCM),
			fileFormat,
			rawPCM.length
		);
		int written = AudioSystem.write(stream, format, out);
		System.out.println("Wrote "+written+" bytes to file "+name);
		return out;
	}
}
