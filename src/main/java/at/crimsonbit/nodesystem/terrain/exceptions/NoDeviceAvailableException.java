package at.crimsonbit.nodesystem.terrain.exceptions;

public class NoDeviceAvailableException extends Exception {

	public NoDeviceAvailableException() {
		super();
	}

	public NoDeviceAvailableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoDeviceAvailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoDeviceAvailableException(String message) {
		super(message);
	}

	public NoDeviceAvailableException(Throwable cause) {
		super(cause);
	}

}
