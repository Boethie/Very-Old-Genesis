package genesis.world.biome.decorate;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import genesis.util.WorldBlockMatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenPatch extends WorldGenDecorationBase
{
	protected final List<IBlockState> states;
	
	public WorldGenPatch(Predicate<IBlockState> replacePredicate, IBlockState... states)
	{
		super(WorldBlockMatcher.REPLACEABLE_LEAVES, WorldBlockMatcher.state(replacePredicate));
		
		this.states = ImmutableList.copyOf(states);
	}
	
	@Override
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		boolean generated = false;
		
		if (setBlock(world, pos, random))
			generated = true;
		
		for (int i = 0; i < 64; ++i)
			if (setBlock(world, pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3), random))
				generated = true;
		
		return generated;
	}
	
	private boolean setBlock(World world, BlockPos pos, Random rand)
	{
		if (states.size() == 0)
			return false;
		
		if (!groundMatcher.apply(world, pos))
			return false;
		
		setBlockInWorld(world, pos, states.get(rand.nextInt(states.size())), true);
		
		return true;
	}
}
