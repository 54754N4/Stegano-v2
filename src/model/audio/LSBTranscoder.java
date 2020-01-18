package model.audio;

import util.BitsUtil;

public class LSBTranscoder extends PCMTranscoder {

	public LSBTranscoder(String sep, int subdivisions) {
		super(sep, subdivisions);
	}

	@Override
	protected byte hide(byte rawPCM, byte b) throws Exception {
		return (byte) BitsUtil.copy(rawPCM, b, subdivisions, 0);
	}

	@Override
	protected byte unhide(byte rawPCM) {
		return (byte) BitsUtil.copy(0, rawPCM, subdivisions, 0);
	}

}
