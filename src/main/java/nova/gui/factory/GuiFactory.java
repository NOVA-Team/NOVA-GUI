package nova.gui.factory;

import nova.core.util.Factory;
import nova.gui.Gui;

import java.util.function.Function;
import java.util.function.Supplier;

public class GuiFactory extends Factory<Gui> {

	public GuiFactory(Function<Object[], Gui> constructor) {
		super(constructor);
	}

	public GuiFactory(Supplier<Gui> supplier) {
		super(o -> supplier.get());
	}

	public Gui makeGUI(Object... args) {
		return constructor.apply(args);
	}
}
