package util;

public class Test {
	public static boolean allUnder(int i, byte[] bytes) {
		for (byte b : bytes) if (b > i) return false;
		return true;
	}
}
