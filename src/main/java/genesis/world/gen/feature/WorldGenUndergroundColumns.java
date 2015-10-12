package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenUndergroundColumns extends WorldGenerator
{
	public final Block[] blocks;
	
	private final Random random;
	private final int maxHeight;
	
	public WorldGenUndergroundColumns(Random r, int h)
	{
		this.random = r;
		this.maxHeight = (h < 5)? 5 : h;
		
		this.blocks = new Block[1];
		this.blocks[0] = GenesisBlocks.granite;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos position)
	{
		BlockPos pos = new BlockPos(position.getX(), 6, position.getZ());
		
		if (world.getBlockState(pos).getBlock() != GenesisBlocks.komatiitic_lava)
			return false;
		
		int height = nextInt(maxHeight - 5);
		
		generateColumn(world, pos, height, 8);
		
		return true;
	}
	
	private void generateColumn(World world, BlockPos pos, int height, int count)
	{
		if (count < 2)
			return;
		
		if (height < 0)
			height = 0;
		
		for (int i = 1; i <= 6 + height; ++i)
		{
			BlockPos colPos = new BlockPos(pos.getX(), i, pos.getZ());
			
			if (
					world.getBlockState(colPos).getBlock() == GenesisBlocks.komatiitic_lava 
					|| world.getBlockState(colPos).getBlock().isAir(world, colPos))
			{
				world.setBlockState(colPos, blocks[random.nextInt(blocks.length)].getDefaultState());
			}
		}
		
		if (nextInt(3) == 0)
			generateColumn(world, pos.north(), height - (1 + nextInt(3)), count - 1);
		if (nextInt(3) == 0)
			generateColumn(world, pos.south(), height - (1 + nextInt(3)), count - 1);
		if (nextInt(3) == 0)
			generateColumn(world, pos.east(), height - (1 + nextInt(3)), count - 1);
		if (nextInt(3) == 0)
			generateColumn(world, pos.west(), height - (1 + nextInt(3)), count - 1);
	}
	
	private int nextInt(int i)
	{
		return i <= 1 ? 0 : random.nextInt(i);
	}
}
