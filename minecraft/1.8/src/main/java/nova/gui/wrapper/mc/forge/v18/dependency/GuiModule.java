package nova.gui.wrapper.mc.forge.v18.dependency;

import nova.gui.factory.GuiComponentFactory;
import nova.gui.factory.GuiManager;
import nova.gui.wrapper.mc.forge.v18.MCGuiComponentFactory;
import nova.gui.wrapper.mc.forge.v18.MCGuiFactory;
import se.jbee.inject.bind.BinderModule;

public class GuiModule extends BinderModule {
	@Override
	protected void declare() {
		bind(GuiComponentFactory.class).to(MCGuiComponentFactory.class);
		bind(GuiManager.class).to(MCGuiFactory.class);
	}
}
