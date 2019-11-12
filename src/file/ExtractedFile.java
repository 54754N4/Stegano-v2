package file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

public class ExtractedFile extends HashedFile {
	private File file;
	
	public ExtractedFile(File file) throws NoSuchAlgorithmException, IOException {
		super(file);
		this.file = file;
	}
	
	public ExtractedFile(byte[] bytes) throws NoSuchAlgorithmException {
	    super(bytes);
	}
	
	public File getFile() {
		return file;
	}
	
	public File writeToFile(String filename) throws IOException {
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filename))) {
		    stream.write(fileBytes);
		}
		return Paths.get(filename).toFile();
	}
}