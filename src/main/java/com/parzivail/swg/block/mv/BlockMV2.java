package com.parzivail.swg.block.mv;

import com.parzivail.swg.Resources;
import com.parzivail.swg.StarWarsGalaxy;
import com.parzivail.swg.tile.mv.TileMV2;
import com.parzivail.util.block.HarvestLevel;
import com.parzivail.util.block.PBlockContainer;
import com.parzivail.util.block.TileEntityRotate;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMV2 extends PBlockContainer
{
	public BlockMV2()
	{
		super("moistureVaporator2", Material.iron);
		setCreativeTab(StarWarsGalaxy.tab);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F);
		setHardness(50.0F);
		this.setHarvestLevel("pickaxe", HarvestLevel.IRON);
	}

	//	@Override
	//	public void breakBlock(World world, int x, int y, int z, Block block, int wut)
	//	{
	//		TileMV2 moistureVap = (TileMV2)world.getTileEntity(x, y, z);
	//		if (moistureVap != null)
	//		{
	//			ItemStack itemstack = moistureVap.getStackInSlot(0);
	//			if (itemstack != null)
	//			{
	//				EntityItem entityitem = new EntityItem(world, x, y, z, itemstack);
	//				if (itemstack.hasTagCompound())
	//					entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
	//				world.spawnEntityInWorld(entityitem);
	//			}
	//			world.func_147453_f(x, y, z, block);
	//		}
	//		super.breakBlock(world, x, y, z, block, wut);
	//	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileMV2();
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

	//	@Override
	//	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float e, float f, float g)
	//	{
	//		if (world.isRemote)
	//			player.openGui(StarWarsMod.instance, Resources.GUI_MV, world, x, y, z);
	//		return true;
	//	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityRotate)
		{
			TileEntityRotate vap = (TileEntityRotate)tile;
			int l = MathHelper.floor_double(player.rotationYaw * 8.0F / 360.0F + 0.5D) & 0x3;
			vap.setFacing(l);
		}
	}

	@Override
	public void registerIcons(IIconRegister icon)
	{
		blockIcon = icon.registerIcon(Resources.MODID + ":" + "iconBlank");
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess block, int x, int y, int z)
	{
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F);
	}
}