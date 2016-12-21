package nova.gui.component.inventory;

import nova.core.component.inventory.Inventory;
import nova.core.component.inventory.InventoryException;
import nova.core.component.inventory.ItemFilter;
import nova.core.item.Item;
import nova.gui.Gui;
import nova.gui.GuiComponent;
import nova.gui.GuiEvent;
import nova.gui.nativeimpl.NativeSlot;

import java.util.Optional;

/**
 * A slot is a {@link GuiComponent} that can hold {@link Item Items}.
 *
 * @author Vic Nightfall
 */
public class Slot extends GuiComponent<Slot, NativeSlot> {

	private final String inventoryID;
	private final int slotID;
	protected Inventory inventory;
	private Optional<ItemFilter> filter = Optional.empty();
	private boolean readonly;

	/**
	 * Creates a new Slot instance. The inventory id specifies which
	 * {@link Inventory} the slot will apply to, has to be specified on the
	 * parent GUI with the {@link GuiEvent.BindEvent} and
	 * {@link Gui#addInventory(String, Inventory)}
	 *
	 * @param uniqueID
	 * @param inventoryID
	 * @param slotID
	 */
	public Slot(String uniqueID, String inventoryID, int slotID) {
		super(uniqueID, NativeSlot.class);
		this.inventoryID = inventoryID;
		this.slotID = slotID;

		onGuiEvent(this::onBind, GuiEvent.BindEvent.class);
	}

	/**
	 * Creates a new Slot instance. The inventory id specifies which
	 * {@link Inventory} the slot will apply to, has to be specified on the
	 * parent GUI with the {@link GuiEvent.BindEvent} and
	 * {@link Gui#addInventory(String, Inventory)}
	 *
	 * @param inventoryID
	 * @param slotID
	 */
	public Slot(String inventoryID, int slotID) {
		this("", inventoryID, slotID);
	}

	public Optional<ItemFilter> getFilter() {
		return filter;
	}

	/**
	 * Sets the {@link ItemFilter} of this slot. May be null.
	 *
	 * @param filter
	 * @return this
	 */
	public Slot setFilter(ItemFilter filter) {
		this.filter = Optional.ofNullable(filter);
		return this;
	}

	public boolean isReadOnly() {
		return readonly;
	}

	/**
	 * Sets weather this slot should be accessible by players.
	 *
	 * @param readonly
	 * @return readonly flag
	 */
	public Slot setReadOnly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	public Optional<Item> getItem() {
		if (inventory == null)
			return Optional.empty();
		return inventory.get(slotID);
	}

	public Optional<Item> removeItem(int amount) {
		if (inventory == null)
			return Optional.empty();
		return inventory.remove(slotID, amount);
	}

	public boolean accept(Item item) {
		return !filter.isPresent() || filter.get().test(item);
	}

	public boolean setItem(Item item) {
		if (inventory == null || !accept(item))
			return false;
		return inventory.set(slotID, item);
	}

	public int addItem(Item item) {
		if (inventory == null || !accept(item))
			return item.count();
		return inventory.add(slotID, item);
	}

	public Inventory getInventory() {
		return inventory;
	}

	public int getSlotID() {
		return slotID;
	}

	protected void onBind(GuiEvent.BindEvent event) {
		inventory = event.gui.getInventory(inventoryID);
		if (inventory == null) {
			throw new InventoryException("Unsupplied inventory \"" + inventoryID + "\" for Slot " + getID());
		}
	}
}
