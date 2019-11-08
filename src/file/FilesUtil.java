package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

public final class FilesUtil {
	public static HashedFile hash(File file) throws NoSuchAlgorithmException, IOException {
		return new HashedFile(file);
	}
	
	public static byte[] getBytes(File file) throws FileNotFoundException, IOException {
		return Files.readAllBytes(file.toPath());
	}
}
