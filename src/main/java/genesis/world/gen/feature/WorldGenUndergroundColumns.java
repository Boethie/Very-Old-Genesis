package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenUndergroundColumns extends WorldGenerator
{
	public final Block stone;
	
	private final Random random;
	
	public WorldGenUndergroundColumns(Random r)
	{
		this.stone = GenesisBlocks.komatiite;
		this.random = r;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos position)
	{
		BlockPos pos = new BlockPos(position.getX(), 6, position.getZ());
		
		if (world.getBlockState(pos).getBlock() != GenesisBlocks.komatiitic_lava)
			return false;
		
		int height = rand.nextInt(13);
		
		generateColumn(world, pos, height, 8);
		
		return true;
	}
	
	private void generateColumn(World world, BlockPos pos, int maxHeight, int count)
	{
		if (count <= 0)
			return;
		
		if (maxHeight < 0)
			maxHeight = 0;
		
		for (int i = 1; i <= 6 + maxHeight; ++i)
		{
			BlockPos colPos = new BlockPos(pos.getX(), i, pos.getZ());
			
			if (
					world.getBlockState(colPos).getBlock() == GenesisBlocks.komatiitic_lava 
					|| world.getBlockState(colPos).getBlock().isAir(world, colPos))
			{
				world.setBlockState(colPos, stone.getDefaultState());
			}
		}
		
		if (nextInt(3) == 0)
			generateColumn(world, pos.north(), maxHeight - (1 + nextInt(3)), count - 1);
		if (nextInt(3) == 0)
			generateColumn(world, pos.south(), maxHeight - (1 + nextInt(3)), count - 1);
		if (nextInt(3) == 0)
			generateColumn(world, pos.east(), maxHeight - (1 + nextInt(3)), count - 1);
		if (nextInt(3) == 0)
			generateColumn(world, pos.west(), maxHeight - (1 + nextInt(3)), count - 1);
	}
	
	private int nextInt(int i)
	{
		return i <= 1 ? 0 : random.nextInt(i);
	}
}
