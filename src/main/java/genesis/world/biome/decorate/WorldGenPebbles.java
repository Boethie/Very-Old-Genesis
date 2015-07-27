package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenPebbles extends WorldGenDecorationBase
{
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = getPosition(world, pos);
		
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		boolean water_exists = findBlockInRange(world, pos, Blocks.water.getDefaultState(), 3, 2, 3);
		
		if (!water_exists)
			return false;
		
		//setBlockInWorld(world, pos.up(), GenesisItems.tools.getBlockState(, ToolObjectType.));
		
		return true;
	}
}
