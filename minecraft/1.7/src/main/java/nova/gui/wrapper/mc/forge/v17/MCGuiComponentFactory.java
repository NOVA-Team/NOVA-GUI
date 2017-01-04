package nova.gui.wrapper.mc.forge.v17;

import nova.gui.AbstractGuiContainer;
import nova.gui.Gui;
import nova.gui.GuiComponent;
import nova.gui.component.Button;
import nova.gui.component.inventory.PlayerInventory;
import nova.gui.component.inventory.Slot;
import nova.gui.factory.GuiComponentFactory;
import nova.gui.nativeimpl.NativeButton;
import nova.gui.nativeimpl.NativeContainer;
import nova.gui.nativeimpl.NativeGui;
import nova.gui.nativeimpl.NativeGuiComponent;
import nova.gui.nativeimpl.NativePlayerInventory;
import nova.gui.nativeimpl.NativeSlot;

public class MCGuiComponentFactory extends GuiComponentFactory {

	public MCGuiComponentFactory() {
		registerNativeComponent(NativeGui.class,
			component -> new MCGui((Gui) component));
		registerNativeComponent(NativeContainer.class,
			component -> new MCGuiContainer((AbstractGuiContainer<?, ?>) component));
		registerNativeComponent(NativeButton.class,
			component -> new MCButton((Button) component));
		registerNativeComponent(NativeGuiComponent.class,
			component -> new MCGuiComponent<GuiComponent<?, ?>>(component));
		registerNativeComponent(NativeSlot.class,
			component -> new MCGuiSlot((Slot) component));
		registerNativeComponent(NativePlayerInventory.class,
			component -> new MCGuiPlayerInventory((PlayerInventory) component));
	}
}
