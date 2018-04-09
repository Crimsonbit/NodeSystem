package at.crimsonbit.nodesystem.nodebackend.misc;

public class NoSuchNodeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6509586411824369320L;

	public NoSuchNodeException() {
		super();
	}

	public NoSuchNodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchNodeException(String message) {
		super(message);
	}

	public NoSuchNodeException(Throwable cause) {
		super(cause);
	}

}
