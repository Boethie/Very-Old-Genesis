package genesis.world.gen.feature;

import genesis.combo.variant.EnumTree;
import genesis.util.BlockVolumeShape;
import genesis.util.random.i.IntRange;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeArchaeopteris extends WorldGenTreeBase
{
	public WorldGenTreeArchaeopteris(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.ARCHAEOPTERIS, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand) - 5;
		int branchY = 3 + rand.nextInt(4);
		
		if (!BlockVolumeShape.region(-1, 1, -1, 1, branchY - 1, 1)
					 .and(-2, branchY, -2, 2, height + 2, 2)
					 .hasSpace(pos, isEmptySpace(world)))
			return false;
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(height - 1);
		
		doPineTopLeaves(world, pos, branchPos, height, branchPos.down(branchY).getY(), rand, false);
		
		return true;
	}
}
