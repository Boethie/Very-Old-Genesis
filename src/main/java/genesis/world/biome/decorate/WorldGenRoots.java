package genesis.world.biome.decorate;

import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.common.GenesisBlocks;
import genesis.util.WorldUtils;
import genesis.util.math.PosVecIterable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
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
		for (BlockPos checkPos : new PosVecIterable(pos, EnumFacing.DOWN))
		{
			IBlockState state = world.getBlockState(checkPos);
			
			if (state.getMaterial() == Material.GROUND)
			{
				BlockPos below = checkPos.down();
				state = world.getBlockState(below);
				
				if (state.getBlock().isAir(state, world, below))
				{
					pos = below;
					break;
				}
			}
			
			if (checkPos.getY() < 55)
				return false;
		}
		
		int length = 1 + random.nextInt(2);
		
		if (!WorldUtils.isMatchInCylinder(world, pos, (s, w, p) -> GenesisBlocks.TREES.isStateOf(s, TreeBlocksAndItems.LOG), 4, length, length + 2))
			return false;
		
		for (int i = 0; i < length; ++i)
		{
			BlockPos rootPos = pos.down(i);
			
			if (!setAirBlock(world, rootPos, GenesisBlocks.ROOTS.getDefaultState()))
				return false;
		}
		
		return true;
	}
}
