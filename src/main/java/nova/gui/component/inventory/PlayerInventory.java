package nova.gui.component.inventory;

import nova.core.inventory.component.InventoryPlayer;
import nova.gui.GuiComponent;
import nova.gui.GuiEvent;
import nova.gui.nativeimpl.NativePlayerInventory;

/**
 * Defines the standard player inventory. It automatically gets wrapped to the
 * player inventory and its appearance, size, and other properties might differ
 * from wrapper to wrapper.
 * 
 * @author Vic Nightfall
 */
public class PlayerInventory extends GuiComponent<PlayerInventory, NativePlayerInventory> {

	protected InventoryPlayer playerInventory;

	public PlayerInventory(String uniqueID) {
		super(uniqueID, NativePlayerInventory.class);

		onGuiEvent(this::onBind, GuiEvent.BindEvent.class);
	}

	public PlayerInventory() {
		this("");
	}

	public void onBind(GuiEvent.BindEvent event) {
		playerInventory = event.gui.getPlayerInventory();
	}

	public InventoryPlayer getInventory() {
		return playerInventory;
	}
}
