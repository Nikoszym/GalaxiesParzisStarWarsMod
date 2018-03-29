package com.parzivail.swg.item;

import com.parzivail.swg.Resources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.UUID;

public class PItem extends Item
{
	/**
	 * Movement bonus for wearing Pegasus Boots
	 */
	private static final UUID precisionMovementSlowdownUUID = UUID.fromString("65a1759e-43ea-476f-85d1-717fc2a573f7");
	private static final AttributeModifier precisionMovementSlowdown = (new AttributeModifier(precisionMovementSlowdownUUID, "Precise Movement", -0.5D, 2)).setSaved(false);

	public final String name;
	private final String[] variants;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public PItem(String name)
	{
		this(name, new String[0]);
	}

	public PItem(String name, String[] variants)
	{
		this.name = name;
		this.variants = variants;
		this.setHasSubtypes(variants.length != 0);
		this.setTextureName(Resources.modColon(this.name));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		if (this.variants.length == 0)
			return Resources.itemDot(this.name);
		return Resources.itemDot(this.name, this.variants[stack.getItemDamage()]);
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldRequestRenderState(ItemStack stack, World world, EntityPlayer player)
	{
		return false;
	}

	public boolean shouldUsePrecisionMovement(ItemStack stack, World world, EntityPlayer player)
	{
		return false;
	}

	public float getZoomLevel(ItemStack stack, World world, EntityPlayer player)
	{
		return 1;
	}

	public static void applyPrecisionMovement(EntityPlayer player, boolean apply)
	{
		IAttributeInstance movement = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

		if (apply)
		{
			if (movement.getModifier(precisionMovementSlowdownUUID) == null)
				movement.applyModifier(precisionMovementSlowdown);
		}
		else
		{
			if (movement.getModifier(precisionMovementSlowdownUUID) != null)
				movement.removeModifier(precisionMovementSlowdown);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		if (this.variants.length == 0)
		{
			super.registerIcons(iconRegister);
			return;
		}

		this.icons = new IIcon[this.variants.length];
		for (int i = 0; i < this.icons.length; i++)
			this.icons[i] = iconRegister.registerIcon(Resources.MODID + ":" + this.name + "_" + variants[i]);
	}
}