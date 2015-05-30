package genesis.block;

import genesis.block.BlockGrowingPlant.GrowingPlantProperties;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms.CanStayOptions;
import genesis.client.GenesisSounds;
import genesis.common.Genesis;
import genesis.common.GenesisItems;
import genesis.util.RandomItemDrop;
import genesis.util.WorldUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
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
		
		setPlantSize(1, 0, RADIUS * 2);
		setCollisionBox(new AxisAlignedBB(0.5F - RADIUS, 0, 0.5F - RADIUS, 0.5F + RADIUS, 1, 0.5F + RADIUS));
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
	public void managePlantMetaProperties(BlockGrowingPlant plant, ArrayList<IProperty> metaProps)
	{
	}

	@Override
	public ArrayList<ItemStack> getPlantDrops(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, int fortune, boolean firstBlock)
	{
		return null;
	}

	@Override
	public void plantUpdateTick(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, Random rand, boolean grew)
	{
	}

	@Override
	public CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World worldIn, BlockPos pos, boolean placed)
	{
		BlockPos under = pos.down();
		Block blockUnder = worldIn.getBlockState(under).getBlock();
		
		if (blockUnder == plant)
		{
			return CanStayOptions.YIELD;
		}
		else if (WorldUtils.waterInRange(worldIn, under, 2, 1))
		{
			return CanStayOptions.YIELD;
		}

		return CanStayOptions.NO;
	}
}
