package error.file;

import java.io.IOException;

public class NothingToExtractException extends IOException {
	private static final long serialVersionUID = 4829279617324180002L;
	
	public NothingToExtractException() {
		super("No payload detected.");
	}
}