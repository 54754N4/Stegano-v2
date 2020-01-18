package file;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Payload extends HashedFile {
	public final String format;
	
	public Payload(File file) throws NoSuchAlgorithmException, IOException {
		this(file.getPath());
	}
	
	public Payload(String filename) throws NoSuchAlgorithmException, IOException {
		super(filename);
		format = parseFormat(filename);
	}
	
	private String parseFormat(String filename) {
		int dotIndex;
		return ((dotIndex = filename.indexOf('.')) == -1) ? 
			"" : filename.substring(dotIndex+1);
	}
	
}
