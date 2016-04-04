package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;
import genesis.util.WorldBlockMatcher;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenPrototaxites extends WorldGenDecorationBase
{
	private IBlockState baseBlock = GenesisBlocks.prototaxites_mycelium.getDefaultState();
	private IBlockState bodyBlock = GenesisBlocks.prototaxites.getDefaultState();
	
	public WorldGenPrototaxites()
	{
		super(WorldBlockMatcher.AIR, WorldBlockMatcher.TRUE);
	}
	
	@Override
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		boolean placedSome = false;
		
		if (placeMycelliumBase(world, pos, random))
			placedSome = true;
		
		if (placedSome)
		{
			placeMycelliumBase(world, pos.north(), random);
			placeMycelliumBase(world, pos.south(), random);
			placeMycelliumBase(world, pos.east(), random);
			placeMycelliumBase(world, pos.west(), random);
			
			int size = 1 + random.nextInt(5);
			
			for (int i = 1; i <= size; ++i)
			{
				setBlockInWorld(world, pos.up(i), bodyBlock, true);
			}
		}
		
		return placedSome;
	}
	
	private boolean placeMycelliumBase(World world, BlockPos pos, Random rand)
	{
		if (!(
				world.getBlockState(pos).getBlock() == Blocks.dirt
				|| world.getBlockState(pos).getBlock() == GenesisBlocks.moss)
			|| !(world.isAirBlock(pos.up())))
			return false;
		
		setBlockInWorld(world, pos, baseBlock, true);
		
		for (EnumFacing side : EnumFacing.HORIZONTALS)
		{
			BlockPos sidePos = pos.offset(side);
			
			if (!world.isAirBlock(sidePos) && rand.nextInt(2) == 0)
				setBlockInWorld(world, sidePos, baseBlock, true);
		}
		
		return true;
	}
}
