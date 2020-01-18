package image.error;

import java.io.IOException;

public class ImageWriterNotFoundException extends IOException {
	private static final long serialVersionUID = 1645270134493045352L;
	
	public ImageWriterNotFoundException(String format) {
		super("Failed to find image writer for "+format+" format");
	}
}