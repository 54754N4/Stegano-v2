package image;

import java.awt.Color;
import java.util.Arrays;

import image.errors.ImageAlreadyTransparentException;
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
		for (int i=0; i<4; i++) {
			int bit = BitsUtil.getBit(halfByte, i);
			switch (i) {
				case 0: alpha = BitsUtil.toggleBit(alpha, bit, 0); break;
				case 1: red = BitsUtil.toggleBit(red, bit, 0); break;
				case 2: green = BitsUtil.toggleBit(green, bit, 0); break;
				default: blue = BitsUtil.toggleBit(blue, bit, 0); break;
			}
		}
		return this;
	}
	
	public byte unhideLSB() {
		int[] component = getComponents();
		int halfByte = 0, bit;
		for (int i=0; i<4; i++) {
			bit = BitsUtil.getBit(component[i], 0);
			halfByte = BitsUtil.toggleBit(halfByte, bit, i);
		}
		return (byte) halfByte;
	}

	public Pixel hideLSB2(byte b) {
		for (int i=0; i<4; i++) {
			int bits = BitsUtil.getBits(b, i*2, (1+i)*2);
			switch (i) {
				case 0: alpha = BitsUtil.copy(alpha, bits, 2); break;
				case 1: red = BitsUtil.copy(red, bits, 2); break;
				case 2: green = BitsUtil.copy(green, bits, 2); break;
				default: blue = BitsUtil.copy(blue, bits, 2); break;
			}
		}
		return this;
	}
	
	public byte unhideLSB2() {
		int[] component = getComponents();
		int aByte = 0, bits;
		for (int i=0; i<4; i++) {
			bits = BitsUtil.getBits(component[i], 2);
			aByte += bits << i*2;
		}
		return (byte) aByte;
	}
	
	public Pixel hideAlpha(int aByte) throws ImageAlreadyTransparentException {
		if (alpha != 255) throw new ImageAlreadyTransparentException();
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
	
	public String toBinaryString() {
		StringBuilder sb = new StringBuilder("[");
		for (int component : getComponents()) sb.append(Integer.toBinaryString(component)+",");
		return sb.deleteCharAt(sb.length()-1).append("]").toString();
	}
	
	public String toString() {
		return Arrays.toString(getComponents());
	}
	
	public static void main(String[] args) {
		Pixel p;
		byte b = (byte) 0;
		while (++b != 0) {
			p = new Pixel(Color.WHITE);
			if (p.hideLSB2(b).unhideLSB2() != b)
				System.out.println(p.toBinaryString());
		}
	}
}