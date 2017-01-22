package nova.gui.wrapper.mc.forge.v1_8;

import nova.gui.nativeimpl.NativeGuiComponent;
import nova.gui.render.Graphics;

public interface DrawableGuiComponent extends NativeGuiComponent {

	public void draw(int mouseX, int mouseY, float partial, Graphics graphics);

	public default void onAddedToContainer(MCGui.MCContainer container) {

	}

	public default MCCanvas getCanvas() {
		return getGui().getCanvas();
	}

	public default MCGui getGui() {
		return (MCGui) getComponent().getParentGui().get().getNative();
	}
}
