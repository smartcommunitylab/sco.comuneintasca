package it.smartcommunitylab.comuneintasca.connector.processor;


public class MissingDataException extends Exception {
	private static final long serialVersionUID = 3312527089132198381L;

	public MissingDataException() {
	}

	public MissingDataException(String message) {
		super(message);
	}

	public MissingDataException(Throwable cause) {
		super(cause);
	}

	public MissingDataException(String message, Throwable cause) {
		super(message, cause);
	}

}
