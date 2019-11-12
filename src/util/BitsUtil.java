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

	public static int toggleBit(int from, int bit, int offset) {
		return (bit == 1) ? setBit(from, offset) : clearBit(from, offset);
	}
	
	public static int copy(int to, int from, int count) {
		for (int i=0; i<count; i++)
			to = toggleBit(to, getBit(from, i), i);
		return to;
	} 
	
	public static int getBits(int n, int count) {
		return getBits(n, 0, count);
	}
	
	public static int getBits(int n, int from, int to) {
		int bits = 0;
		for (int i=from; i<to; i++) bits += getBit(n, i) << (i-from);
		return bits;
	}

	public static void main(String[] args) {
		System.out.println(Integer.toBinaryString(getBits(0b11110101, 4, 10)));
	}
}
