package model.text;

import java.io.File;

import model.Stegano;

public class TextStegano extends Stegano {
	private LineTranscoder metafier;
	
	public TextStegano(LineTranscoder metafier) {
		super(metafier);
		this.metafier = metafier;
	}

	@Override
	public File hide(File payload, File carrier, String name, String format) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
