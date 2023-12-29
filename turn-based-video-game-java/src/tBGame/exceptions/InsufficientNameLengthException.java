package tBGame.exceptions;

public class InsufficientNameLengthException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InsufficientNameLengthException() {
		super("Name length is insufficent.");
	}
	public InsufficientNameLengthException(String message) {
		super(message);
	}

}
