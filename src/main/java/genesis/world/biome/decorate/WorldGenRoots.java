package genesis.world.biome.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.common.GenesisBlocks;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
		pos = new BlockPos(pos.getX(), 80, pos.getZ());
		
		List<IBlockState> allowedBlocks = new ArrayList<>();
		
		allowedBlocks.add(Blocks.dirt.getDefaultState());
		allowedBlocks.add(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
		allowedBlocks.add(GenesisBlocks.moss.getDefaultState());	// TODO: This should be less hardcoded.
		
		do
		{
			IBlockState state = world.getBlockState(pos);
			
			if (allowedBlocks.contains(state))
			{
				state = world.getBlockState(pos.down());
				
				if (state.getBlock().isAir(state, world, pos.down()))
					break;
			}
		}
		while ((pos = pos.down()).getY() > 55);
		
		boolean generated = false;
		
		int radius = 4;
		int depth = 2;
		int length = 1 + random.nextInt(2);
		
		if (!isMatchInCylinder(world, pos, (s, w, p) -> GenesisBlocks.trees.isStateOf(s, TreeBlocksAndItems.LOG), radius, 0, depth))
			return false;
		
		for (int i = 0; i < length; ++i)
		{
			BlockPos rootPos = pos.down(i + 1);
			IBlockState replacing = world.getBlockState(rootPos);
			
			if (replacing.getBlock().isAir(replacing, world, rootPos))
			{
				setAirBlock(world, rootPos, GenesisBlocks.roots.getDefaultState());
				generated = true;
			}
			else
			{
				break;
			}
		}
		
		return generated;
	}
}
