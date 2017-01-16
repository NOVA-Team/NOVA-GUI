package nova.gui.wrapper.mc.forge.v17;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import nova.gui.GuiEvent.MouseEvent;
import nova.gui.GuiEvent.MouseEvent.EnumMouseState;
import nova.gui.Outline;
import nova.gui.component.Button;
import nova.gui.nativeimpl.NativeButton;
import nova.gui.render.Graphics;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public class MCButton extends MCGuiComponent<Button> implements NativeButton, DrawableGuiComponent {

	@SideOnly(Side.CLIENT)
	private MCGuiButton button;

	public MCButton(Button component) {
		super(component);

		if (FMLCommonHandler.instance().getSide().isClient()) {
			button = new MCGuiButton();
		}

		component.onGuiEvent(this::onMousePressed, MouseEvent.class);
	}

	@Override
	public void setOutline(Outline outline) {
		button.width = outline.getWidth();
		button.height = outline.getHeight();
		button.xPosition = outline.minXi();
		button.yPosition = outline.minYi();
		super.setOutline(outline);
	}

	@Override
	public String getText() {
		return button.displayString;
	}

	@Override
	public void setText(String text) {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			button.displayString = text;
		}
	}

	@Override
	public Optional<Vector2D> getMinimumSize() {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		return fontRenderer != null ? Optional.of(new Vector2D(fontRenderer.getStringWidth(button.displayString) + 10, fontRenderer.FONT_HEIGHT + 10)) : Optional.empty();
	}

	@Override
	public boolean isPressed() {
		// TODO
		return false;
	}

	@Override
	public void setPressed(boolean isPressed) {
		// TODO
	}

	@Override
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics) {
		button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		super.draw(mouseX, mouseY, partial, graphics);
	}

	public void onMousePressed(MouseEvent event) {
		if (event.state == EnumMouseState.DOWN) {
			if (getComponent().isMouseOver()) {
				button.func_146113_a(Minecraft.getMinecraft().getSoundHandler());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public class MCGuiButton extends GuiButtonExt {

		public MCGuiButton() {
			super(0, 0, 0, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			GL11.glTranslatef(-xPosition, -yPosition, 0);
			super.drawButton(mc, mouseX + xPosition, mouseY + yPosition);
			GL11.glTranslatef(xPosition, yPosition, 0);
		}
	}
}
