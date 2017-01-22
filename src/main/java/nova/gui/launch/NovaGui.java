package nova.gui.launch;

import nova.core.game.InputManager;
import nova.core.loader.Mod;
import nova.core.network.NetworkManager;
import nova.core.util.registry.LanguageManager;
import nova.gui.factory.GuiComponentFactory;

/**
 * @author Calclavia
 */
@Mod(id = "nova-gui", name = "NOVA-GUI", version = "0.0.1", novaVersion = "0.1.0")
public class NovaGui {
	public static NovaGui instance;
	public final InputManager input;
	public final GuiComponentFactory guiComponentFactory;
	public final NetworkManager network;
	public final LanguageManager language;

	public NovaGui(InputManager input, GuiComponentFactory guiComponentFactory, NetworkManager network, LanguageManager language) {
		instance = this;
		this.input = input;
		this.guiComponentFactory = guiComponentFactory;
		this.network = network;
		this.language = language;
	}
}
