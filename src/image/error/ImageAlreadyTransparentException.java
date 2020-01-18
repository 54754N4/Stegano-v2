package image.error;

import java.io.IOException;

public class ImageAlreadyTransparentException extends IOException {
	private static final long serialVersionUID = -7961893063003749096L;
	
	public ImageAlreadyTransparentException() {
		super("There's alerady some transparent pixels in this image!");
	}
}