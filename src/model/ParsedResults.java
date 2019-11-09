package model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import file.ExtractedFile;
import image.errors.InvalidChecksumException;

public class ParsedResults {
	private Metafier metafier;
	public final int[] bytes;
	public int[] header, payload;
	public int subdivisions, length;
	public String format, checksum;
	public ExtractedFile extracted;
	public File file;
	
	public ParsedResults(Metafier metafier, int[] bytes) throws NoSuchAlgorithmException, IOException, InvalidChecksumException {
		this.metafier = metafier;
		this.bytes = bytes;
		separatePayload(parseHeader(bytes));
	}
	
	private int parseHeader(int[] hidden) {
		int chain = metafier.verify(10, hidden), end = chain+10;
		int[] unmerged = new int[end];
		for (int i=0; i<end; i++) unmerged[i] = hidden[i];
		header = metafier.merge(unmerged);
		subdivisions = Integer.parseInt(new String(new byte[] {(byte) header[5]}));
		System.out.println(String.format("Subdivisions=%d", subdivisions));
		format = new String(ints2bytes(metafier.sublist(7, 10, header)));
		System.out.println(String.format("Format=%s", format));
		checksum = new String(ints2bytes(metafier.sublist(11, 75, header))).toUpperCase();
		System.out.println(String.format("Header Checksum=%s", checksum));
		length = Integer.parseInt(
			new String(ints2bytes(metafier.sublist(76, header.length, header)))
				.replaceAll(metafier.sep, ""));
		System.out.println(String.format("Bytes length=%d", length));
		return end;
	}
	
	private void separatePayload(int start) throws NoSuchAlgorithmException, IOException, InvalidChecksumException {
		int[] unmerged = new int[length*2];
		for (int i=start, c=0; i<start+unmerged.length; i++, c++) unmerged[c] = bytes[i];
		payload = metafier.merge(unmerged);
		file = writeToFile("extracted."+format, payload);
		extracted = new ExtractedFile(file);
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
