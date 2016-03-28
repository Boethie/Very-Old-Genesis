package genesis.block;

import genesis.block.BlockGrowingPlant.GrowingPlantProperties;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.util.WorldUtils;
import genesis.util.random.IntRange;

import java.util.*;

import com.google.common.collect.Lists;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWaterSpreadingPlantCustoms implements IGrowingPlantCustoms
{
	protected final ItemStack drop;
	protected final int spreadRate;
	
	public BlockWaterSpreadingPlantCustoms(ItemStack drop, int spreadRate)
	{
		this.drop = drop;
		this.spreadRate = spreadRate;
	}
	
	public BlockWaterSpreadingPlantCustoms(ItemStack drop)
	{
		this(drop, 35);
	}
	
	@Override
	public void managePlantMetaProperties(BlockGrowingPlant plant, ArrayList<IProperty<?>> metaProps)
	{
	}
	
	@Override
	public List<ItemStack> getPlantDrops(BlockGrowingPlant plant, World world, BlockPos pos, IBlockState state, int fortune)
	{
		int age = state.getValue(plant.ageProp);
		boolean top = state.getValue(plant.topProp);
		IntRange range = null;
		
		if (!top)
		{	// Bottom
			switch (age)
			{
			case 0:
			case 1:
				break;
			case 2:
				range = IntRange.create(0, 1);
				break;
			case 3:
				range = IntRange.create(1);
				break;
			case 4:
				range = IntRange.create(1, 2);
				break;
			case 5:
			case 6:
			case 7:
				range = IntRange.create(1, 3);
				break;
			}
		}
		else
		{	// Top
			switch (age)
			{
			case 5:
				range = IntRange.create(0, 1);
			case 6:
				range = IntRange.create(1);
			case 7:
				range = IntRange.create(1, 2);
				break;
			}
		}
		
		if (range != null)
		{
			ItemStack stack = drop.copy();
			stack.stackSize = range.get(world.rand);
			
			if (stack.stackSize > 0)
			{
				return Collections.singletonList(stack);
			}
		}
		
		return Collections.emptyList();
	}

	@Override
	public void plantUpdateTick(BlockGrowingPlant plant, World world, BlockPos pos, IBlockState state, Random rand, boolean grew)
	{
		int age = state.getValue(plant.ageProp);
		
		if (age >= plant.maxAge && rand.nextInt(spreadRate) == 0)
		{
			GrowingPlantProperties props = new GrowingPlantProperties(world, pos);
			pos = props.getBottom();
			
			Iterable<BlockPos> checkArea = WorldUtils.getArea(pos.add(-3, -1, -3), pos.add(3, 1, 3));
			
			int plantsLeft = 6;
			
			for (BlockPos plantCheck : checkArea)
			{
				if (world.getBlockState(plantCheck).getBlock() == plant && new GrowingPlantProperties(world, plantCheck).isBottom(plantCheck))
				{
					plantsLeft--;

					if (plantsLeft <= 0)
					{
						break;
					}
				}
			}
			
			ArrayList<BlockPos> toList = Lists.newArrayList(WorldUtils.getArea(pos.add(-1, 0, -1), pos.add(1, 1, 1)));
			
			if (plantsLeft > 0)
			{
				int tries = 5;
				
				do
				{
					BlockPos to = toList.remove(rand.nextInt(toList.size()));
					
					if (world.isAirBlock(to) && plant.canPlaceBlockAt(world, to) && WorldUtils.waterInRange(world, to.down(), 3, 1))
					{
						world.setBlockState(to, plant.getDefaultState());
						break;
					}
				}
				while (--tries > 0);
			}
		}
	}

	@Override
	public CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World worldIn, BlockPos pos, boolean placed)
	{
		return CanStayOptions.YIELD;
	}

	@Override
	public boolean shouldUseBonemeal(World world, BlockPos pos, IBlockState state)
	{
		return true;
	}
}
