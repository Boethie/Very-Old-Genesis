package genesis.block;

import genesis.block.BlockGrowingPlant.GrowingPlantProperties;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms.CanStayOptions;
import genesis.common.GenesisItems;
import genesis.util.WorldUtils;
import genesis.util.random.IntRange;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockSphenophyllumCustoms implements IGrowingPlantCustoms
{
	@Override
	public void managePlantMetaProperties(BlockGrowingPlant plant, ArrayList<IProperty> metaProps)
	{
	}
	
	@Override
	public ArrayList<ItemStack> getPlantDrops(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, int fortune, boolean firstBlock)
	{
		final Item item = GenesisItems.sphenophyllum_fiber;
		
		ArrayList<ItemStack> out = new ArrayList<ItemStack>();
		int age = (Integer) state.getValue(plant.ageProp);
		boolean top = (Boolean) state.getValue(plant.topProp);
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
			ItemStack addStack = new ItemStack(item, range.get(worldIn.rand));
			
			if (addStack.stackSize > 0)
			{
				out.add(addStack);
			}
		}
		
		return out;
	}

	@Override
	public void plantUpdateTick(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, Random rand, boolean grew)
	{
		int age = (Integer) state.getValue(plant.ageProp);
		
		if (age >= plant.maxAge && rand.nextInt(35) == 0)
		{
			GrowingPlantProperties props = new GrowingPlantProperties(worldIn, pos);
			pos = props.getBottom();
			
			Iterable<BlockPos> checkArea = (Iterable<BlockPos>) BlockPos.getAllInBox(pos.add(-3, -1, -3), pos.add(3, 1, 3));
			
			int plantsLeft = 6;
			
			for (BlockPos plantCheck : checkArea)
			{
				if (worldIn.getBlockState(plantCheck).getBlock() == plant && new GrowingPlantProperties(worldIn, plantCheck).isBottom(plantCheck))
				{
					plantsLeft--;

					if (plantsLeft <= 0)
					{
						break;
					}
				}
			}
			
			ArrayList<BlockPos> spreadToList = new ArrayList<BlockPos>();
			
			for (BlockPos plantCheck : (Iterable<BlockPos>) BlockPos.getAllInBox(pos.add(-1, 0, -1), pos.add(1, 1, 1)))
			{
				spreadToList.add(plantCheck);
			}
			
			if (plantsLeft > 0)
			{
				BlockPos spreadPos;
				int tries = 10;
				
				do
				{
					spreadPos = spreadToList.get(rand.nextInt(spreadToList.size()));
					
					if (worldIn.isAirBlock(spreadPos) && plant.canPlaceBlockAt(worldIn, spreadPos) && WorldUtils.waterInRange(worldIn, spreadPos.down(), 3, 1))
					{
						break;
					}
					else
					{
						tries--;
					}
				}
				while (tries > 0);
				
				if (tries > 0)
				{
					worldIn.setBlockState(spreadPos, plant.getDefaultState());
				}
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
