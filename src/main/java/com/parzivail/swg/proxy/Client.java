package com.parzivail.swg.proxy;

import com.parzivail.pm3d.Pm3dModelLoader;
import com.parzivail.swg.Resources;
import com.parzivail.swg.StarWarsGalaxy;
import com.parzivail.swg.animation.HyperspaceEnter;
import com.parzivail.swg.entity.EntityBlasterBolt;
import com.parzivail.swg.entity.EntityShip;
import com.parzivail.swg.entity.ship.EntityT65B;
import com.parzivail.swg.item.ItemLightsaber;
import com.parzivail.swg.network.client.MessageItemLeftClick;
import com.parzivail.swg.register.ItemRegister;
import com.parzivail.swg.register.KeybindRegister;
import com.parzivail.swg.register.Pr3ModelRegister;
import com.parzivail.swg.render.RenderBlasterBolt;
import com.parzivail.swg.render.RenderShip;
import com.parzivail.swg.render.item.RenderItemLightsaber;
import com.parzivail.swg.render.util.DummyBuiltinLoader;
import com.parzivail.util.animation.Sequencer;
import com.parzivail.util.item.ILeftClickInterceptor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.IOException;

public class Client extends Common
{
	public static int leftClickDelayTimer;
	public static Minecraft mc;
	public static boolean autoRelevelEnabled = true;

	public static Sequencer animHyperspaceTest = new Sequencer(new HyperspaceEnter());
	public static boolean xwingDebug = false;

	public static Entity prevCamEntity = null;

	/**
	 * Queries the "attack" keybind and processes items who consume the associated action
	 *
	 * @param passive False if the event was triggered by a keybind or mouse click, true if it was triggered by the tick handler
	 */
	public static void checkLeftClickPressed(boolean passive)
	{
		if (leftClickDelayTimer > 0)
			return;

		ItemStack heldItem = mc.player.getHeldItemMainhand();
		if (!(heldItem.getItem() instanceof ILeftClickInterceptor))
			return;
		ILeftClickInterceptor item = ((ILeftClickInterceptor)heldItem.getItem());

		boolean risingEdge = KeybindRegister.keyAttack.interceptedIsPressed();
		boolean holding = KeybindRegister.keyAttack.getInterceptedIsKeyDown();

		boolean pressed = item.isLeftClickRepeatable(heldItem, mc.player.world, mc.player) ? (passive && (risingEdge || holding)) : risingEdge;

		if (item.isLeftClickRepeatable(heldItem, mc.player.world, mc.player))
			while (KeybindRegister.keyAttack.interceptedIsPressed())
				;

		if (pressed)
		{
			leftClickDelayTimer = 2;
			item.onItemLeftClick(heldItem, mc.player.world, mc.player);
			StarWarsGalaxy.NETWORK.sendToServer(new MessageItemLeftClick(mc.player));
		}
	}

	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		mc = Minecraft.getMinecraft();

		ModelLoaderRegistry.registerLoader(new Pm3dModelLoader());
		ModelLoaderRegistry.registerLoader(new DummyBuiltinLoader());

		RenderingRegistry.registerEntityRenderingHandler(EntityT65B.class, renderManagerIn -> new RenderShip(renderManagerIn, Pr3ModelRegister.XwingT65b));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlasterBolt.class, RenderBlasterBolt::new);

		try
		{
			Pr3ModelRegister.load();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void postInit(FMLPostInitializationEvent e)
	{
		super.postInit(e);
		KeybindRegister.register();
	}

	@Override
	public void registerItemRenderer(Item item, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Resources.modColon(id), "inventory"));
	}

	@Override
	public void onRegisterItem(RegistryEvent.Register<Item> event)
	{
		for (ItemLightsaber lightsaber : ItemRegister.lightsabers)
			setItemRenderer(lightsaber, new RenderItemLightsaber());
	}

	public void setItemRenderer(Item item, TileEntityItemStackRenderer renderer)
	{
		item.setTileEntityItemStackRenderer(renderer);
		registerItemRenderer(item, "builtin");
	}

	public void captureShipInput(EntityPlayer pilot, EntityShip entityShip)
	{
		MouseHelper mouseHelper = mc.mouseHelper;
		float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
		float f1 = f * f * f * 8.0F;
		ShipInputMode shipInputMode = entityShip.getShipInputMode();
		entityShip.setInputsClient(mouseHelper.deltaX * f1, mouseHelper.deltaY * f1, shipInputMode, mc.gameSettings.keyBindJump.isKeyDown());
	}

	public MovementInput getMovementInput(EntityPlayer player)
	{
		if (player instanceof EntityPlayerSP)
			return ((EntityPlayerSP)player).movementInput;
		return null;
	}

	@Override
	public void notifyPlayer(ITextComponent message, boolean actionBar)
	{
		mc.player.sendStatusMessage(message, actionBar);
	}
}
