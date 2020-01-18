package error.image;

import java.io.IOException;

public class InvalidImageSubdivisionException extends IOException {
	private static final long serialVersionUID = 1645270134493045352L;
	
	public InvalidImageSubdivisionException(String msg) {
		super(msg);
	}
}