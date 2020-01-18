package error.audio;

import javax.sound.sampled.AudioFileFormat;

public class AudioWriterNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -8047752908976943184L;

	public AudioWriterNotFoundException(AudioFileFormat.Type type) {
		super("Audio writer for type "+type+" is not supported.");
	}
}
