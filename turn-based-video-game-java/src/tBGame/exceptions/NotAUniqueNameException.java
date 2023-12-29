package tBGame.exceptions;

public class NotAUniqueNameException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotAUniqueNameException() {
		super("Not a unique name");
	}
	public NotAUniqueNameException(String message) {
		super(message);
	}
}

