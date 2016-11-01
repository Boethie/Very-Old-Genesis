package genesis.world.gen.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public abstract class WorldGenStructureHelper
{
	protected int rarity = 3;
	
	public abstract List<Biome> getAllowedBiomes();
	public abstract GenerationType getGenerationType();
	protected abstract boolean doGenerate(World world, Random rand, BlockPos pos);
	
	protected boolean generate(World world, Random rand, BlockPos pos)
	{
		switch (getGenerationType())
		{
		case FINDGROUND:
			pos = findGround(world, pos);
			break;
		default:
			pos = new BlockPos(pos.getX(), rand.nextInt(255), pos.getZ());
			break;
		}
		
		if (!(rand.nextInt(rarity) == 0))
			return false;
		
		boolean generated = doGenerate(world, rand, pos);
		
		return generated;
	}
	
	public BlockPos findGround(World world, BlockPos pos)
	{
		BlockPos groundPos = new BlockPos(pos.getX(), 255, pos.getZ());
		
		do {
			if (!world.isAirBlock(groundPos))
				break;
			
			groundPos = groundPos.down();
		} while (groundPos.getY() > 0);
		
		return groundPos;
	}
	
	public boolean setBlockInWorld(World world, BlockPos pos, IBlockState state)
	{
		if (!world.isBlockLoaded(pos))
			return false;
		
		world.setBlockState(pos, state);
		
		return true;
	}
	
	public BlockPos findBlockInArea(World world, BlockPos center, int areaExpand, int height, IBlockState match, boolean surface)
	{
		BlockPos pos = null;
		
		state_search:
		for (int x = -areaExpand; x <= areaExpand; ++x)
		{
			for (int z = -areaExpand; z <= areaExpand; ++z)
			{
				for (int y = height; y >= (surface ? 0 : -height); --y)
				{
					if (world.isBlockLoaded(center.add(x, y, z)) && world.getBlockState(center.add(x, y, z)) == match)
					{
						pos = center.add(x, y, z);
						break state_search;
					}
				}
			}
		}
		
		return pos;
	}
	
	public boolean checkSurface(World world, BlockPos center, int squareArea, int height)
	{
		for (int x = -squareArea; x <= squareArea; ++ x)
		{
			for (int z = -squareArea; z <= squareArea; ++z)
			{
				if (world.isAirBlock(center.add(x, 0, z)))
					return false;
				
				for (int y = height; y > 1; --y)
				{
					if(!world.isAirBlock(center.add(x, y, z)))
						return false;
				}
			}
		}
		
		return true;
	}
	
	public enum GenerationType
	{
		FINDGROUND,
		ANYWHERE;
	}
}
