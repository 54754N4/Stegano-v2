package model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import file.ExtractedFile;
import image.errors.ExtractingLengthFailedException;
import image.errors.InvalidChecksumException;

public class ParsedResults {
	private Metafier metafier;
	public byte[] bytes, header, payload;
	public int subdivisions, length;
	public String format, checksum;
	public ExtractedFile extracted;
	public File file;
	
	public ParsedResults(Metafier metafier, byte[] bytes) throws NoSuchAlgorithmException, IOException, InvalidChecksumException {
		this.metafier = metafier;
		this.bytes = bytes;
		separatePayload(parseHeader(bytes));
	}
	
	private int parseHeader(byte[] hidden) throws ExtractingLengthFailedException {
		int chain = metafier.verify(10, hidden), end = chain+10;
		byte[] unmerged = new byte[end];
		for (int i=0; i<end; i++) unmerged[i] = hidden[i];
		header = metafier.merge(unmerged);
		subdivisions = Integer.parseInt(new String(new byte[] {(byte) header[5]}));
		System.out.println(String.format("Subdivisions=%d", subdivisions));
		format = new String(metafier.sublist(7, 10, header));
		System.out.println(String.format("Format=%s", format));
		checksum = new String(metafier.sublist(11, 75, header)).toUpperCase();
		System.out.println(String.format("Header Checksum=%s", checksum));
		Matcher m = Pattern.compile("\\d+")	// extract length only (digits)
			.matcher(new String(metafier.sublist(76, header.length, header)));
		if (!m.find()) throw new ExtractingLengthFailedException();
		length = Integer.parseInt(m.group());
		System.out.println(String.format("Bytes length=%d", length));
		return end;
	}
	
	private void separatePayload(int start) throws NoSuchAlgorithmException, IOException, InvalidChecksumException {
		byte[] unmerged = new byte[length*2];
		for (int i=start, c=0; i<start+unmerged.length; i++, c++) unmerged[c] = bytes[i];
		payload = metafier.merge(unmerged);
		extracted = new ExtractedFile(payload);
		file = extracted.writeToFile("extracted."+format);
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
