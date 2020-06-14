package error.text;

public class NotEnoughTextException extends Exception {
	private static final long serialVersionUID = 3909303422102504812L;

	public NotEnoughTextException(int bytes, long size) {
		super("Not enough text. "+bytes+" payload bytes < "+size+" spaces!");
	}
}