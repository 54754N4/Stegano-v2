package image;

import java.awt.Color;
import java.util.Arrays;

import model.Metafier;

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
			System.out.println("WTF");
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
	
	public Pixel hideLSB(int halfByte) {
		for (int i=0; i<4; i++) {
			int bit = Metafier.getBit(halfByte, i);
			switch (i) {
				case 0: alpha = toggleLSB(alpha, bit, 0); break;
				case 1: red = toggleLSB(red, bit, 0); break;
				case 2: green = toggleLSB(green, bit, 0); break;
				default: blue = toggleLSB(blue, bit, 0); break;
			}
		}
		return this;
	}
	
	public int unhideLSB() {
		int halfByte = 0, bit;
		for (int i=0; i<4; i++) {
			switch (i) {
				case 0: bit = Metafier.getBit(alpha, 0); break;
				case 1: bit = Metafier.getBit(red, 0); break;
				case 2: bit = Metafier.getBit(green, 0); break;
				default: bit = Metafier.getBit(blue, 0); break;
			}
			halfByte = toggleLSB(halfByte, bit, i);
		}
		return halfByte;
	}
	
	private int toggleLSB(int aByte, int bit, int offset) {
		return (bit == 1) ? 
			Metafier.setBit(aByte, offset) : Metafier.clearBit(aByte, offset);
	}
	
	public Pixel hideAlpha(int aByte) {
		if (alpha != 255)
			alpha = 255;
		alpha -= aByte;
		return this;
	}
	
	public int unhide() {
		int aByte = 255 - alpha;
		alpha = 255;
		return aByte;
	}
	
	public int toARGB() {
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}
	
	public boolean equals(Pixel p) {
		return alpha == p.alpha && red == p.red && green == p.green && blue == p.blue; 
	}
	
	public String toString() {
		return Arrays.toString(new int[]{red, green, blue, alpha});
	}
	
	public static void main(String[] args) {
		Pixel p = new Pixel(Color.DARK_GRAY);
		byte b = (byte) 0xF;
		while (b>=0)
			System.out.println(p.hideLSB(b--).unhideLSB());
	}
}