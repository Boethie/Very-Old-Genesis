package genesis.world.biome.decorate;

import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.common.GenesisBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenRoots extends WorldGenDecorationBase
{
	public WorldGenRoots()
	{
		setPatchCount(8);
	}
	
	@Override
	public boolean place(World world, Random random, BlockPos pos)
	{
		//pos = new BlockPos(pos.getX(), 80, pos.getZ());
		
		do
		{
			IBlockState state = world.getBlockState(pos);
			
			if (state.getMaterial() == Material.ground)
			{
				BlockPos below = pos.down();
				state = world.getBlockState(below);
				
				if (state.getBlock().isAir(state, world, below))
				{
					pos = below;
					break;
				}
			}
		}
		while ((pos = pos.down()).getY() > 55);
		
		int length = 1 + random.nextInt(2);
		
		if (!isMatchInCylinder(world, pos, (s, w, p) -> GenesisBlocks.trees.isStateOf(s, TreeBlocksAndItems.LOG), 2, length, length + 8))
			return false;
		
		for (int i = 0; i < length; ++i)
		{
			BlockPos rootPos = pos.down(i);
			IBlockState replacing = world.getBlockState(rootPos);
			
			if (replacing.getBlock().isAir(replacing, world, rootPos))
				setAirBlock(world, rootPos, GenesisBlocks.roots.getDefaultState());
			else
				return false;
		}
		
		return true;
	}
}
