package nova.gui.wrapper.mc.forge.v1_8.launch;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.loader.Loadable;
import nova.core.loader.Mod;
import nova.core.wrapper.mc.forge.v18.launcher.NovaMinecraft;
import nova.gui.wrapper.mc.forge.v1_8.MCGuiFactory;
import nova.gui.wrapper.mc.forge.v1_8.dependency.GuiModule;

/**
 * The main class to initialize the Gui Plugin
 *
 * @author Calclavia
 */
@Mod(id = "nova-gui-wrapper", name = "NOVA-GUI", version = "0.0.1", modules = { GuiModule.class }, novaVersion = "0.1.0")
public class NovaGuiWrapper implements Loadable {

	@SideOnly(Side.CLIENT)
	@Override
	public void preInit() {
		NetworkRegistry.INSTANCE.registerGuiHandler(NovaMinecraft.instance, new MCGuiFactory.GuiHandler());
	}
}
