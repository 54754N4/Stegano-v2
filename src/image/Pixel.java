package image;

import java.awt.Color;
import java.util.Arrays;

import image.error.ImageAlreadyTransparentException;

public class Pixel {
	public static enum Target { ALPHA, RED, GREEN, BLUE };
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
	}
	
	public int[] getComponents() {
		return new int[] {alpha, red, green, blue};
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
	
	public int get(Target t) {
		switch (t) {
			case ALPHA:	return getAlpha();
			case BLUE: return getBlue();
			case GREEN: return getGreen();
			case RED: return getRed();
			default: throw new IllegalArgumentException("Invalid target");
		}
	}
	
	public Pixel set(Target t, int value) {
		switch (t) {
			case ALPHA:	alpha = value; break;
			case BLUE: blue = value; break;
			case GREEN: green = value; break;
			case RED: red = value; break;
			default: throw new IllegalArgumentException("Invalid target");
		}
		return this;
	}
	
	public boolean notWhite() {
		return !(alpha == red && red == green && green == blue && blue == 255);
	}
	
	public Pixel hideAlpha(int aByte) throws ImageAlreadyTransparentException {
		alpha = 255 - aByte;
		return this;
	}
	
	public byte unhide() {
		int aByte = 255 - alpha;
		alpha = 255;
		return (byte) (aByte & 0xFF);
	}
	
	public int toARGB() {
		return (alpha << 24) 
			| (red << 16) 
			| (green << 8) 
			| blue;
	}
	
	public boolean equals(Pixel p) {
		return alpha == p.alpha 
			&& red == p.red 
			&& green == p.green 
			&& blue == p.blue; 
	}
	
	public String toBinaryString() {
		StringBuilder sb = new StringBuilder("[");
		for (int component : getComponents()) sb.append(Integer.toBinaryString(component)+",");
		return sb.deleteCharAt(sb.length()-1).append("]").toString();
	}
	
	public String toString() {
		return Arrays.toString(getComponents());
	}
	
//	public static void main(String[] args) {
//		Pixel p;
//		byte b = (byte) 0;
//		while (++b != 0) {
//			p = new Pixel(Color.WHITE);
//			if (p.hideLSB(b).unhideLSB() != b)
//				System.out.println(p.toBinaryString());
//		}
//	}
}