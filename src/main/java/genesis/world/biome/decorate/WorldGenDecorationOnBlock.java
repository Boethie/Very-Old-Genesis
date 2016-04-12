package genesis.world.biome.decorate;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import genesis.util.WorldBlockMatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDecorationOnBlock extends WorldGenDecorationBase
{
	protected final Predicate<IBlockState> groundPredicate;
	protected final List<IBlockState> states;
	
	public WorldGenDecorationOnBlock(Predicate<IBlockState> groundPredicate, IBlockState... states)
	{
		super(WorldBlockMatcher.STANDARD_AIR, WorldBlockMatcher.state(groundPredicate));
		
		this.groundPredicate = groundPredicate;
		this.states = ImmutableList.copyOf(states);
	}
	
	@Override
	public boolean place(World world, Random random, BlockPos pos)
	{
		return setBlockInWorld(world, pos, states.get(random.nextInt(states.size())));
	}
}
