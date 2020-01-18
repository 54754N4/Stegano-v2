package error.file;

public class InvalidChecksumException extends Exception {
	private static final long serialVersionUID = 1102928047377311510L;

	public InvalidChecksumException(String msg) {
		super("Extracted file hash is different than one inside header.\n"+msg);
	}
}
