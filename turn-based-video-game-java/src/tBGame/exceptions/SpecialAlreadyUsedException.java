package tBGame.exceptions;

public class SpecialAlreadyUsedException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SpecialAlreadyUsedException() {
		super("Special is already used!");
	}
	public SpecialAlreadyUsedException(String message) {
		super(message);
	}
}
