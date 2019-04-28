package com.parzivail.swg.proxy;

import com.parzivail.swg.Resources;
import com.parzivail.swg.entity.EntityShip;
import com.parzivail.swg.render.RenderShip;
import com.parzivail.util.jsonpipeline.BlockbenchModelLoader;
import com.parzivail.util.jsonpipeline.BlockbenchWeightedModelLoader;
import com.parzivail.util.jsonpipeline.ModelLocationInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class SwgClientProxy extends SwgProxy
{
	public static Minecraft mc;

	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		mc = Minecraft.getMinecraft();
		ModelLoaderRegistry.registerLoader(new BlockbenchModelLoader(modelLocation -> (Resources.MODID.equals(modelLocation.getResourceDomain()) && !(modelLocation instanceof ModelResourceLocation))));
		ModelLoaderRegistry.registerLoader(new BlockbenchWeightedModelLoader(modelLocation -> (Resources.MODID.equals(modelLocation.getResourceDomain()) && modelLocation instanceof ModelResourceLocation)));

		RenderingRegistry.registerEntityRenderingHandler(EntityShip.class, RenderShip::new);
	}

	@Override
	public void init(FMLInitializationEvent e)
	{
		super.init(e);
		ModelLocationInformation.init(mc.getBlockRendererDispatcher().getBlockModelShapes().getBlockStateMapper());
	}

	@Override
	public void registerItemRenderer(Item item, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Resources.modColon(id), "inventory"));
	}

	public MovementInput getMovementInput(EntityPlayer player)
	{
		if (player instanceof EntityPlayerSP)
			return ((EntityPlayerSP)player).movementInput;
		return null;
	}
}