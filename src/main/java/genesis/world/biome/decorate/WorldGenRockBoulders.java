package genesis.world.biome.decorate;

import genesis.combo.SiltBlocks;
import genesis.common.GenesisBlocks;
import genesis.util.WorldUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenRockBoulders extends WorldGenDecorationBase
{
	private List<IBlockState> blocks = new ArrayList<IBlockState>();
	private boolean waterRequired = true;
	private int rarity = 1;
	private int maxHeight = 5;
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = getPosition(world, pos);
		
		if (!(
				world.getBlockState(pos).getBlock() == GenesisBlocks.moss 
				|| world.getBlockState(pos).getBlock() == Blocks.dirt
				|| GenesisBlocks.silt.isStateOf(world.getBlockState(pos), SiltBlocks.SILT)))
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		if (waterRequired && !WorldUtils.waterInRange(world, pos, 1, 1, 1))
			return false;
		
		if (random.nextInt(rarity) != 0)
			return false;
		
		if (blocks.size() == 0)
		{
			addBlocks(GenesisBlocks.granite.getDefaultState(), GenesisBlocks.mossy_granite.getDefaultState());
		}
		
		int maxHeight = 2 + random.nextInt(this.maxHeight - 1);
		
		generateRockColumn(world, pos, random, maxHeight);
		
		if (random.nextInt(100) > 15)
		{
			generateRockColumn(world, pos.add(1, 0, 0), random, 1 + random.nextInt(maxHeight - 1));
			if (random.nextInt(10) > 5)
				generateRockColumn(world, pos.add(1, 0, 1), random, 1 + random.nextInt(maxHeight - 1));
		}
		
		if (random.nextInt(100) > 15)
		{
			generateRockColumn(world, pos.add(-1, 0, 0), random, 1 + random.nextInt(maxHeight - 1));
			if (random.nextInt(10) > 5)
				generateRockColumn(world, pos.add(-1, 0, -1), random, 1 + random.nextInt(maxHeight - 1));
		}
		
		if (random.nextInt(100) > 15)
		{
			generateRockColumn(world, pos.add(0, 0, 1), random, 1 + random.nextInt(maxHeight - 1));
			if (random.nextInt(10) > 5)
				generateRockColumn(world, pos.add(-1, 0, 1), random, 1 + random.nextInt(maxHeight - 1));
		}
		
		if (random.nextInt(100) > 15)
		{
			generateRockColumn(world, pos.add(0, 0, -1), random, 1 + random.nextInt(maxHeight - 1));
			if (random.nextInt(10) > 5)
				generateRockColumn(world, pos.add(1, 0, -1), random, 1 + random.nextInt(maxHeight - 1));
		}
		
		return true;
	}
	
	public WorldGenRockBoulders setMaxHeight(int mxHeight)
	{
		maxHeight = mxHeight;
		return this;
	}
	
	public WorldGenRockBoulders setRarity(int rarity)
	{
		this.rarity = rarity;
		return this;
	}
	
	public WorldGenRockBoulders setWaterRequired(boolean required)
	{
		waterRequired = required;
		return this;
	}
	
	public WorldGenDecorationBase addBlocks(IBlockState... blockTypes)
	{
		for (int i = 0; i < blockTypes.length; ++i)
		{
			blocks.add(blockTypes[i]);
		}
		
		return this;
	}
	
	private void generateRockColumn(World world, BlockPos pos, Random rand, int height)
	{
		BlockPos rockPos = pos;
		
		for (int i = 1; i <= height; ++i)
		{
			setBlockInWorld(world, rockPos, blocks.get(rand.nextInt(blocks.size())), true);
			rockPos = rockPos.up();
		}
	}
}