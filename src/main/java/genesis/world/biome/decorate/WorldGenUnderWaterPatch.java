package genesis.world.biome.decorate;

import java.util.Random;

import com.google.common.collect.ImmutableList;

import genesis.util.WorldBlockMatcher;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenUnderWaterPatch extends WorldGenDecorationBase
{
	protected final ImmutableList<IBlockState> blocks;
	private boolean setInBlock = true;
	
	public WorldGenUnderWaterPatch(IBlockState... blocks)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER, WorldBlockMatcher.TRUE);
		
		this.blocks = ImmutableList.copyOf(blocks);
	}
	
	public WorldGenUnderWaterPatch mustSetInBlock(boolean inBlock)
	{
		this.setInBlock = inBlock;
		return this;
	}
	
	@Override
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		boolean placedSome = false;
		
		if (placeSmallPatch(world, pos, random))
			placedSome = true;
		
		if (random.nextInt(2) == 0)
			if(placeSmallPatch(world, pos.add(1, 0, 1), random))
				placedSome = true;
		
		if (random.nextInt(2) == 0)
			if(placeSmallPatch(world, pos.add(1, 0, -1), random))
				placedSome = true;
		
		if (random.nextInt(2) == 0)
			if(placeSmallPatch(world, pos.add(-1, 0, 1), random))
				placedSome = true;
		
		if (random.nextInt(2) == 0)
			if(placeSmallPatch(world, pos.add(-1, 0, -1), random))
				placedSome = true;
		
		return placedSome;
	}
	
	private boolean placeSmallPatch(World world, BlockPos pos, Random random)
	{
		if (!(
				world.getBlockState(pos.up()).getMaterial() == Material.water
				&& (world.getBlockState(pos).getMaterial() != Material.water || !setInBlock)))
			return false;
		
		setBlockInWorld(world, pos, blocks.get(random.nextInt(blocks.size())), true);
		if (world.getBlockState(pos.north()).getBlock() != Blocks.water)
			setBlockInWorld(world, pos.north(), blocks.get(random.nextInt(blocks.size())), true);
		if (world.getBlockState(pos.south()).getBlock() != Blocks.water)
			setBlockInWorld(world, pos.south(), blocks.get(random.nextInt(blocks.size())), true);
		if (world.getBlockState(pos.east()).getBlock() != Blocks.water)
			setBlockInWorld(world, pos.east(), blocks.get(random.nextInt(blocks.size())), true);
		if (world.getBlockState(pos.west()).getBlock() != Blocks.water)
			setBlockInWorld(world, pos.west(), blocks.get(random.nextInt(blocks.size())), true);
		
		return true;
	}
}
