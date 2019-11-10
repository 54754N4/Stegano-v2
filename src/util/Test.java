package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Test {
	public static boolean allUnder(int i, byte[] bytes) {
		for (byte b : bytes) if (b > i) return false;
		return true;
	}
	
	public static void test(File file) throws FileNotFoundException, IOException {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			System.out.println(bis.available());
			while (bis.available()>0) {
				int ib = bis.read();
				byte b = (byte) ib;
				if (ib < 0 || ib > 127) System.out.println("int: "+ib);
				if (b < 0) System.out.println("byte: "+b);
			}
			System.out.println("Finished "+file);
		}
	}
}
