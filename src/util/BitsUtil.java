package util;

public class BitsUtil {

	public static int getBit(int n, int offset) {
		return ((n >> offset) & 1);
	}

	public static int setBit(int n, int offset) {
		return (n |= 1 << offset);
	}

	public static int clearBit(int n, int offset) {
		return (n &= ~(1 << offset));
	}

	public static int toggleBit(int from, int to, int offset) {
		return (to == 1) ? setBit(from, offset) : clearBit(from, offset);
	}

	public static void main(String[] args) {
		System.out.println(toggleBit(112, 0, 0));
	}
}
