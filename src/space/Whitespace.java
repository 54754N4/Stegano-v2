package space;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Whitespace {
	private static final String SPACE = " ";

	public static String spaces(int n) {
		StringBuilder sb = new StringBuilder();
		while (n-->0) sb.append(SPACE);
		return sb.toString();
	}
	
	public static Path encode(Path payload, Path into) throws IOException {
		Path copy = Paths.get("copy");
		List<Integer> bytes = FilesManager.extractBytes(payload.toFile());
		into.toFile().createNewFile();
		copy.toFile().createNewFile();
		try (
			PrintWriter pw = new PrintWriter( 
				new BufferedWriter(
					new OutputStreamWriter(
						new FileOutputStream(copy.toFile()))));
			Stream<String> stream = Files.lines(copy);
		) {
			int i = 0;
			List<String> lines = stream.collect(Collectors.toList());
			for (String line : lines) {
				String str = line;
				if (i < bytes.size()) str += spaces(bytes.get(i++));
				pw.print(str);
			}
			pw.flush();
		}
		return into;
	}
	
	public static Path decode(Path file) throws IOException {
		try (Stream<String> stream = Files.lines(file)) {
			stream.forEach((line) -> {
				
			});
		}
		return file;
	}
	
	public static int count(String line) {
		int count = 0, i = line.length()-1;
		while (line.length() != 0 && line.charAt(i--) == ' ') count++;
		return count;
	}
	
	public static void main(String[] args) {
		
	}
}

abstract class FilesManager {
	
	protected static List<Integer> extractBytes(File file) throws IOException {
		List<Integer> bytes = new ArrayList<Integer>();
		try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file))) {
			int fbyte;
			while ((fbyte = bin.read()) != -1) bytes.add(fbyte);
		}
		return bytes;
	}
	
	protected static String extractFormat(String fileName) {
		int dotIndex = fileName.indexOf('.');
		if (dotIndex == -1)
			throw new IllegalArgumentException("OutFileName has no extension !");
		return fileName.split("\\.")[1];
	}
}