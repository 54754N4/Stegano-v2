package model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import file.ExtractedFile;
import image.error.ExtractingLengthFailedException;
import image.error.InvalidChecksumException;

public class ParsedResults {
	private Metafier metafier;
	public byte[] bytes, payload;
	public int subdivisions, length;
	public String format, checksum, header;
	public ExtractedFile extracted;
	public File file;
	
	public ParsedResults(Metafier metafier, byte[] bytes) throws NoSuchAlgorithmException, IOException, InvalidChecksumException {
		this.metafier = metafier;
		this.bytes = bytes;
		separatePayload(parseHeader(bytes));
	}
	
	private int parseHeader(byte[] hidden) throws ExtractingLengthFailedException {
		int chain = metafier.verify(metafier.encodedChain.length(), hidden), end = chain+metafier.packets*5;
		byte[] unmerged = new byte[end];
		for (int i=0; i<end; i++) unmerged[i] = hidden[i];
		header = new String(metafier.merge(unmerged), StandardCharsets.UTF_8);
		String[] split = filter(header.split(metafier.sep+"+"));
		subdivisions = Integer.parseInt(split[0]);
		System.out.println(String.format("Subdivisions=%d", subdivisions));
		format = split[1];
		System.out.println(String.format("Format=%s", format));
		checksum = split[2].toUpperCase();
		System.out.println(String.format("Header Checksum=%s", checksum));
		length = Integer.parseInt(split[3]);
		System.out.println(String.format("Bytes length=%d", length));
		return end;
	}
	
	private String[] filter(String[] split) {
		List<String> strings = new ArrayList<>();
		for (int i=0; i<split.length; i++) 
			if (!split[i].equals(""))
				strings.add(split[i]);
		return strings.toArray(new String[0]);
	}
	
	private void separatePayload(int start) throws NoSuchAlgorithmException, IOException, InvalidChecksumException {
		byte[] unmerged = new byte[length*metafier.packets];
		for (int i=start, c=0; i<start+unmerged.length; i++, c++) unmerged[c] = bytes[i];
		payload = metafier.merge(unmerged);
		extracted = new ExtractedFile(payload);
		file = extracted.writeToFile("res/extracted."+format);
		if (!checksum.equals(extracted.hexChecksum())) 
			throw new InvalidChecksumException(
				String.format("%nHeader=\t\t%s%nExtracted=\t%s", checksum, extracted.hexChecksum()));
		System.out.println(String.format("Post-Extraction SHA256=%s", checksum));
	}
	
	public static byte[] ints2bytes(int[] ints) {
		byte[] bytes = new byte[ints.length];
		int c=0;
		for (int i: ints) bytes[c++] = (byte) i;
		return bytes;
	}
	
	public static File writeToFile(String filename, int[] bytes) throws IOException {
		File out = new File(filename);
		if (!out.exists()) out.createNewFile();
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out))) {
			for (int aByte : bytes)
				bos.write(aByte);
			bos.flush();
		}
		return out;
	}
}
