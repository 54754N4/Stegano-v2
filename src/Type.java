//
//
//import java.util.Arrays;
//
//import javax.imageio.ImageIO;
//
//import space.Whitespace;
//
//public enum Type {
//	SPACE(Whitespace.values()),
//	TEXT(TextFormats.values()), 
//	IMAGE(ImageFormats.values()), 
//	AUDIO(AudioFormats.values()), 
//	VIDEO(VideoFormats.values());
//	
//	private Format[] formats;
//	
//	private Type(Format[] formats) {
//		this.formats = formats;
//	}
//	
//	public Format[] supported() {
//		return formats;
//	}
//	
//	public static void main(String[] args) {
//		for (Type type : Type.values())
//			System.out.println(Arrays.toString(type.supported()));
//	}
//}
//
//interface Format {
//}
//
//enum Whitespace implements Format {
//	TXT;
//}
//
//enum TextFormats implements Format {
//	TXT, BAT, SH, DOC, LOG, RTF;
//}
//
//enum ImageFormats implements Format {
//	GIF, PNG, WBMP;		//from ImageIO.getReaderFileSuffixes())
//	//JPG doesn't support transparency
//	//BMP ImageIO doesn't have a writer for that format
//}
//
//enum AudioFormats implements Format {
//	MP3, WAV, AAC, OGG, OPUS;
//}
//
//enum VideoFormats implements Format {
//	AVI, MP4, MKV, FLV, WEBM, WMV;
//}