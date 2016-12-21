package nova.gui;

import nova.core.util.exception.NovaException;

public class GuiException extends NovaException {
	private static final long serialVersionUID = 2017_01_22L;

	public GuiException() {
		super();
	}

	public GuiException(String message, Object... parameters) {
		super(message, parameters);
	}

	public GuiException(String message) {
		super(message);
	}

	public GuiException(String message, Throwable cause) {
		super(message, cause);
	}

	public GuiException(Throwable cause) {
		super(cause);
	}
}
