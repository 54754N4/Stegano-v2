package error.image;

public class SmallResolutionException extends ImageResolutionException {
	private static final long serialVersionUID = 3909303422102504812L;

	public SmallResolutionException(int bytes, int pixels) {
		super("Not enough pixels. "+bytes+" payload bytes < "+pixels+" pixels!");
	}
}