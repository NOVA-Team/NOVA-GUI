package nova.gui.wrapper.mc.forge.v18;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.network.Packet;
import nova.core.wrapper.mc.forge.v18.network.netty.MCNetworkManager;
import nova.gui.Gui;
import nova.gui.GuiComponent;
import nova.gui.GuiEvent.MouseEvent.EnumMouseButton;
import nova.gui.Outline;
import nova.gui.nativeimpl.NativeGui;
import nova.gui.render.Canvas;
import nova.gui.render.Graphics;
import nova.gui.render.text.TextMetrics;
import nova.gui.wrapper.mc.forge.v18.text.MCTextRenderer;
import nova.internal.core.Game;
import nova.gui.wrapper.mc.forge.v18.network.PacketGui;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Optional;

/**
 * Minecraft implementation of the NOVA GUI System
 *
 * @author Vic Nightfall
 */
public class MCGui extends MCGuiContainer implements NativeGui, DrawableGuiComponent {

	private static final Container fakeContainer = new Container() {
		@Override
		public boolean canInteractWith(EntityPlayer player) {
			return false;
		}
	};
	private final Gui component;
	private Outline outline = Outline.empty;
	private Graphics graphics;
	private MCTextRenderer textRenderer;
	@SideOnly(Side.CLIENT)
	private MCGuiScreen guiScreen;
	private MCContainer container;

	public MCGui(Gui component) {
		super(component);
		this.component = component;

		if (FMLCommonHandler.instance().getSide().isClient()) {
			guiScreen = new MCGuiScreen();
		}
	}

	@SideOnly(Side.CLIENT)
	public MCGuiScreen getGuiScreen() {
		return guiScreen;
	}

	public MCContainer newContainer() {
		container = new MCContainer();
		if (FMLCommonHandler.instance().getSide().isClient()) {
			guiScreen.inventorySlots = container;
		}
		for (GuiComponent<?, ?> component : components) {
			((DrawableGuiComponent) component.getNative()).onAddedToContainer(container);
		}
		return container;
	}

	@Override
	public MCCanvas getCanvas() {
		return (MCCanvas) graphics.getCanvas();
	}

	@Override
	public TextMetrics getTextMetrics() {
		return textRenderer;
	}

	@Override
	public Gui getComponent() {
		return component;
	}

	@Override
	public Outline getOutline() {
		return outline;
	}

	@Override
	public void setOutline(Outline outline) {
		this.outline = outline;
	}

	@Override
	public void requestRender() {
		// Not needed as it gets redrawn every frame
	}

	@Override
	public void dispatchNetworkEvent(Packet packet) {
		// TODO I think the NetworkManager should be able to do this
		MCNetworkManager manager = (MCNetworkManager) Game.network();
		manager.sendToServer(new PacketGui(packet));
	}

	@Override
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics) {

		Canvas canvas = graphics.getCanvas();

		Optional<Vector2D> preferredSize = getComponent().getPreferredSize();
		if (preferredSize.isPresent()) {
			// We have a preferred size so we can draw our fancy gray
			// background.

			Vector2D size = getOutline().getDimension();
			Vector2D position = getOutline().getPosition();
			GuiUtils.drawGUIWindow((int) position.getX() - 4, (int) position.getY() - 4, (int) size.getX() + 8, (int) size.getY() + 8);
		}

		Outline guiOutline = getOutline();
		canvas.translate(guiOutline.minXi(), guiOutline.minYi());
		super.draw(mouseX - guiOutline.minXi(), mouseY - guiOutline.minYi(), partial, graphics);
		canvas.translate(-guiOutline.minXi(), -guiOutline.minYi());
	}

	public class MCContainer extends Container {

		@Override
		public boolean canInteractWith(EntityPlayer player) {
			return true;
		}

		public MCGui getGui() {
			return MCGui.this;
		}

		@Override
		public void onContainerClosed(EntityPlayer player) {
			super.onContainerClosed(player);
			getGui().component.unbind();
		}

		@Override
		public Slot addSlotToContainer(Slot slot) {
			return super.addSlotToContainer(slot);
		}

		@Override
		public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
			// TODO Nothing here
			return null;
		}
	}

	@SideOnly(Side.CLIENT)
	public class MCGuiScreen extends GuiContainer {

		public MCGuiScreen() {
			super(MCGui.this.container);
		}

		// TODO bridge, might want to use the AT for this?
		public int guiLeft() {
			return guiLeft;
		}

		public int guiTop() {
			return guiTop;
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partial) {
			Container container = inventorySlots;
			// Replace container instance withPriority fake container in order to stop
			// slot rendering
			inventorySlots = fakeContainer;
			super.drawScreen(mouseX, mouseY, partial);
			// Back to where it belongs
			inventorySlots = container;
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partial, int mouseX, int mouseY) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glPushMatrix();
			MCGui.this.draw(mouseX, mouseY, partial, graphics);
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		@Override
		protected void mouseClicked(int mouseX, int mouseY, int button) {
			onMousePressed(mouseX - getOutline().minXi(), mouseY - getOutline().minYi(), getMouseButton(button), true);

			try {
				super.mouseClicked(mouseX, mouseY, button);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void mouseClickMove(int mouseX, int mouseY, int button, long time) {
			onMousePressed(mouseX - getOutline().minXi(), mouseY - getOutline().minYi(), getMouseButton(button), false);
			super.mouseClickMove(mouseX, mouseY, button, time);
		}

		private EnumMouseButton getMouseButton(int button) {
			switch (button) {
				case 0:
				default:
					return EnumMouseButton.LEFT;
				case 1:
					return EnumMouseButton.RIGHT;
				case 2:
					return EnumMouseButton.MIDDLE;
			}
		}

		@Override
		public void handleKeyboardInput() {
			boolean state = Keyboard.getEventKeyState();
			int key = Keyboard.getEventKey();
			char ch = Keyboard.getEventCharacter();
			onKeyPressed(Game.input().getKey(key), ch, state);
			if (state) {
				try {
					keyTyped(ch, key);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			this.mc.dispatchKeypresses();
		}

		@Override
		public boolean doesGuiPauseGame() {
			return false;
		}

		@Override
		public void setWorldAndResolution(Minecraft mc, int width, int height) {
			ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

			MCCanvas canvas = new MCCanvas(width, height, Tessellator.getInstance(), scaledresolution.getScaleFactor());
			if (textRenderer == null) {
				textRenderer = new MCTextRenderer(fontRendererObj, canvas);
			}

			textRenderer.setCanvas(canvas);
			graphics = new Graphics(canvas, textRenderer);

			boolean resized = width != outline.getWidth() || height != outline.getHeight();
			Outline oldOutline = outline;
			outline = new Outline(0, 0, width, height);

			if (resized) {
				Optional<Vector2D> preferredSize = getComponent().getPreferredSize();
				if (preferredSize.isPresent()) {
					// Set the size to the preferred size and center the GUI
					Vector2D size = preferredSize.get();
					int xOffset = (int) (width / 2 - size.getX() / 2);
					int yOffset = (int) (height / 2 - size.getY() / 2);
					setOutline(getOutline().setPosition(new Vector2D(xOffset, yOffset)).setDimension(size));
				}
				onResized(oldOutline);
			}

			xSize = outline.getWidth();
			ySize = outline.getHeight();
			super.setWorldAndResolution(mc, width, height);
		}

		public MCGui getGui() {
			return MCGui.this;
		}
	}
}
