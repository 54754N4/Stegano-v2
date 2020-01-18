# Stegano
A java project to compile multiple stegano techniques

# Supported
Audio formats:
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
audio.LSBTranscoder 		Transcodes in LSB of PCM bytes
# Image
image.AlphaTranscoder 		Transcodes in image alpha channel
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
Then create an ImageStegano (IS) instance passing it the technique to use for encoding and decoding. In this case AlphaTranscoder is used to hide using the transparency bit (alpha color channel) of each pixel. A power of 2 (<= 8) has to be specified to subdivide the bytes that will be hidden (e.g. subdivision of 2 means that every byte will be cut into 4 packets of 2 bits).
```java
int subdivision = 2;
String separator = "#";  // used to separate metadata and payload
ImageStegano is = new ImageStegano(new AlphaTranscoder(separator, subdivision));
File carrier = is.hide(payload, image, "newImage.png","png"); 
System.out.println("Carrier="+carrier.getName());			
```
After that you can extract the results from the carrier file by passing it to an IS instance with the appropriate metafier to decode the data. ParsedResults contains much more info on the extracted data.
Note: Different IS instances can be used for encoding and decoding separately if and only if both instances have been created using the same separator and subdivision bits.
```java
ParsedResults extracted = is.extractFile(carrier);  // extracts data from carrier file
System.out.println("Hidden="+extracted.file.getName()); 
 ```
Working running example should print something along those lines :
```
Pre-Infusion SHA256=3CF8E9498F9FFEFCA5A1111C5F8388C5045EFEA1B68C69AD8AFF75AC37AFF4E3
Subdividing 336087 into 1344348 bytes
Total bytes/pixels = 1344348/6948864
Image width/height/ratio = 6144/1131/5.432361
Size is valid, Encoding data..
Affected 1344348/6948864 pixels.
Encoded data, saving image..
Carrier=newImage.png
Packing 348 into 87 bytes
Subdivisions=2
Format=txt
Header Checksum=3CF8E9498F9FFEFCA5A1111C5F8388C5045EFEA1B68C69AD8AFF75AC37AFF4E3
Bytes length=336000
Packing 1344000 into 336000 bytes
Post-Extraction SHA256=3CF8E9498F9FFEFCA5A1111C5F8388C5045EFEA1B68C69AD8AFF75AC37AFF4E3
Hidden=extracted.txt
```
 

# placeholder

placeholder
