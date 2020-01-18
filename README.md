# Stegano
A java project to compile multiple stegano techniques

# Supported Formats
Currently supported audio formats:
> Tested: WAV

> Untested: AIFF, AU, MIDI Type 0, MIDI Type 1 and RMF 

Image formats:
> Tested: PNG

> Untested: TIFF, GIF, BMP

Text formats:
> Tested:

> Untested:

# Transcoders
```markdown
# Audio
	audio.LSBTranscoder: 		Transcodes in LSB of PCM bytes
# Image
	image.AlphaTranscoder: 		Transcodes in image alpha channel
	image.LSBTranscoder 		Transcodes in LSB of a single colour channel
	image.BatchLSBTranscoder* 	Transcodes in LSB of all colour channels (x4 storage)
# Text
```
*Still in debugging phase
# Images 
First get a handle to the payload and carrier files, a copy will be made from the carrier.
```java
File image = new File("bigimagepf.bmp"),    // image to hide into
    payload = new File("aFile.txt");        // file to hide 
```
Then create an ImageStegano (IS) instance passing it the technique to use for encoding and decoding. In this case AlphaTranscoder is used to hide using the transparency bit (alpha color channel) of each pixel.
```java
ImageStegano is = new ImageStegano(new AlphaMetafier("#"));		// separator used for metadata + payload 
File carrier = is.hide(payload, image, "newImage.png","png"); 
System.out.println("Carrier="+carrier.getName());			
```
After that you can extract the results from the carrier file by passing it to an IS instance with the appropriate metafier to decode the data. ParsedResults contains much more info on the extracted data.
```java
ParsedResults extracted = is.extractFile(carrier);  // extracts data from carrier file
System.out.println("Hidden="+extracted.file.getName()); 
 ```
Working running example should print something along those lines :
```
Pre-Infusion SHA256=3CF8E9498F9FFEFCA5A1111C5F8388C5045EFEA1B68C69AD8AFF75AC37AFF4E3
Encoded 672174 bytes
Total pixels/bytes = 6948864/672174
Image width/height/ratio = 6144/1131/5.432361
Size is valid, Encoding data..
Affected 672174/6948864 pixels.
Encoded data, saving image..
Carrier=newImage.png
Packing 174 into 87 bytes
Packing 672000 into 336000 bytes
Post-Extraction SHA256=3CF8E9498F9FFEFCA5A1111C5F8388C5045EFEA1B68C69AD8AFF75AC37AFF4E3
Hidden=extracted.txt
```
 

# placeholder

placeholder
