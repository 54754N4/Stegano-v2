package image.error;

import java.io.IOException;

public class ExtractingLengthFailedException extends IOException {
	private static final long serialVersionUID = -1637351973787001088L;
	
	public ExtractingLengthFailedException() {
		super("Failed to extract data length, header might be corrupted");
	}
}
