package genesis.world.gen.feature;

import genesis.combo.variant.EnumTree;
import genesis.util.random.i.WeightedIntItem;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.WeightedIntProvider;

import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeAraucarioxylon extends WorldGenTreeBase
{
	public WorldGenTreeAraucarioxylon(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.ARAUCARIOXYLON, IntRange.create(minHeight, maxHeight), notify);
		
		this.saplingCountProvider = new WeightedIntProvider(
				WeightedIntItem.of(4, 0),
				WeightedIntItem.of(6, IntRange.create(0, 5)));
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		
		if (!isCubeClear(world, pos, 1, height))
			return false;
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(height - 1);
		
		int leavesBase = 0;
		boolean alternate = true;
		boolean irregular = true;
		boolean inverted = false;
		int maxLeavesLength = 3;
		
		switch (treeType)
		{
		case TYPE_2:
			leavesBase = branchPos.getY() - 4 - rand.nextInt(6);
			alternate = false;
			inverted = true;
			irregular = false;
			maxLeavesLength = 4;
			break;
		default:
			maxLeavesLength = 2;
			leavesBase = branchPos.getY() - 2 - rand.nextInt(2);
			break;
		}
		
		int base = 4 + rand.nextInt(4);
		int direction = rand.nextInt(8);
		
		int lFactor;
		
		for (int i = base; i < height && treeType == TreeTypes.TYPE_1; ++i)
		{
			++direction;
			if (direction > 7)
				direction = 0;
			
			lFactor = (int)(6 * (((height - i) / (float) height)));
			
			branchDown(world, pos.up(i), rand, pos.getY(), direction + 1, lFactor);
			
			if (rand.nextInt(8) == 0)
			{
				++direction;
				if (direction > 7)
					direction = 0;
				
				branchDown(world, pos.up(i), rand, pos.getY(), direction + 1, lFactor);
			}
		}
		
		doPineTopLeaves(world, pos, branchPos, height, leavesBase, rand, alternate, maxLeavesLength, irregular, inverted);
		
		return true;
	}
	
	private void branchDown(World world, BlockPos pos, Random rand, int groundLevel, int direction, int lengthModifier)
	{
		int fallX = 1;
		int fallZ = 1;
		BlockPos upPos = pos.down();
		EnumAxis woodAxis = EnumAxis.Y;
		
		int fallDistance = lengthModifier;
		
		switch(direction)
		{
		case 0:
			fallX = 1;
			fallZ = 1;
			break;
		case 1:
			fallX = 0;
			fallZ = 1;
			break;
		case 2:
			fallX = 1;
			fallZ = 1;
			break;
		case 3:
			fallX = 1;
			fallZ = 0;
			break;
		case 4:
			fallX = 1;
			fallZ = -1;
			break;
		case 5:
			fallX = 0;
			fallZ = -1;
			break;
		case 6:
			fallX = -1;
			fallZ = -1;
			break;
		case 7:
			fallX = -1;
			fallZ = 0;
		case 8:
			fallX = -1;
			fallZ = 1;
			break;
		}
		
		boolean leaves = true;
		int horzCount = 0;
		
		for (int i = 0; i < fallDistance; i++)
		{
			if (upPos.getY() < groundLevel + 3)
				return;
			
			upPos = upPos.add(fallX, 0, fallZ);
			
			if (fallX != 0)
				woodAxis = EnumAxis.X;
			else if (fallZ != 0)
				woodAxis = EnumAxis.Z;
			else
				woodAxis = EnumAxis.Y;
			
			if (horzCount < 1 + rand.nextInt(3))
			{
				++horzCount;
				
				if (rand.nextInt(3) == 0 || (fallX == 0 && fallZ == 0))
					upPos = upPos.down();
			}
			else
			{
				horzCount = 0;
				
				woodAxis = EnumAxis.Y;
				upPos = upPos.down();
			}
			
			setBlockInWorld(world, upPos, wood.withProperty(BlockLog.LOG_AXIS, woodAxis));
			
			if (leaves && rand.nextInt(6) == 0)
			{
				doBranchLeaves(world, upPos, rand, true, 3, true);
				doBranchLeaves(world, upPos.down(), rand, true, 2, true);
			}
			
			leaves = !leaves;
			
			if (i == fallDistance - 1)
				doBranchLeaves(world, upPos, rand, false, 1 + rand.nextInt(2), true);
		}
	}
}
