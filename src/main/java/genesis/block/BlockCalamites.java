package genesis.block;

import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.util.WorldUtils;

import java.util.ArrayList;
import java.util.Random;

import genesis.util.blocks.ISitOnBlock;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockCalamites extends BlockGrowingPlant implements IGrowingPlantCustoms, ISitOnBlock
{
	protected static final float RADIUS = 0.2F;
	
	public BlockCalamites(boolean topProperty, int maxAge, int height)
	{
		super(topProperty, maxAge, height);
		
		setHardness(1);
		setHarvestLevel("axe", 0);
		enableStats = true;
		
		setPlantSize(1, 0, RADIUS * 2);
		setCollisionBox(RADIUS);
		setSoundType(GenesisSoundTypes.CALAMITES);
		
		setPlantSoilTypes(EnumPlantType.Plains, EnumPlantType.Desert);
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
	public CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World world, BlockPos pos, PlantState plantState)
	{
		BlockPos under = pos.down();
		
		if (world.getBlockState(under).getBlock() == plant)
			return CanStayOptions.YIELD;
		else if (WorldUtils.waterInRange(world, under, 2, 1))
			return CanStayOptions.YIELD;
		
		return CanStayOptions.NO;
	}
	
	@Override
	public boolean shouldUseBonemeal(World world, BlockPos pos, IBlockState state)
	{
		return isTop(world, pos);
	}
}
