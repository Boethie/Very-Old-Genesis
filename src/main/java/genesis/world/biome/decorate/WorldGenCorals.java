package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumCoral;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenCorals extends WorldGenDecorationBase
{
	private EnumCoral coralType;
	private int minHeight;
	private int maxHeight;
	
	public WorldGenCorals(int minHeight, int maxHeight, EnumCoral coralType)
	{
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.coralType = coralType;
	}
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isLeaves(world, pos) && !block.isLeaves(world, pos))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		if (coralType == null)
			coralType = EnumCoral.FAVOSITES;
		
		int height = minHeight + random.nextInt(maxHeight);
		
		IBlockState coralBlock = GenesisBlocks.corals.getBlockState(coralType);
		
		if (!placeCoralFormation(world, pos, random, coralBlock, height))
			return false;
		
		BlockPos additionalPos;
		
		for (int i = 0; i < random.nextInt(5); ++i)
		{
			additionalPos = pos.add(random.nextInt(7) - 3, random.nextInt(3) - 1, random.nextInt(7) - 3);
			placeCoralFormation(world, additionalPos, random, coralBlock, height);
		}
		
		return true;
	}
	
	private boolean placeCoralFormation(World world, BlockPos pos, Random random, IBlockState coralBlock, int height)
	{
		Block block = world.getBlockState(pos).getBlock();
		
		if (
				block == Blocks.water
				|| block.isAir(world, pos))
			return false;
		
		for (int i = 1; i <= height + 2; ++i)
		{
			if (world.getBlockState(pos.add(0, i, 0)).getBlock() != Blocks.water)
			{
				return false;
			}
		}
		
		placeCoralColumn(world, pos, coralBlock, height);
		
		if (random.nextInt(2) == 0)
			placeCoralColumn(world, pos.north(), coralBlock, 1 + random.nextInt(height - 1));
		if (random.nextInt(2) == 0)
			placeCoralColumn(world, pos.south(), coralBlock, 1 + random.nextInt(height - 1));
		if (random.nextInt(2) == 0)
			placeCoralColumn(world, pos.east(), coralBlock, 1 + random.nextInt(height - 1));
		if (random.nextInt(2) == 0)
			placeCoralColumn(world, pos.west(), coralBlock, 1 + random.nextInt(height - 1));
		
		return true;
	}
	
	private void placeCoralColumn(World world, BlockPos pos, IBlockState coralBlock, int height)
	{
		for (int i = 0; i < height; ++i)
			setBlockInWorld(world, pos.add(0, i, 0), coralBlock, true);
	}
}
