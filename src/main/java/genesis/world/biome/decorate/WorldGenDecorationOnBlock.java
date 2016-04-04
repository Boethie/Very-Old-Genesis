package genesis.world.biome.decorate;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDecorationOnBlock extends WorldGenDecorationBase
{
	protected final Predicate<IBlockState> groundPredicate;
	protected final List<IBlockState> states;
	
	public WorldGenDecorationOnBlock(Predicate<IBlockState> groundPredicate, IBlockState... states)
	{
		this.groundPredicate = groundPredicate;
		this.states = ImmutableList.copyOf(states);
	}
	
	@Override
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		do
		{
			if (groundPredicate.apply(world.getBlockState(pos)))
				break;
			
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		pos = pos.up();
		
		IBlockState replacing = world.getBlockState(pos);
		
		if (!replacing.getBlock().isAir(replacing, world, pos))
			return false;
		
		world.setBlockState(pos, states.get(random.nextInt(states.size())));
		
		return true;
	}
}
