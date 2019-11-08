package model;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import file.ExtractedFile;
import image.errors.InvalidChecksumException;

public class ParsedResults {
	private Metafier metafier;
	public final byte[] bytes;
	public byte[] header, payload;
	public int subdivisions, length;
	public String format, checksum;
	public ExtractedFile extracted;
	public File file;
	
	public ParsedResults(Metafier metafier, byte[] bytes) throws NoSuchAlgorithmException, IOException, InvalidChecksumException {
		this.metafier = metafier;
		this.bytes = bytes;
		separatePayload(separateHeader(bytes));
	}

	private int separateHeader(byte[] hidden) {
		int chain = metafier.verify(10, hidden), end = chain+10;
		byte[] unmerged = new byte[end];
		for (int i=0; i<end; i++)
			unmerged[i] = hidden[i];
		parseHeader(unmerged);
		return end;
	}
	
	private void parseHeader(byte[] unmerged) {
		header = metafier.merge(unmerged);
		subdivisions = Integer.parseInt(new String(new byte[] {header[5]}));
		format = new String(metafier.sublist(7, 10, header));
		checksum = new String(metafier.sublist(11, 75, header)).toUpperCase();
		length = Integer.parseInt(
			new String(metafier.sublist(76, header.length, header))
				.replaceAll(metafier.sep, ""));
	}
	
	private void separatePayload(int start) throws NoSuchAlgorithmException, IOException, InvalidChecksumException {
		byte[] unmerged = new byte[length*2];
		int c=0;
		for (int i=start; i<start+unmerged.length; i++)
			unmerged[c++] = bytes[i];
		payload = metafier.merge(unmerged);
		extracted = new ExtractedFile(payload);
		String filename = "extracted."+format;
		file = new File(filename);
		extracted.writeToFile(filename);
		if (!checksum.equals(extracted.hexChecksum())) 
			throw new InvalidChecksumException(
				String.format("%nHeader=\t\t%s%nExtracted=\t%s", checksum, extracted.hexChecksum()));
		System.out.println(String.format("Post-Extraction SHA256=%s", checksum));
	}
}
