# Stegano
A java project to compile multiple stegano techniques

Currently: Only image steganography is done for now

# Images 

```java
// Infusion
File image = new File("bigimagepf.bmp"),    // image to hide into
    	payload = new File("aFile.txt");    // file to hide 
ImageStegano is = new ImageStegano("#");    // separator-> metadata + payload 
File carrier = is.hide(payload, image, "newImage.png","png"); 
System.out.println("Carrier="+carrier.getName());

// Extraction
ParsedResults extracted = is.extractFile(carrier);      // extracts data from carrier file
System.out.println("Hidden="+extracted.file.getName()); // Also contains other parsed results
 ```

# Whitespace

placeholder
