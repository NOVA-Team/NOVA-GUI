package nova.gui.nativeimpl;

import nova.core.gui.InputManager;
import nova.core.network.Packet;
import nova.gui.ComponentEvent;
import nova.gui.ComponentEvent.SidedComponentEvent;
import nova.gui.Gui;
import nova.gui.GuiEvent;
import nova.gui.Outline;
import nova.gui.factory.GuiEventFactory;
import nova.gui.render.text.TextMetrics;

public interface NativeGui extends NativeContainer {

	void dispatchNetworkEvent(Packet packet);

	TextMetrics getTextMetrics();

	/**
	 * Called when the GUI was resized and the child components need to
	 * re-validate their layout. The new size has to be set before calling this.
	 *
	 * @param oldOutline Old {@link Outline}
	 */
	default void onResized(Outline oldOutline) {
		getComponent().triggerEvent(new ComponentEvent.ResizeEvent(getComponent(), oldOutline));
	}

	default void onNetworkEvent(Packet packet) {
		Gui gui = (Gui) getComponent();
		SidedComponentEvent event = GuiEventFactory.instance.constructEvent(packet, gui);
		event.reduceTarget();
		event.component.triggerEvent(event);
	}

	default void onMousePressed(int mouseX, int mouseY, GuiEvent.MouseEvent.EnumMouseButton button, boolean state) {
		// TODO Post events for CLICK and DOUBLECLICK
		if (getComponent().isActive()) {
			getComponent().onMouseEvent(new GuiEvent.MouseEvent(mouseX, mouseY, button, state ? GuiEvent.MouseEvent.EnumMouseState.DOWN : GuiEvent.MouseEvent.EnumMouseState.UP));
		}
	}

	default void onMouseWheelTurned(int scrollAmount) {
		if (getComponent().isActive()) {
			getComponent().onEvent(new GuiEvent.MouseWheelEvent(scrollAmount));
		}
	}

	default void onKeyPressed(InputManager.Key key, char character, boolean state) {
		// TODO Post events for TYPE
		if (getComponent().isActive()) {
			getComponent().onEvent(new GuiEvent.KeyEvent(key, character, state ? GuiEvent.KeyEvent.EnumKeyState.DOWN : GuiEvent.KeyEvent.EnumKeyState.UP));
		}
	}
}
