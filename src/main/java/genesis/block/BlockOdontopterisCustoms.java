package genesis.block;

import genesis.block.BlockGrowingPlant.GrowingPlantProperties;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import genesis.util.RandomItemDrop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOdontopterisCustoms implements IGrowingPlantCustoms
{
	/* |-----------------------------------------|
	 * | TOP         | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
	 * |-------------|---------------------------|
	 * |  Seeds      |       0       |  0-1  |0-2|
	 * |-------------|---------------------------|
	 * |  Fiddlehead |             0             |
	 * |-----------------------------------------|
	 * | BOTTOM      | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
	 * |-------------|---------------------------|
	 * |  Seeds      |0-1|          1            |
	 * |-------------|---------------------------|
	 * |  Fiddlehead |0-1|     1    |     0      |
	 * |-----------------------------------------|
	 */
	static final RandomItemDrop seedsDropBottom1 = new RandomItemDrop(GenesisItems.odontopteris_seeds, 0, 1);
	static final RandomItemDrop seedsDropBottom2Up = new RandomItemDrop(GenesisItems.odontopteris_seeds, 1, 1);
	
	static final RandomItemDrop seedsDropTopBeforeMature = new RandomItemDrop(GenesisItems.odontopteris_seeds, 0, 1);
	static final RandomItemDrop seedsDropTopMature = new RandomItemDrop(GenesisItems.odontopteris_seeds, 0, 2);

	static final RandomItemDrop fiddleheadDrop1 = new RandomItemDrop(GenesisItems.odontopteris_fiddlehead, 0, 1);
	static final RandomItemDrop fiddleheadDrop2To4 = new RandomItemDrop(GenesisItems.odontopteris_fiddlehead, 1, 1);

	@Override
	public void managePlantMetaProperties(BlockGrowingPlant plant, ArrayList<IProperty> metaProps)
	{
	}

	@Override
	public ArrayList<ItemStack> getPlantDrops(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, int fortune, boolean firstBlock)
	{
		ArrayList<ItemStack> out = new ArrayList<ItemStack>();
		int age = (Integer) state.getValue(plant.ageProp);
		boolean top = (Boolean) state.getValue(plant.topProp);
		
		if (top)
		{
			if (age >= plant.maxAge)
			{
				out.add(seedsDropTopMature.getRandomStack(worldIn.rand));
			}
			else
			{
				out.add(seedsDropTopBeforeMature.getRandomStack(worldIn.rand));
			}
		}
		else
		{
			if (age >= 2)
			{
				out.add(seedsDropBottom2Up.getRandomStack(worldIn.rand));
			}
			else
			{
				out.add(seedsDropBottom1.getRandomStack(worldIn.rand));
			}

			if (age == 1)
			{
				out.add(fiddleheadDrop1.getRandomStack(worldIn.rand));
			}
			if (age >= 2 && age <= 4)
			{
				out.add(fiddleheadDrop2To4.getRandomStack(worldIn.rand));
			}
		}
		
		return out;
	}
	
	@Override
	public void plantUpdateTick(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, Random rand, boolean grew)
	{
	}
	
	@Override
	public CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World worldIn, BlockPos pos, boolean placed)
	{
		if (placed)
		{
			Block block = worldIn.getBlockState(pos.down()).getBlock();
			
			if (block == Blocks.dirt || block == GenesisBlocks.moss)
			{
				return CanStayOptions.YES;
			}
		}
		
		return CanStayOptions.YIELD;
	}
}
