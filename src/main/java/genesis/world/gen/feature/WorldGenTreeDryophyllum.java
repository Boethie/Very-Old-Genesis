package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.random.i.WeightedIntItem;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.WeightedIntProvider;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeDryophyllum extends WorldGenTreeBase
{
	public WorldGenTreeDryophyllum(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.DRYOPHYLLUM, IntRange.create(minHeight, maxHeight), notify);
		
		this.saplingCountProvider = new WeightedIntProvider(
				WeightedIntItem.of(104, 0),
				WeightedIntItem.of(6, IntRange.create(1, 3)));
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		int base = 5 + rand.nextInt(4);
		
		if (!isCubeClear(world, pos.up(base), 3, height))
		{
			return false;
		}
		
		int mainBranches = (treeType == TreeTypes.TYPE_1)? 2 + rand.nextInt(2) : 4 + rand.nextInt(8);
		
		for (int i = 0; i < mainBranches; ++i)
		{
			base = 5 + rand.nextInt(6);
			branchUp(world, pos, rand, height, (base >= height - 2)? height - 5: base);
		}
		
		return true;
	}
	
	private void branchUp(World world, BlockPos pos, Random rand, int height, int base)
	{
		int fallX = 1 - rand.nextInt(3);
		int fallZ = 1 - rand.nextInt(3);
		int fallCount = 0;
		BlockPos upPos = pos.down();
		EnumAxis woodAxis = EnumAxis.Y;
		
		for (int i = 0; i < height; i++)
		{
			if (rand.nextInt(3) == 0 && i > base && fallCount < 2)
			{
				fallCount++;
				
				upPos = upPos.add(fallX, 0, fallZ);
				
				if (fallX != 0)
					woodAxis = EnumAxis.X;
				else if (fallZ != 0)
					woodAxis = EnumAxis.Z;
				else
					woodAxis = EnumAxis.Y;
				
				if (rand.nextInt(3) == 0 || (fallX == 0 && fallZ == 0))
					upPos = upPos.up();
			}
			else
			{
				fallCount = 0;
				
				woodAxis = EnumAxis.Y;
				upPos = upPos.up();
			}
			
			setBlockInWorld(world, upPos, wood.withProperty(BlockLog.LOG_AXIS, woodAxis));
			
			if (i == base - 1)
				doBranchLeaves(world, upPos, rand, false, 2, true);
			
			if (i > base - 1)
			{
				doBranchLeaves(world, upPos, rand, true, 4, true);
			}
		}
	}
}
