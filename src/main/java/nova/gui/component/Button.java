package nova.gui.component;

import nova.gui.ComponentEvent;
import nova.gui.GuiComponent;
import nova.gui.GuiEvent;
import nova.gui.nativeimpl.NativeButton;

public class Button extends GuiComponent<Button, NativeButton> {

	public Button(String uniqueID, final String text) {
		super(uniqueID, NativeButton.class);
		onGuiEvent(this::onMousePressed, GuiEvent.MouseEvent.class);
		getNative().setText(text);
	}

	public Button(final String text) {
		this("", text);
	}

	public String getText() {
		return getNative().getText();
	}

	public Button setText(String text) {
		getNative().setText(text);
		return this;
	}

	private void onMousePressed(GuiEvent.MouseEvent event) {
		switch (event.state) {
			case DOWN:
				// TODO Handle overlapping components etc, check if the
				// component is visible at the given position.
				if (isMouseOver()) {
					triggerEvent(new ComponentEvent.ActionEvent(this));
				}
			default:
				break;
		}
	}
}
