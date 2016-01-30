package genesis.world.biome.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenDecorationOnBlock
	extends WorldGenDecorationBase
{
	List<IBlockState> onState = new ArrayList<IBlockState>();
	List<IBlockState> state = new ArrayList<IBlockState>();
	
	public WorldGenDecorationOnBlock(IBlockState... states)
	{
		for (int i = 0; i < states.length; ++i)
		{
			state.add(states[i]);
		}
	}
	
	public WorldGenDecorationOnBlock setBaseBlocks(IBlockState... states)
	{
		for (int i = 0; i < states.length; ++i)
		{
			onState.add(states[i]);
		}
		
		return this;
	}
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		IBlockState curState;
		
		do
		{
			curState = world.getBlockState(pos);
			if (onState.contains(curState))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos.up()))
			return false;
		
		world.setBlockState(pos.up(), state.get(random.nextInt(state.size())));
		
		return true;
	}
}
