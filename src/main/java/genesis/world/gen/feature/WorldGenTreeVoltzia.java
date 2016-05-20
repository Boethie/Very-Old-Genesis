package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.random.i.IntRange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeVoltzia extends WorldGenTreeBase
{
	public WorldGenTreeVoltzia (int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.VOLTZIA, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		
		if (!isCubeClear(world, pos, 1, height))
			return false;
		
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(height - 2);
		
		doPineTopLeaves(world, pos, branchPos, height, pos.getY() + 1, rand, false, 2, true, false);
		
		return true;
	}
}
