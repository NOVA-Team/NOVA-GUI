package nova.gui.wrapper.mc.forge.v1_11;

import nova.gui.AbstractGuiContainer;
import nova.gui.GuiComponent;
import nova.gui.Outline;
import nova.gui.nativeimpl.NativeContainer;
import nova.gui.render.Canvas;
import nova.gui.render.Graphics;
import nova.gui.wrapper.mc.forge.v1_11.MCGui.MCContainer;

import java.util.ArrayList;
import java.util.List;

public class MCGuiContainer extends MCGuiComponent<AbstractGuiContainer<?, ?>> implements NativeContainer {

	protected List<GuiComponent<?, ?>> components = new ArrayList<>();

	public MCGuiContainer(AbstractGuiContainer<?, ?> component) {
		super(component);
	}

	@Override
	public void addElement(GuiComponent<?, ?> element) {
		components.add(element);
	}

	@Override
	public void removeElement(GuiComponent<?, ?> element) {
		components.remove(element);
	}

	@Override
	public void onAddedToContainer(MCContainer container) {
		for (GuiComponent<?, ?> component : components) {
			((DrawableGuiComponent) component.getNative()).onAddedToContainer(container);
		}
	}

	// TODO This should be part of NovaCore -> Move to NativeContainer interface
	@Override
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics) {
		for (GuiComponent<?, ?> component : components) {
			Outline outline = component.getOutline();
			int width = outline.minXi(), height = outline.minYi();
			Canvas canvas = graphics.getCanvas();

			canvas.push();
			canvas.setScissor(component.getOutline());
			canvas.translate(width, height);
			((DrawableGuiComponent) component.getNative()).draw(mouseX - width, mouseY - height, partial, graphics);
			canvas.pop();
		}
		super.draw(mouseX, mouseY, partial, graphics);
	}
}
