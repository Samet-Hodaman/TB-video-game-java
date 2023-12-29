package tBGame.exceptions;

public class InsufficentStaminaException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficentStaminaException() {
		super("Stamina is insufficent");
	}
	
	public InsufficentStaminaException(String message) {
		super(message);
	}
}
