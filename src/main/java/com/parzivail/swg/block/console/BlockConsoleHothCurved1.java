package com.parzivail.swg.block.console;

import com.parzivail.swg.Resources;
import com.parzivail.swg.StarWarsGalaxy;
import com.parzivail.swg.tile.console.TileConsoleHoth1;
import com.parzivail.util.block.HarvestLevel;
import com.parzivail.util.block.PBlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockConsoleHothCurved1 extends PBlockContainer
{
	public BlockConsoleHothCurved1()
	{
		super("blockConsoleHoth1", Material.iron);
		setCreativeTab(StarWarsGalaxy.tab);
		setBlockBounds(0, 0, 0, 1, 2.5f, 1);
		setHardness(50.0F);
		this.setHarvestLevel("pickaxe", HarvestLevel.IRON);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileConsoleHoth1)
		{
			TileConsoleHoth1 te = (TileConsoleHoth1)tile;
			int l = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
			te.setFacing(l);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileConsoleHoth1();
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public void registerIcons(IIconRegister icon)
	{
		blockIcon = icon.registerIcon(Resources.MODID + ":" + "blank");
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
}