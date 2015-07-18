package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenRottenLogAraucarioxylon extends WorldGenTreeBase
{
	public WorldGenRottenLogAraucarioxylon(boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.ROTTEN_LOG, EnumTree.ARAUCARIOXYLON),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.ARAUCARIOXYLON),
				notify);
		
		this.notify = notify;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		Block soil = world.getBlockState(pos.down()).getBlock();
		
		if (
				soil == null 
				|| !soil.canSustainPlant(world, pos, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.ARAUCARIOXYLON))
				|| !world.getBlockState(pos).getBlock().isAir(world, pos))
		{
			return false;
		}
		
		int length = 3 + rand.nextInt(4);
		
		if (!isCubeClear(world, pos.up(), length, 1))
		{
			return false;
		}
		
		BlockPos logPos;
		
		if (rand.nextInt(2) == 0)
		{
			logPos = pos.add(((int)length / 2) * -1, 0, 0);
			
			for (int i = 0; i < length; ++i)
			{
				setBlockInWorld(world, logPos, wood.withProperty(BlockLog.LOG_AXIS, EnumAxis.X));
				
				logPos = logPos.add(1, 0, 0);
			}
		}
		else
		{
			logPos = pos.add(0, 0, ((int)length / 2) * -1);
			
			for (int i = 0; i < length; ++i)
			{
				setBlockInWorld(world, logPos, wood.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z));
				
				if (rand.nextInt(10) > 7 && world.getLight(logPos.up()) < 13)
				{
					setBlockInWorld(world, logPos.up(), GenesisBlocks.archaeomarasimus.getDefaultState());
				}
				
				logPos = logPos.add(0, 0, 1);
			}
		}
		
		return true;
	}
}
