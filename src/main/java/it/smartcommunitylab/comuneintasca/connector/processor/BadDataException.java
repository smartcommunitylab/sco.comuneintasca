package it.smartcommunitylab.comuneintasca.connector.processor;


public class BadDataException extends Exception {
	private static final long serialVersionUID = 2170335864064397429L;

	public BadDataException() {
	}

	public BadDataException(String message) {
		super(message);
	}

	public BadDataException(Throwable cause) {
		super(cause);
	}

	public BadDataException(String message, Throwable cause) {
		super(message, cause);
	}

}
