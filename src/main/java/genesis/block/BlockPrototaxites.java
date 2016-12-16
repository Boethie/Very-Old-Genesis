package genesis.block;

import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.combo.variant.EnumSeeds;
import genesis.common.GenesisItems;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockPrototaxites extends BlockGrowingPlant implements IGrowingPlantCustoms
{
	public BlockPrototaxites()
	{
		super(Material.WOOD, false, 15, 5);

		setSoundType(GenesisSoundTypes.MUSHROOM);

		setHardness(0.75F);
		setHarvestLevel("axe", 0);
		enableStats = true;
		setPlantSize(1, 0, 1);
		setCollisionBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1));

		setPlantSoilTypes(EnumPlantType.Plains);
		setGrowth(0.75F, 1, 1, 1);
		setResetAgeOnGrowth(true);
		setAllowPlacingStacked(true);
	}

	@Override
	public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack) {
		BlockPos below = pos.down();
		IBlockState stateBelow = world.getBlockState(below);

		return stateBelow.getBlock() == this && !GenesisItems.SEEDS.isStackOf(stack, EnumSeeds.PROTOTAXITES_FLESH) && new GrowingPlantProperties(world, below).getHeight() < maxHeight ||
						super.canReplace(world, pos, side, stack);
	}

	@Override
	public List<ItemStack> getPlantDrops(BlockGrowingPlant plant, World world, BlockPos pos, IBlockState state, int fortune)
	{
		return Collections.singletonList(GenesisItems.SEEDS.getStack(EnumSeeds.PROTOTAXITES_FLESH, 1));
	}

	@Override
	public CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World world, BlockPos pos, PlantState plantState)
	{
		for (EnumFacing side : EnumFacing.HORIZONTALS)
		{
			BlockPos sidePos = pos.offset(side);
			IBlockState sideState = world.getBlockState(sidePos);

			if (sideState.getBlock() == this || sideState.isSideSolid(world, sidePos, side.getOpposite()) || sideState.getMaterial().isLiquid())
			{
				return CanStayOptions.NO;
			}
		}

		IBlockState stateDown = world.getBlockState(pos.down());
		if (stateDown.getBlock().canSustainPlant(stateDown, world, pos.down(), EnumFacing.UP, this)
				&& !world.getBlockState(pos.up()).getMaterial().isLiquid()
				&& WorldUtils.canSoilSustainTypes(world, pos, EnumPlantType.Plains))
		{
			return CanStayOptions.YES;
		}

		return CanStayOptions.YIELD;
	}

	@Override
	public boolean placeRandomAgePlant(World world, BlockPos pos, Random rand, int flags) {
		return world.isAirBlock(pos) && super.placeRandomAgePlant(world, pos, rand, flags);
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