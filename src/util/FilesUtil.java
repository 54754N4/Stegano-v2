package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

import file.HashedFile;

public final class FilesUtil {
	public static HashedFile hash(File file) throws NoSuchAlgorithmException, IOException {
		return new HashedFile(file);
	}
	
	public static byte[] getBytes(File file) throws FileNotFoundException, IOException {
		return Files.readAllBytes(file.toPath());
	}

	public static void writeToFile(String name, byte[] bytes) {	//debug
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name))))) {
			for (byte b : bytes) pw.println(b);		// for diffing purposes
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
