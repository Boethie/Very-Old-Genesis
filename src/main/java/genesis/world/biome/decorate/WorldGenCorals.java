package genesis.world.biome.decorate;

import genesis.combo.variant.EnumCoral;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;
import genesis.util.random.i.IntRange;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenCorals extends WorldGenDecorationBase
{
	private static final IntRange DEFAULT = IntRange.create(1, 4);
	private static final IntRange HORIZONTAL = IntRange.create(1);
	
	private final IBlockState coralState;
	private final int minHeight;
	private final int maxHeight;
	
	private boolean horizontal;
	
	public WorldGenCorals(int minHeight, int maxHeight, EnumCoral coralType)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER, WorldBlockMatcher.TRUE);
		
		coralState = GenesisBlocks.coral.getBlockState(coralType);
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		
		setPatchRadius(3);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		horizontal = rand.nextBoolean();
		setPatchCount(horizontal ? HORIZONTAL : DEFAULT);
		
		return super.generate(world, rand, pos);
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		pos = pos.down();
		int height = minHeight + rand.nextInt(maxHeight);
		
		if (horizontal)
			return placeCoralFormationHorizontal(world, pos, rand, height);
		
		return placeCoralFormation(world, pos, rand, height);
	}
	
	private boolean placeCoralFormation(World world, BlockPos pos, Random random, int height)
	{
		if (!isPositionSuitable(world, pos, height))
			return false;
		
		placeCoralColumn(world, pos, height);
		
		for (EnumFacing side : EnumFacing.HORIZONTALS)
		{
			if (random.nextInt(2) == 0)
				placeCoralColumn(world, pos.offset(side), 1 + random.nextInt(height - 1));
		}
		
		return true;
	}
	
	private void placeCoralColumn(World world, BlockPos pos, int height)
	{
		for (int i = 0; i < height; ++i)
			setBlock(world, pos.add(0, i, 0), coralState);
	}
	
	private boolean placeCoralFormationHorizontal(World world, BlockPos pos, Random rand, int length)
	{
		if (!isPositionSuitable(world, pos, 5))
			return false;
		
		boolean placed = false;
		
		for (EnumFacing direction : EnumFacing.HORIZONTALS)
		{
			if (rand.nextInt(10) < 7)
			{
				placeCoralBranch(world, pos, rand, length, direction);
				placed = true;
			}
		}
		
		return placed;
	}
	
	private void placeCoralBranch(World world, BlockPos pos, Random random, int length, EnumFacing direction)
	{
		BlockPos placePos = pos;
		boolean placeTop = true;
		
		for (int i = 1; i <= length; ++i)
		{
			IBlockState state = world.getBlockState(placePos);
			
			if (state.getBlock() != Blocks.dirt
					&& state.getBlock() != Blocks.clay
					&& state.getBlock() != GenesisBlocks.ooze
					&& state != coralState)
				break;
			
			setBlock(world, placePos, coralState);
			setBlock(world, placePos.north(), coralState);
			setBlock(world, placePos.south(), coralState);
			setBlock(world, placePos.east(), coralState);
			setBlock(world, placePos.west(), coralState);
			
			if (placeTop)
				setBlock(world, placePos.up(), coralState);
			
			if (random.nextInt(3) == 0)
				placeTop = false;
			
			placePos = placePos.offset(direction);
		}
	}
	
	protected boolean isPositionSuitable(World world, BlockPos pos, int height)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (state.getMaterial() == Material.water
				|| state.getBlock().isAir(state, world, pos)
				|| state == coralState)
			return false;
		
		for (int i = 1; i <= height + 2; ++i)
		{
			if (world.getBlockState(pos.add(0, i, 0)).getBlock() != Blocks.water)
			{
				return false;
			}
		}
		
		return true;
	}
}
