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
		
		int height = rand.nextInt(11);
		
		generateColumn(world, pos, height);
		
		return true;
	}
	
	private void generateColumn(World world, BlockPos pos, int maxHeight)
	{
		for (int i = 1; i <= 6 + maxHeight; ++i)
		{
			BlockPos colPos = new BlockPos(pos.getX(), i, pos.getZ());
			
			if (
					world.getBlockState(colPos).getBlock() == GenesisBlocks.komatiitic_lava 
					|| world.getBlockState(colPos).getBlock().isAir(world, colPos))
			{
				try{
				world.setBlockState(colPos, stone.getDefaultState());
				}catch(Exception e){}
			}
		}
		
		if (nextInt(4) == 0)
			generateColumn(world, pos.north(), nextInt(maxHeight));
		if (nextInt(4) == 0)
			generateColumn(world, pos.south(), nextInt(maxHeight));
		if (nextInt(4) == 0)
			generateColumn(world, pos.east(), nextInt(maxHeight));
		if (nextInt(4) == 0)
			generateColumn(world, pos.west(), nextInt(maxHeight));
	}
	
	private int nextInt(int i)
	{
		return i <= 1 ? 0 : random.nextInt(i);
	}
}
