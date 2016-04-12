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
		super(WorldBlockMatcher.STANDARD_AIR_WATER, WorldBlockMatcher.state(replacePredicate));
		
		this.states = ImmutableList.copyOf(states);
		
		setPatchRadius(3);
		setPatchCount(64);
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		if (states.size() == 0)
			return false;
		
		return setBlockInWorld(world, pos.down(), states.get(rand.nextInt(states.size())), true);
	}
}
