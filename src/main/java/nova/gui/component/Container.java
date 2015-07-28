package nova.gui.component;

import nova.gui.AbstractGuiContainer;
import nova.gui.nativeimpl.NativeContainer;

public class Container extends AbstractGuiContainer<Container, NativeContainer> {

	public Container(String uniqueID) {
		super(uniqueID, NativeContainer.class);
	}

	public Container() {
		super(NativeContainer.class);
	}
}
