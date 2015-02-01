package genesis.block;

import genesis.block.BlockGrowingPlant.GrowingPlantProperties;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.common.GenesisItems;
import genesis.util.RandomItemDrop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOdontopterisCustoms implements IGrowingPlantCustoms
{
	static final RandomItemDrop seedsDropMature = new RandomItemDrop(GenesisItems.odontopteris_seeds, 1, 3);
	static final RandomItemDrop frondDropMature = new RandomItemDrop(GenesisItems.odontopteris_frond, 1, 2);
	static final RandomItemDrop seedsDropYoung = new RandomItemDrop(GenesisItems.odontopteris_seeds, 1, 1);
	static final RandomItemDrop frondDropYoung = new RandomItemDrop(GenesisItems.odontopteris_frond, 1, 1);
	
	@Override
	public ArrayList<ItemStack> getDrops(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, int fortune, boolean firstBlock)
	{
		ArrayList<ItemStack> out = new ArrayList<ItemStack>();
		int age = (Integer) state.getValue(plant.ageProp);
		boolean top = (Boolean) state.getValue(plant.topProp);
		
		if (top)
		{
			if (age >= plant.maxAge)
			{
				out.add(frondDropMature.getRandom(worldIn.rand));
			}
			else
			{
				out.add(frondDropYoung.getRandom(worldIn.rand));
			}
		}
		else
		{
			if (age >= plant.maxAge)
			{
				out.add(seedsDropMature.getRandom(worldIn.rand));
			}
			else
			{
				out.add(seedsDropYoung.getRandom(worldIn.rand));
			}
		}
		
		return out;
	}

	@Override
	public void updateTick(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, Random rand, boolean grew)
	{
	}
}
