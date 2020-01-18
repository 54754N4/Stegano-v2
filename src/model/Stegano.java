package model;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import error.file.NothingToExtractException;

public abstract class Stegano {
	private Metafier metafier;
	
	public Stegano(Metafier metafier) {
		this.metafier = metafier;
	}
	
	public abstract File hide(File payload, File carrier, String name, String format) throws Exception;
	
	public ParsedResults extractFile(File carrier) throws Exception {
		byte[] hidden = metafier.extractHidden(carrier);
		if (!hasHiddenData(hidden)) 
			throw new NothingToExtractException();
		return new ParsedResults(metafier, hidden);
	}
	
	// verifies if our encoded metafier chain is unpacked in the first bytes
	private boolean hasHiddenData(byte[] bytes) throws NoSuchAlgorithmException, IOException {
		byte[] topBytes = metafier.sublist(0, 400, bytes);
		return metafier.verify(topBytes) != Metafier.NOT_FOUND;
	}
}
