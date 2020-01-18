package util;

import java.util.Arrays;

public class BitsUtil {

	public static int setBit(int n, int offset) {
		return (n |= 1 << offset);
	}

	public static int clearBit(int n, int offset) {
		return (n &= ~(1 << offset));
	}
	
	public static int getBit(int n, int offset) {
		return ((n >> offset) & 1);
	}
	
	public static int getBits(int n, int count) {
		return getBits(n, 0, count);
	}
	
	public static int getBits(int n, int from, int to) {
		int bits = 0;
		for (int i=from; i<to; i++) bits += getBit(n, i) << (i-from);
		return bits;
	}

	public static int toggleBit(int from, int bit, int offset) {
		return (bit == 1) ? setBit(from, offset) : clearBit(from, offset);
	}
	
	public static int copy(int to, int from, int count, int offset) {
		for (int i=0; i<count; i++)
			to = toggleBit(to, getBit(from, i), i+offset);
		return to;
	}
	
	public static int getMask(int bits) {
		int mask = 0;
		for (int i=0; i<bits; i++) 
			mask += 1<<i;
		return mask;
	}
	
	public static int[] subdivide(int num, int bits) {
		int mask = getMask(bits), max = 8/bits;
		int[] bytes = new int[max];
		for (int i=0; i/bits<max; i+=bits)
			bytes[i/bits] = (num & mask<<i) >> i;
		return bytes;
	}
	
	public static int merge(int[] bytes, int bits) {
		int result = 0;
		for (int i=0; i<bytes.length; i++)
			result |= bytes[i] << (i*bits);
		return result;
	}
	
	public static void main(String[] args) {
//		System.out.println(Integer.toBinaryString(0b11110101));
//		System.out.println(Integer.toBinaryString(copy(0b11111111, 0b00000101, 4, 2)));
//		System.out.println(Integer.toBinaryString(0b11110101 & (getMask(5)<<1)));
		System.out.println(Integer.toBinaryString(0b11110101));
		int subdivisions = 8;
		int[] bytes = subdivide(0b11111111111111111111111111110101, subdivisions);
		System.out.println(Arrays.toString(bytes));
		System.out.println(Integer.toBinaryString(merge(bytes, subdivisions)));
	}
}
