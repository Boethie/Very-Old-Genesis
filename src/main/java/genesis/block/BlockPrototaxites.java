package genesis.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import genesis.block.BlockGrowingPlant.*;
import genesis.common.*;
import genesis.metadata.EnumSeeds;
import genesis.util.WorldUtils;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockPrototaxites extends BlockGrowingPlant implements IGrowingPlantCustoms
{
	public BlockPrototaxites()
	{
		super(Material.wood, false, 15, 5);
		
		setStepSound(GenesisSounds.MUSHROOM);
		
		setHardness(0.75F);
		setHarvestLevel("axe", 0);
		enableStats = true;
		setPlantSize(1, 0, 1);
		setCollisionBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1));
		
		setPlantSoilTypes(EnumPlantType.Plains, BlockPrototaxitesMycelium.SOIL_TYPE);
		setGrowth(0.75F, 1, 1, 1);
		setResetAgeOnGrowth(true);
		setAllowPlacingStacked(false);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		BlockPos below = pos.down();
		
		if (WorldUtils.canSoilSustainTypes(world, pos, EnumPlantType.Plains))
			world.setBlockState(below, GenesisBlocks.prototaxites_mycelium.getDefaultState());
		
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}
	
	@Override
	public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack)
	{
		BlockPos below = pos.down();
		IBlockState stateBelow = world.getBlockState(below);
		
		if (stateBelow.getBlock() == this && !GenesisItems.seeds.isStackOf(stack, EnumSeeds.PROTOTAXITES_FLESH)
				&& new GrowingPlantProperties(world, below).getHeight() < maxHeight)
			return true;
		
		return super.canReplace(world, pos, side, stack);
	}
	
	@Override
	public List<ItemStack> getPlantDrops(BlockGrowingPlant plant, World world, BlockPos pos, IBlockState state, int fortune)
	{
		return Collections.singletonList(GenesisItems.seeds.getStack(EnumSeeds.PROTOTAXITES_FLESH, 1));
	}
	
	@Override
	public CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World world, BlockPos pos, boolean placed)
	{
		for (EnumFacing side : EnumFacing.HORIZONTALS)
			if (world.getBlockState(pos.offset(side)).getBlock() == this)
				return CanStayOptions.NO;
		
		if (placed && WorldUtils.canSoilSustainTypes(world, pos, EnumPlantType.Plains))
			return CanStayOptions.NO;
		
		return CanStayOptions.YIELD;
	}
	
	@Override
	public void plantUpdateTick(BlockGrowingPlant plant, World world, BlockPos pos, IBlockState state, Random rand, boolean grew)
	{
	}
	
	@Override
	public void managePlantMetaProperties(BlockGrowingPlant plant, ArrayList<IProperty<?>> metaProps)
	{
	}
	
	@Override
	public boolean shouldUseBonemeal(World world, BlockPos pos, IBlockState state) 
	{
		return true;
	}
}
