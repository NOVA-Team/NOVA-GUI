package nova.core.gui.dep;

import nova.core.gui.factory.GuiComponentFactory;
import nova.core.gui.factory.GuiManager;
import se.jbee.inject.bind.BinderModule;

public class GuiModule extends BinderModule {

	@Override
	protected void declare() {
		require(GuiComponentFactory.class);
		require(GuiManager.class);
	}

}
