package nova.gui;

public class GuiComponentException extends GuiException {
	private static final long serialVersionUID = 2017_01_22L;

	public GuiComponentException() {
		super();
	}

	public GuiComponentException(String message, Object... parameters) {
		super(message, parameters);
	}

	public GuiComponentException(String message) {
		super(message);
	}

	public GuiComponentException(String message, Throwable cause) {
		super(message, cause);
	}

	public GuiComponentException(Throwable cause) {
		super(cause);
	}
}
