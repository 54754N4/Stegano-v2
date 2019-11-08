package file;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

public class ExtractedFile extends HashedFile {
	
	public ExtractedFile(byte[] bytes) throws NoSuchAlgorithmException {
	    super(sha256(bytes), bytes);
	}
	
	public Path writeToFile(String filename) throws IOException {
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filename))) {
		    stream.write(file);
		}
		return Paths.get(filename);
	}
}