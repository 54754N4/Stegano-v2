package file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

public class HashedFile {
	public final byte[] checksum, fileBytes;
	
	public HashedFile(byte[] checksum, byte[] file) {
		this.checksum = checksum;
		this.fileBytes = file;
	}
	
	public HashedFile(File file) throws NoSuchAlgorithmException, IOException {
		this(file.getName());
	}
	
	public HashedFile(String filename) throws IOException, NoSuchAlgorithmException {
		List<Byte> bytes = new ArrayList<>();
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		try (InputStream is = Files.newInputStream(Paths.get(filename));
		     DigestInputStream dis = new DigestInputStream(is, md)) {
			while (dis.available() > 0) 
				bytes.add((byte) dis.read());
		}
		checksum = md.digest();
		fileBytes = convert(bytes);
	}
	
	public HashedFile(byte[] bytes) throws NoSuchAlgorithmException {
	    this(sha256(bytes), bytes);
	}
	
	public HashedFile(Object[] any) throws NoSuchAlgorithmException {
		this(convertToBytes(any));
	}
	
	private static byte[] convertToBytes(Object[] any) {
		List<byte[]> preconverted = new ArrayList<>();
		for (Object a : any) preconverted.add(a.toString().getBytes());
		long total = 0;
		for (byte[] bytes : preconverted) total += bytes.length;
		if (total > Integer.MAX_VALUE)
			throw new IllegalStateException("Java can't create arrays that big");
		byte[] bytes = new byte[(int) total];
		int c=0;
		for (byte[] bs: preconverted) for (byte b : bs) bytes[c++] = b;
		return bytes;
	}
	
	
	
	public static byte[] sha256(byte[] bytes) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(bytes);
		return md.digest();
	}
	
	public String hexChecksum() {
	    return DatatypeConverter.printHexBinary(checksum).toUpperCase();
	} 
	
	private static byte[] convert(List<Byte> list) {
		byte[] array = new byte[list.size()];
		for (int i=0; i<list.size(); i++)
			array[i] = list.get(i);
		return array;
	} 
}