package nova.gui.factory;

import nova.core.entity.Entity;
import nova.core.event.bus.GlobalEvents;
import nova.core.loader.Mod;
import nova.core.network.NetworkTarget.IllegalSideException;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.Sided;
import nova.core.util.exception.RegistrationException;
import nova.core.util.registry.FactoryManager;
import nova.core.util.registry.Registry;
import nova.gui.Gui;
import nova.gui.GuiEvent;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class GuiManager extends FactoryManager<GuiManager, Gui, GuiFactory> {

	// TODO Move this into a seperate manager
	protected EnumMap<GuiType, List<Gui>> overlayRegistry = new EnumMap<>(GuiType.class);
	private final GlobalEvents events;

	public GuiManager(GlobalEvents events) {
		super(new Registry<>());
		this.events = events;
	}

	@Deprecated
	public void registerOverlay(Gui gui, GuiType guiType) {
		if (!overlayRegistry.containsKey(guiType)) {
			overlayRegistry.put(guiType, new ArrayList<>());
		}
		overlayRegistry.get(guiType).add(gui);
	}

	@Override
	public GuiFactory register(String id, Supplier<Gui> constructor) {
		return register(new GuiFactory(id, constructor));
	}

	public GuiFactory register(String id, Supplier<Gui> constructor, Function<Gui, Gui> processor) {
		return register(new GuiFactory(id, constructor, processor));
	}

	/**
	 * <p>
	 * Shows the provided {@link Gui} previously registered over the factory
	 * instance. It will trigger the {@link GuiEvent.BindEvent}, so any changes to the
	 * instance can be done there.
	 * </p>
	 *
	 * <p>
	 * Calling this is <i>safe</i>, you will always get a valid GUI for both
	 * sides no matter on which side you call it.
	 * </p>
	 * @param identifier Unique identifier for the GUI
	 * @param entity {@link Entity} which opened the GUI
	 * @param position The block coordinate on which to open the GUI
	 * @see #showGui(Gui, Entity, Vector3D)
	 */
	public void showGui(String identifier, Entity entity, Vector3D position) {
		GuiFactory factory = registry.get(identifier).orElseThrow(() -> new RegistrationException(String.format("No GUI called %s registered!", identifier)));
		showGui(factory.build(), entity, position);
	}

	/**
	 * <p>
	 * Shows the provided {@link Gui}. It will trigger the {@link GuiEvent.BindEvent}, so
	 * any changes to the instance can be done there.
	 * </p>
	 *
	 * <p>
	 * Calling this is <i>unsafe</i>, if you need a server side backend you
	 * <b>have</b> to call this on <i>both</i> sides on order create a matching
	 * GUI on the server side if the provided GUI wasn't registered. This should
	 * only be used for client side GUIs, the usage of
	 * {@link #showGui(String, Entity, Vector3D)} is recommended.
	 * </p>
	 * @param modID Id of the {@link Mod} that wants to show the GUI
	 * @param gui GUI to to display
	 * @param entity {@link Entity} which opened the GUI
	 * @param position The block coordinate on which to open the GUI
	 * @see #showGui(String, Entity, Vector3D)
	 */
	public void showGui(String modID, Gui gui, Entity entity, Vector3D position) {
		showGui(gui, entity, position);
	}

	protected void showGui(Gui gui, Entity entity, Vector3D position) {
		gui.bind(entity, position);
	}

	/**
	 * Closes the currently open NOVA {@link Gui} on the client side, if
	 * present, and returns to the in-game GUI. It will not affect any native
	 * GUIs that might exist along with NOVA.
	 * @throws IllegalSideException if called on the server side
	 */
	@Sided(Side.CLIENT)
	public void closeGui() {
		Side.assertSide(Side.CLIENT);
		Optional<Gui> active = getActiveGui();
		if (active.isPresent()) {
			closeGui(active.get());
		}
	}

	protected void closeGui(Gui gui) {
		gui.unbind();
	}

	/**
	 * Returns the active NOVA {@link Gui} on the client side, if present.
	 * @return NOVA {@link Gui}
	 * @throws IllegalSideException if called on the server side
	 */
	@Sided(Side.CLIENT)
	public Optional<Gui> getActiveGui() {
		Side.assertSide(Side.CLIENT);
		return getActiveGuiImpl();
	}

	protected abstract Optional<Gui> getActiveGuiImpl();

	/**
	 * Returns the active NOVA {@link Gui} of the supplied player on the client
	 * side, if present.
	 * @param player Player to check for
	 * @return NOVA {@link Gui}
	 */
	public Optional<Gui> getActiveGui(Entity player) {
		if (Side.get().isClient()) {
			return getActiveGuiImpl();
		}
		return getActiveGuiImpl(player);
	}

	protected abstract Optional<Gui> getActiveGuiImpl(Entity player);

	/**
	 * Returns the active {@link GuiType}.
	 * @return active {@link GuiType}
	 */
	public GuiType getActiveGuiType() {
		return getActiveGui().isPresent() ? GuiType.CUSTOM : GuiType.NATIVE;
	}

	@Override
	public void init() {
		this.events.publish(new Init(this));
	}

	public class Init extends ManagerEvent<GuiManager> {
		public Init(GuiManager manager) {
			super(manager);
		}
	}

	public static enum GuiType {
		INGAME, TITLE, OPTIONS, INGAME_OPTIONS, CRAFTING, NATIVE, CUSTOM
	}
}
