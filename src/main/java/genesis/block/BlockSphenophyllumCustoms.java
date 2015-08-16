package genesis.block;

import genesis.block.BlockGrowingPlant.GrowingPlantProperties;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.common.GenesisItems;
import genesis.util.WorldUtils;

import java.util.ArrayList;
import java.util.Random;

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
		if (firstBlock)
		{
			final Item item = GenesisItems.sphenophyllum_fiber;
			
			ArrayList<ItemStack> out = new ArrayList<ItemStack>();
			int age = (Integer) state.getValue(plant.ageProp);
			boolean top = (Boolean) state.getValue(plant.topProp);
			ItemStack addStack = null;
			
			if (top)
			{
				if (age >= plant.maxAge)
				{
					addStack = new ItemStack(item, 1);
				}
				else
				{
					addStack = new ItemStack(item, MathHelper.getRandomIntegerInRange(worldIn.rand, 0, 1));
				}
			}
			else
			{
				if (age >= 5)
				{
					addStack = new ItemStack(item, MathHelper.getRandomIntegerInRange(worldIn.rand, 0, 2));
				}
				else
				{
					addStack = new ItemStack(item, MathHelper.getRandomIntegerInRange(worldIn.rand, 0, 1));
				}
			}
			
			if (addStack != null && addStack.stackSize > 0)
			{
				out.add(addStack);
			}
			
			return out;
		}
		
		return null;
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
