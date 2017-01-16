package nova.gui.wrapper.mc.forge.v18;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import nova.core.entity.Entity;
import nova.core.wrapper.mc.forge.v18.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v18.wrapper.entity.backward.BWEntity;
import nova.gui.Gui;
import nova.gui.GuiException;
import nova.gui.factory.GuiFactory;
import nova.gui.factory.GuiManager;
import nova.gui.wrapper.mc.forge.v18.MCGui.MCContainer;
import nova.gui.wrapper.mc.forge.v18.MCGui.MCGuiScreen;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MCGuiFactory extends GuiManager {

	private static Optional<Gui> guiToOpen = Optional.empty();
	private static List<GuiFactory> idMappedFactories = new ArrayList<>();

	public MCGuiFactory() {
		super(Game.events());
	}

	@Override
	public GuiFactory register(GuiFactory factory) {
		idMappedFactories.add(factory);
		return super.register(factory);
	}

	@Override
	public void showGui(String identifier, Entity entity, Vector3D pos) {
		GuiFactory factory = registry.get(identifier).orElseThrow(() -> new GuiException(String.format("No GUI called %s registered!", identifier)));
		Gui gui = factory.build();
		showGui(gui, entity, pos, idMappedFactories.indexOf(factory));
	}

	@Override
	protected void showGui(Gui gui, Entity entity, Vector3D pos) {
		showGui(gui, entity, pos, -1);
	}

	private void showGui(Gui gui, Entity entity, Vector3D pos, int id) {
		BWEntity player = (BWEntity) entity;
		guiToOpen = Optional.of(gui);
		if (player.entity.worldObj.isRemote != gui.hasServerSide()) {
			((EntityPlayer) player.entity).openGui(NovaMinecraft.id, id, ((EntityPlayer) player.entity).getEntityWorld(), (int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
		}
	}

	@Override
	protected void closeGui(Gui gui) {
		if (gui.hasServerSide()) {
			Minecraft.getMinecraft().thePlayer.closeScreen();
		} else {
			//TODO Does this work?
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
	}

	@Override
	public Optional<Gui> getActiveGuiImpl() {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if (gui instanceof MCGuiScreen) {
			return Optional.of(((MCGuiScreen) gui).getGui().getComponent());
		}
		return Optional.empty();
	}

	@Override
	public Optional<Gui> getActiveGuiImpl(Entity player) {
		BWEntity entityPlayer = (BWEntity) player;
		Container container = ((EntityPlayer) entityPlayer.entity).openContainer;
		if (container instanceof MCContainer) {
			return Optional.of(((MCContainer) container).getGui().getComponent());
		}
		return Optional.empty();
	}

	public static class GuiHandler implements IGuiHandler {

		@Override
		public Container getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			if (guiToOpen.isPresent()) {
				Gui gui = guiToOpen.get();
				guiToOpen = Optional.empty();
				gui.bind(new BWEntity(player), new Vector3D(x, y, z));
				return ((MCGui) gui.getNative()).newContainer();
			}
			return null;
		}

		@Override
		public GuiContainer getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			Gui gui;
			if (guiToOpen.isPresent()) {
				// Check if the client side GUI instance was set withPriority showGui on
				// the client
				gui = guiToOpen.get();
				guiToOpen = Optional.empty();
			} else {
				// Try to get the client side GUI from the id mapping
				gui = idMappedFactories.get(id).build();
			}
			if (gui == null) {
				throw new GuiException("Couldn't get client side instance for the provided GUI of id " + id + " !");
			}

			gui.bind(new BWEntity(player), new Vector3D(x, y, z));

			MCGui nativeGui = (MCGui) gui.getNative();
			nativeGui.newContainer();
			return nativeGui.getGuiScreen();
		}
	}
}
