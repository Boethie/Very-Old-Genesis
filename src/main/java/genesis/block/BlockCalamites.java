package genesis.block;

import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisSounds;
import genesis.common.GenesisBlocks;
import genesis.util.WorldUtils;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCalamites extends BlockGrowingPlant implements IGrowingPlantCustoms
{
	protected static final float RADIUS = 0.2F;
	
	public BlockCalamites(boolean topPropertyIn, int maxAgeIn, int height)
	{
		super(topPropertyIn, maxAgeIn, height);
		
		setHardness(1);
		setHarvestLevel("axe", 0);
		enableStats = true;
		
		setPlantSize(1, 0, RADIUS * 2);
		setCollisionBox(RADIUS);
		setStepSound(GenesisSounds.CALAMITES);
		
		setResetAgeOnGrowth(true);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 30;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 5;
	}

	@Override
	public void managePlantMetaProperties(BlockGrowingPlant plant, ArrayList<IProperty<?>> metaProps)
	{
	}

	@Override
	public ArrayList<ItemStack> getPlantDrops(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, int fortune)
	{
		return null;
	}

	@Override
	public void plantUpdateTick(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, Random rand, boolean grew)
	{
	}

	@Override
	public CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World world, BlockPos pos, boolean placed)
	{
		BlockPos under = pos.down();
		Block blockUnder = world.getBlockState(under).getBlock();
		
		if (blockUnder == plant)
		{
			return CanStayOptions.YIELD;
		}
		else if (
				blockUnder == Blocks.sand
				|| blockUnder == GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT)
				|| blockUnder == GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.RED_SILT))
		{
			return CanStayOptions.YES;
		}
		else if (WorldUtils.waterInRange(world, under, 2, 1))
		{
			return CanStayOptions.YIELD;
		}

		return CanStayOptions.NO;
	}
	
	@Override
	public boolean shouldUseBonemeal(World world, BlockPos pos, IBlockState state)
	{
		return isTop(world, pos);
	}
}
