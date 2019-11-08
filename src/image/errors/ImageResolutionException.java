package image.errors;

import java.io.IOException;

public class ImageResolutionException extends IOException {
	private static final long serialVersionUID = -5628817206218916283L;

	public ImageResolutionException(String msg) {
		super(msg);
	}
}