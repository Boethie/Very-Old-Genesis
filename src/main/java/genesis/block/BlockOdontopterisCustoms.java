package genesis.block;

import genesis.combo.variant.EnumMaterial;
import genesis.combo.variant.EnumSeeds;
import genesis.common.GenesisItems;
import genesis.util.random.drops.blocks.BlockDrops;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
	 * |  Seeds      |0-1|           1           |
	 * |-------------|---------------------------|
	 * |  Fiddlehead |0-1|     1     |    0      |
	 * |-----------------------------------------|
	 */
	static final BlockDrops seedsDropBottom1 = new BlockDrops(GenesisItems.SEEDS.getStack(EnumSeeds.ODONTOPTERIS_SEEDS), 0, 1);
	static final BlockDrops seedsDropBottom2Up = new BlockDrops(GenesisItems.SEEDS.getStack(EnumSeeds.ODONTOPTERIS_SEEDS), 1, 1);
	
	static final BlockDrops seedsDropTopBeforeMature = new BlockDrops(GenesisItems.SEEDS.getStack(EnumSeeds.ODONTOPTERIS_SEEDS), 0, 1);
	static final BlockDrops seedsDropTopMature = new BlockDrops(GenesisItems.SEEDS.getStack(EnumSeeds.ODONTOPTERIS_SEEDS), 0, 2);

	static final BlockDrops fiddleheadDrop1 = new BlockDrops(GenesisItems.MATERIALS.getStack(EnumMaterial.ODONTOPTERIS_FIDDLEHEAD), 0, 1);
	static final BlockDrops fiddleheadDrop2To4 = new BlockDrops(GenesisItems.MATERIALS.getStack(EnumMaterial.ODONTOPTERIS_FIDDLEHEAD), 1, 1);

	@Override
	public ArrayList<ItemStack> getPlantDrops(BlockGrowingPlant plant, World world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> out = new ArrayList<>();
		int age = state.getValue(plant.ageProp);
		boolean top = state.getValue(plant.topProp);
		
		if (top)
		{
			if (age >= plant.maxAge)
			{
				out.addAll(seedsDropTopMature.getDrops(state, world.rand));
			}
			else
			{
				out.addAll(seedsDropTopBeforeMature.getDrops(state, world.rand));
			}
		}
		else
		{
			if (age >= 2)
			{
				out.addAll(seedsDropBottom2Up.getDrops(state, world.rand));
			}
			else
			{
				out.addAll(seedsDropBottom1.getDrops(state, world.rand));
			}

			if (age == 1)
			{
				out.addAll(fiddleheadDrop1.getDrops(state, world.rand));
			}
			if (age >= 2 && age <= 4)
			{
				out.addAll(fiddleheadDrop2To4.getDrops(state, world.rand));
			}
		}
		
		return out;
	}
}
