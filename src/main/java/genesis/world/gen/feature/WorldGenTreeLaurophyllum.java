package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.random.i.IntRange;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenTreeLaurophyllum extends WorldGenTreeBase
{
	public WorldGenTreeLaurophyllum(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.LAUROPHYLLUM, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		
		if (!isCubeClear(world, pos.up(), 1, height))
			return false;
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
			
			if (i < height)
			{
				if (rand.nextInt(3) == 0)
					setBlockInWorld(world, pos.add(rand.nextInt(3) - 1, i, rand.nextInt(3) - 1), wood);
				if (rand.nextInt(3) == 0)
					setBlockInWorld(world, pos.add(rand.nextInt(3) - 1, i, rand.nextInt(3) - 1), wood);
				if (rand.nextInt(3) == 0)
					setBlockInWorld(world, pos.add(rand.nextInt(3) - 1, i, rand.nextInt(3) - 1), wood);
				if (rand.nextInt(3) == 0)
					setBlockInWorld(world, pos.add(rand.nextInt(3) - 1, i, rand.nextInt(3) - 1), wood);
				
				super.doBranchLeaves(world, pos.up(i), rand, true, MathHelper.clamp_int(i + 1, 0, 2), true);
			}
		}
		
		return true;
	}
}
