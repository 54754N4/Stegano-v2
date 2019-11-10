package image;

import java.awt.Color;
import java.util.Arrays;

import util.BitsUtil;

public class Pixel {
	private Color colour;
	private int alpha, red, green, blue;
	
	public Pixel(Color colour) {
		this(colour.getRGB());
		this.colour = colour;
	}
	
	public Pixel(int ARGB) {			
		colour = new Color(ARGB, true);
		alpha = colour.getAlpha();		// ((ARGB & 0xff000000) >> 24);
		red = colour.getRed();			// ((ARGB & 0x00ff0000) >> 16);
		green = colour.getGreen();		// ((ARGB & 0x0000ff00) >> 8);
		blue = colour.getBlue();		// (ARGB & 0x000000ff);
		if (ARGB != toARGB()) 
			System.out.println("WTF");	// should never print but just in cased we messed up
	}
	
	public int[] getComponents() {
		return new int[] {alpha,red,green,blue};
	}
	
	public int getAlpha() {
		return alpha;
	}
	
	public int getRed() {
		return red;
	}
	
	public int getGreen() {
		return green;
	}
	
	public int getBlue() {
		return blue;
	}
	
	public boolean notWhite() {
		return !(alpha == red && red == green && green == blue && blue == 255);
	}
	
	public Pixel hideLSB(byte halfByte) {
		if (halfByte > 0xF || halfByte < 0) throw new IllegalArgumentException("Invalid half byte : "+halfByte);
		String binary = "";
		for (int i=0; i<4; i++) {
			int bit = BitsUtil.getBit(halfByte, i);
			binary = bit+binary;
			switch (i) {
				case 0: alpha = BitsUtil.toggleBit(alpha, bit, 0); break;
				case 1: red = BitsUtil.toggleBit(red, bit, 0); break;
				case 2: green = BitsUtil.toggleBit(green, bit, 0); break;
				default: blue = BitsUtil.toggleBit(blue, bit, 0); break;
			}
		}
//		System.out.println(binary+" "+halfByte);
		return this;
	}
	
	public byte unhideLSB() {
		int halfByte = 0, bit;
		for (int i=0; i<4; i++) {
			switch (i) {
				case 0: bit = BitsUtil.getBit(alpha, 0); break;
				case 1: bit = BitsUtil.getBit(red, 0); break;
				case 2: bit = BitsUtil.getBit(green, 0); break;
				default: bit = BitsUtil.getBit(blue, 0); break;
			}
			halfByte = (byte) BitsUtil.toggleBit(halfByte, bit, i);
		}
		return (byte) halfByte;
	}
	
	public Pixel hideAlpha(int aByte) {
		if (alpha != 255)
			alpha = 255;
		alpha -= aByte;
		return this;
	}
	
	public byte unhide() {
		int aByte = 255 - alpha;
		alpha = 255;
		return (byte) (aByte & 0xFF);
	}
	
	public int toARGB() {
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}
	
	public boolean equals(Pixel p) {
		return alpha == p.alpha && red == p.red && green == p.green && blue == p.blue; 
	}
	
	public String toString() {
		return Arrays.toString(getComponents());
	}
	
	public static void main(String[] args) {
		Pixel p = new Pixel(Color.DARK_GRAY);
		byte b = (byte) 0;
		while (true) {
			System.out.println(p.hideLSB(b).unhideLSB() == b);
			System.out.println(b++);
		}
	}
}