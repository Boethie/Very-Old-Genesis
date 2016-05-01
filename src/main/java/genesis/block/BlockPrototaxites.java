package genesis.block;

import java.util.*;

import genesis.block.BlockGrowingPlant.*;
import genesis.combo.variant.EnumSeeds;
import genesis.common.*;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.util.WorldUtils;
import genesis.util.random.i.RandomIntProvider;

import net.minecraft.block.material.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockPrototaxites extends BlockGrowingPlant implements IGrowingPlantCustoms
{
	public BlockPrototaxites()
	{
		super(Material.wood, false, 15, 5);
		
		setSoundType(GenesisSoundTypes.MUSHROOM);
		
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
		
		if (WorldUtils.canSoilSustainTypes(world, pos, EnumPlantType.Plains)
				&& !WorldUtils.canSoilSustainTypes(world, pos, BlockPrototaxitesMycelium.SOIL_TYPE))
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
		{
			BlockPos sidePos = pos.offset(side);
			IBlockState sideState = world.getBlockState(sidePos);
			
			if (sideState.getBlock() == this
					|| sideState.isSideSolid(world, sidePos, side.getOpposite()))
				return CanStayOptions.NO;
		}
		
		if (placed
				&& WorldUtils.canSoilSustainTypes(world, pos, EnumPlantType.Plains)
				&& !WorldUtils.canSoilSustainTypes(world, pos, BlockPrototaxitesMycelium.SOIL_TYPE))
			return CanStayOptions.NO;
		
		return CanStayOptions.YIELD;
	}
	
	protected void placeMycelium(World world, BlockPos pos, Random rand, int down, int flags)
	{
		for (int i = 0; i <= down; i++)
		{
			BlockPos soilPos = pos.down(i);
			IBlockState soilState = world.getBlockState(soilPos);
			
			if (soilState.getBlock() == Blocks.dirt
					|| soilState.getBlock() == GenesisBlocks.moss)
			{
				world.setBlockState(soilPos, GenesisBlocks.prototaxites_mycelium.getDefaultState(), flags);
				return;
			}
		}
	}
	
	protected void placeAreaMycelium(World world, BlockPos pos, Random rand, int flags)
	{
		if (rand.nextInt(5) <= 3)
		{
			placeMycelium(world, pos, rand, 1, flags);
			
			for (EnumFacing side : EnumFacing.HORIZONTALS)
				if (rand.nextInt(3) == 0)
					placeMycelium(world, pos.offset(side), rand, 1, flags);
		}
	}
	
	@Override
	public void placePlant(World world, BlockPos pos, Random rand, int height, RandomIntProvider age, int flags)
	{
		BlockPos soil = pos.down();
		
		placeMycelium(world, soil, rand, 0, flags);
		
		for (EnumFacing side : EnumFacing.HORIZONTALS)
			placeAreaMycelium(world, soil.offset(side), rand, flags);
		
		super.placePlant(world, pos, rand, height, age, flags);
	}
	
	@Override
	public boolean placeRandomAgePlant(World world, BlockPos pos, Random rand, int flags)
	{
		if (!world.isAirBlock(pos))
			return false;
		
		return super.placeRandomAgePlant(world, pos, rand, flags);
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
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		return getBoundingBox(state, world, pos);
	}
}
