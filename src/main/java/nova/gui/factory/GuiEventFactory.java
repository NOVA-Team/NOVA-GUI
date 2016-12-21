package nova.gui.factory;

import nova.core.event.bus.EventException;
import nova.core.network.Packet;
import nova.gui.ComponentEvent;
import nova.gui.Gui;
import nova.gui.GuiComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class GuiEventFactory {

	public static final GuiEventFactory instance = new GuiEventFactory();

	private final ArrayList<Function<GuiComponent<?, ?>, ?>> networkEvents = new ArrayList<>();
	private final HashMap<Class<?>, Integer> networkEventsReverse = new HashMap<>();

	private GuiEventFactory() {
		registerNetworkEvents();
	}

	public void registerNetworkEvents() {
		registerNetworkEvent(ComponentEvent.ActionEvent::new);
	}

	public <E extends ComponentEvent.SidedComponentEvent> void registerNetworkEvent(Function<GuiComponent<?, ?>, E> supplier) {
		networkEvents.add(supplier);
		networkEventsReverse.put(supplier.apply(null).getClass(), networkEvents.size() - 1);
	}

	@SuppressWarnings("unchecked")
	public <E extends ComponentEvent.SidedComponentEvent> E constructEvent(Packet packet, Gui parentGui) {
		int eventID = packet.readInt();
		String qualifiedName = packet.readString();
		int eventSubID = packet.readInt();

		if (eventID < 0 || eventID >= networkEvents.size())
			throw new EventException(String.format("Illegal event type %s at GUI %s", eventID, parentGui));
		if (!qualifiedName.startsWith(parentGui.getID().asString()))
			throw new EventException(String.format("Component \"%s\" does not specify an applicable qualified name for GUI \"%s\"", qualifiedName, parentGui));

		qualifiedName = qualifiedName.substring(parentGui.getID().asString().length() + 1);
		Optional<GuiComponent<?, ?>> component = parentGui.getChildElement(qualifiedName);
		if (!component.isPresent()) {
			throw new EventException(String.format("Received an event for a non-existent component \"%s\" at GUI \"%s\"", qualifiedName, parentGui));
		}

		E event = (E) networkEvents.get(eventID).apply(component.get());
		packet.setID(eventSubID);
		event.read(packet);
		return event;
	}

	public void constructPacket(ComponentEvent.SidedComponentEvent event, Gui parentGui, Packet packet, int subID) {
		if (!networkEventsReverse.containsKey(event.getClass())) {
			throw new EventException(String.format("Unknown event %s at GUI \"%s\". Register with registerNetworkEvent!", event.getClass(), packet));
		}
		packet.writeInt(networkEventsReverse.get(event.getClass()));
		packet.writeString(event.component.getQualifiedName());
		packet.writeInt(subID);
		packet.setID(subID);
		event.write(packet);
	}
}
