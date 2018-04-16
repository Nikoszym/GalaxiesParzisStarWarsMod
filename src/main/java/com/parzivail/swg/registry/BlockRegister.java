package com.parzivail.swg.registry;

import com.parzivail.swg.Resources;
import com.parzivail.swg.block.BlockBlasterWorkbench;
import com.parzivail.swg.block.BlockEndorLog;
import com.parzivail.swg.block.BlockFastGrass;
import com.parzivail.util.block.PBlock;
import com.parzivail.util.block.PBlockContainer;
import com.parzivail.util.block.PBlockEnviromap;
import com.parzivail.util.block.PBlockLog;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by colby on 12/26/2017.
 */
public class BlockRegister
{
	public static PBlock fastGrass;

	public static PBlock tatooineSand;
	public static PBlock tatooineSandstone;

	public static PBlock oxidizedSand;
	public static PBlock oxidizedSandStone;
	public static PBlock paleStone;

	public static PBlockLog endorLog;

	public static PBlock labWall;
	public static PBlockEnviromap dynamicTest;

	public static PBlockContainer blasterWorkbench;

	public static void register()
	{
		register(fastGrass = new BlockFastGrass());

		register(tatooineSand = new PBlock("tatooineSand"));
		register(tatooineSandstone = new PBlock("tatooineSandstone"));

		register(oxidizedSand = new PBlock("oxidizedSand"));
		register(oxidizedSandStone = new PBlock("oxidizedSandStone"));
		register(paleStone = new PBlock("paleStone"));

		register(endorLog = new BlockEndorLog());

		register(labWall = new PBlock("labWall"));
		//		register(dynamicTest = new PBlockDynamic("dynamicTest", new String[] {
		//				Resources.modColon("yavinBricksClean"),
		//				Resources.modColon("yavinBricksRough"),
		//				Resources.modColon("yavinBricksCracked"),
		//				Resources.modColon("yavinBricksCrackedMossy"),
		//		}));
		//register(dynamicTest = new PBlockEnviromap("dynamicTest", Resources.modColon("large/yavinrock"), 7, 3));

		register(blasterWorkbench = new BlockBlasterWorkbench().setAlpha());
	}

	private static void register(PBlock item)
	{
		GameRegistry.registerBlock(item, item.name);
	}

	private static void register(PBlockLog item)
	{
		GameRegistry.registerBlock(item, item.name);
	}

	private static void register(PBlockContainer item)
	{
		GameRegistry.registerBlock(item, item.name);
		GameRegistry.registerTileEntity(item.createNewTileEntity(null, 0).getClass(), Resources.tileDot(item.name));
	}
}
