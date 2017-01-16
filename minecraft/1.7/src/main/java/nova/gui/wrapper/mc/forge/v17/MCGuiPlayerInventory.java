package nova.gui.wrapper.mc.forge.v17;

import net.minecraft.inventory.IInventory;
import nova.core.wrapper.mc.forge.v17.wrapper.inventory.BWInventory;
import nova.gui.component.inventory.PlayerInventory;
import nova.gui.nativeimpl.NativePlayerInventory;
import nova.gui.render.Graphics;
import nova.gui.wrapper.mc.forge.v17.MCGui.MCContainer;
import nova.gui.wrapper.mc.forge.v17.MCGui.MCGuiScreen;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MCGuiPlayerInventory extends MCGuiComponent<PlayerInventory> implements NativePlayerInventory {

	List<MCGuiSlot.MCSlot> slots = new ArrayList<>();

	public MCGuiPlayerInventory(PlayerInventory component) {
		super(component);
	}

	@Override
	public Optional<Vector2D> getPreferredSize() {
		return Optional.of(new Vector2D(162, 80));
	}

	@Override
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics) {
		MCCanvas canvas = getCanvas();
		int x = canvas.getState().txi();
		int y = canvas.getState().tyi();

		MCGuiScreen gui = getGui().getGuiScreen();
		for (MCGuiSlot.MCSlot slot : slots) {
			slot.reset();
			MCGuiSlot.drawSlot(gui, slot, mouseX, mouseY, !getComponent().getBackground().isPresent());
			slot.setPosition(x, y, gui);
		}
		super.draw(mouseX, mouseY, partial, graphics);
	}

	@Override
	public void onAddedToContainer(MCContainer container) {
		IInventory inventory = ((BWInventory) getComponent().getInventory()).wrapped;
		slots.clear();

		for (int j = 0; j < 3; j++) {
			for (int k = 0; k < 9; k++) {
				slots.add(new MCGuiSlot.MCSlot(inventory, k + j * 9 + 9, k * 18, j * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			slots.add(new MCGuiSlot.MCSlot(inventory, i, i * 18, 62));
		}

		slots.forEach(container::addSlotToContainer);
	}
}
