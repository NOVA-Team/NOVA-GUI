package nova.gui.factory;

import nova.core.util.registry.Factory;
import nova.gui.Gui;

import java.util.function.Function;
import java.util.function.Supplier;

public class GuiFactory extends Factory<GuiFactory, Gui> {

	public GuiFactory(String id, Supplier<Gui> constructor) {
		super(id, constructor);
	}

	public GuiFactory(String id, Supplier<Gui> constructor, Function<Gui, Gui> processor) {
		super(id, constructor, processor);
	}

	@Override
	protected GuiFactory selfConstructor(String id, Supplier<Gui> constructor, Function<Gui, Gui> processor) {
		return new GuiFactory(id, constructor, processor);
	}
}
