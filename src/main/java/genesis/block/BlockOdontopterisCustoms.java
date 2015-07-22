package genesis.block;

import genesis.block.BlockGrowingPlant.GrowingPlantProperties;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import genesis.util.*;

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

public class BlockOdontopterisCustoms extends SurviveOnDirtCustoms
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
	static final RandomDrop seedsDropBottom1 = new RandomDrop(GenesisItems.odontopteris_seeds, 0, 1);
	static final RandomDrop seedsDropBottom2Up = new RandomDrop(GenesisItems.odontopteris_seeds, 1, 1);
	
	static final RandomDrop seedsDropTopBeforeMature = new RandomDrop(GenesisItems.odontopteris_seeds, 0, 1);
	static final RandomDrop seedsDropTopMature = new RandomDrop(GenesisItems.odontopteris_seeds, 0, 2);

	static final RandomDrop fiddleheadDrop1 = new RandomDrop(GenesisItems.odontopteris_fiddlehead, 0, 1);
	static final RandomDrop fiddleheadDrop2To4 = new RandomDrop(GenesisItems.odontopteris_fiddlehead, 1, 1);

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
				out.add(seedsDropTopMature.getRandomStackDrop(worldIn.rand));
			}
			else
			{
				out.add(seedsDropTopBeforeMature.getRandomStackDrop(worldIn.rand));
			}
		}
		else
		{
			if (age >= 2)
			{
				out.add(seedsDropBottom2Up.getRandomStackDrop(worldIn.rand));
			}
			else
			{
				out.add(seedsDropBottom1.getRandomStackDrop(worldIn.rand));
			}

			if (age == 1)
			{
				out.add(fiddleheadDrop1.getRandomStackDrop(worldIn.rand));
			}
			if (age >= 2 && age <= 4)
			{
				out.add(fiddleheadDrop2To4.getRandomStackDrop(worldIn.rand));
			}
		}
		
		return out;
	}
}
