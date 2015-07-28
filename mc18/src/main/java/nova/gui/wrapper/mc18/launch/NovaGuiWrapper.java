package nova.gui.wrapper.mc18.launch;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import nova.gui.wrapper.mc18.MCGuiFactory;
import nova.gui.wrapper.mc18.dependency.GuiModule;
import nova.wrapper.mc18.launcher.NovaMinecraft;

/**
 * The main class to initialize the Gui Plugin
 *
 * @author Calclavia
 */
@NovaMod(id = "nova-gui", name = "NOVA-GUI", version = "0.0.1", modules = { GuiModule.class }, novaVersion = "0.1.0")
public class NovaGuiWrapper implements Loadable {

	@SideOnly(Side.CLIENT)
	@Override
	public void preInit() {
		NetworkRegistry.INSTANCE.registerGuiHandler(NovaMinecraft.instance, new MCGuiFactory.GuiHandler());
	}
}
