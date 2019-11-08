package image;

import java.awt.Color;
import java.util.Arrays;

import image.errors.ImageAlreadyTransparentException;

class Pixel {
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
	
	public boolean notWhite() {
		return !(alpha == red && red == green && green == blue && blue == 255);
	}
	
	public Pixel hide(int aByte) throws ImageAlreadyTransparentException {
		if (alpha != 255)
			throw new ImageAlreadyTransparentException();
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
}