package genesis.world.gen.feature;

import java.util.Random;
import genesis.block.BlockAnkyropteris;
import genesis.block.BlockFrullania;
import genesis.block.BlockGenesisLogs;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.BlockVolumeShape;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.WeightedIntItem;
import genesis.util.random.i.WeightedIntProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeMetasequoia extends WorldGenTreeBase
{
	public WorldGenTreeMetasequoia(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.METASEQUOIA, IntRange.create(minHeight, maxHeight), notify);
		
		this.saplingCountProvider = new WeightedIntProvider(
				WeightedIntItem.of(104, 0),
				WeightedIntItem.of(6, IntRange.create(1, 3)));
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		
		int trunkHeight = 3 + rand.nextInt(4);
		int leavesBase = pos.getY() + trunkHeight;
		
		int rMin = treeType == TreeTypes.TYPE_1 ? 0 : 1;
		int rMax = treeType == TreeTypes.TYPE_1 ? 2 : 3;
		int add = treeType == TreeTypes.TYPE_2 ? 1 : 0;
		if (!BlockVolumeShape.region(-rMin, 1, -rMin, rMin + add, trunkHeight, rMin + add)
					 .and(-rMax, trunkHeight + 1, -rMax, rMax + add, height, rMax + add)
					 .hasSpace(pos, isEmptySpace(world)))
			return false;
		
		BlockPos checkPos = pos;
		
		if (treeType == TreeTypes.TYPE_2
				&& (getTreePos(world, checkPos = checkPos.east(), 1) == null
					|| getTreePos(world, checkPos = checkPos.south(), 1) == null)
					|| getTreePos(world, checkPos = checkPos.west(), 1) == null)
			return false;
		
		for (BlockPos cornerPos : BlockPos.getAllInBoxMutable(pos, pos.add(1, 0, 1)))
		{
			if (cornerPos.equals(pos))
				continue;
			
			BlockPos groundPos = getTreePos(world, cornerPos);
			
			if (groundPos == null)
				return false;
			
			pos = new BlockPos(pos.getX(), Math.min(pos.getY(), groundPos.getY()), pos.getZ());
		}
		
		for (int i = 0; i < height; i++)
		{
			switch (treeType)
			{
			case TYPE_2:
				placeTrunkAndVine(world, pos.add(1, i, 0));
				placeTrunkAndVine(world, pos.add(0, i, 1));
				placeTrunkAndVine(world, pos.add(1, i, 1));
				placeTrunkAndVine(world, pos.add(0, i, 0));
				break;
			default:
				placeTrunkAndVine(world, pos.up(i));
				break;
			}
		}
		
		BlockPos branchPos = pos.up(height - 1);
		
		switch (treeType)
		{
		case TYPE_2:
			doPineTopLeaves(world, pos, branchPos.add(0, 0, 0), height, leavesBase, rand, false, true, false);
			doPineTopLeaves(world, pos, branchPos.add(1, 0, 1), height, leavesBase, rand, false, true, false);
			doPineTopLeaves(world, pos, branchPos.add(1, 0, 0), height, leavesBase, rand, false, true, false);
			doPineTopLeaves(world, pos, branchPos.add(0, 0, 1), height, leavesBase, rand, false, true, false);
			break;
		default:
			doPineTopLeaves(world, pos, branchPos, height, leavesBase, rand, false, true, false);
			break;
		}
		
		switch (treeType)
		{
		case TYPE_2:
			generateResin(world, pos.add(1, 0, 0), height);
			generateResin(world, pos.add(0, 0, 1), height);
			generateResin(world, pos.add(1, 0, 1), height);
			generateResin(world, pos.add(0, 0, 0), height);
			break;
		default:
			generateResin(world, pos, height);
			break;
		}
		
		return true;
	}
	
	
	
	private void placeTrunkAndVine(World world, BlockPos pos)
	{
		setBlockInWorld(world, pos, wood, false);
		for ( EnumFacing facing : EnumFacing.HORIZONTALS )
		{
			switch (facing)
			{
				case NORTH: this.placeVine(world, world.rand, pos.north(), BlockFrullania.SOUTH); break;
				case SOUTH: this.placeVine(world, world.rand, pos.south(), BlockFrullania.NORTH); break;
				case EAST: this.placeVine(world, world.rand, pos.east(), BlockFrullania.WEST); break;
				case WEST: this.placeVine(world, world.rand, pos.west(), BlockFrullania.EAST); break;
				default: break;
			}
		}
	}
	
    private void placeVine(World world, Random rand, BlockPos pos, PropertyBool side)
    {
        if (rand.nextInt(3) > 0 && world.isAirBlock(pos))
        {
            this.setBlockAndNotifyAdequately(world, pos, GenesisBlocks.FRULLANIA.getDefaultState().withProperty(side, Boolean.valueOf(true)));
        }
    }
	
}
