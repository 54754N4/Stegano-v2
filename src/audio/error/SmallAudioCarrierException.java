package audio.error;

import java.io.IOException;

public class SmallAudioCarrierException extends IOException {
	private static final long serialVersionUID = -7381893892194734761L;
	
	public SmallAudioCarrierException(int bytes, int rawPCM) {
		super("Not enough bytes. "+bytes+" payload bytes < "+rawPCM+" raw PCM data!");
	}
}
