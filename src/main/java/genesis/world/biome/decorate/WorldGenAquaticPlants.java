package genesis.world.biome.decorate;

import genesis.combo.variant.EnumAquaticPlant;
import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenAquaticPlants extends WorldGenDecorationBase
{
	private EnumAquaticPlant plantType;
	private EnumAquaticPlant plantTypeTop;
	private boolean generateInGroup = false;
	private int groupSize = 1;
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isAir(world, pos) && !(block == Blocks.water))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		if (plantType == null)
			plantType = EnumAquaticPlant.BANGIOMORPHA;
		
		IBlockState plant = GenesisBlocks.aquatic_plants.getBlockState(plantType);
		
		if (!canPlantBePlaced(world, pos, plant))
			return false;
		
		setBlockInWorld(world, pos.up(), plant, true);
		if (plantTypeTop != null)
			setBlockInWorld(world, pos.up().up(), GenesisBlocks.aquatic_plants.getBlockState(plantTypeTop), true);
		
		BlockPos additionalPos;
		
		if (generateInGroup)
		{
			for (int i = 1; i <= groupSize - 1; ++i)
			{
				additionalPos = pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
				if (canPlantBePlaced(world, additionalPos, plant))
				{
					setBlockInWorld(world, additionalPos.up(), plant, true);
					if (plantTypeTop != null)
						setBlockInWorld(world, additionalPos.up().up(), GenesisBlocks.aquatic_plants.getBlockState(plantTypeTop), true);
				}
			}
		}
		
		return true;
	}
	
	private boolean canPlantBePlaced(World world, BlockPos pos, IBlockState state)
	{
		if (!GenesisBlocks.aquatic_plants.getBlock(plantType).canBlockStay(world, pos.up(), state))
			return false;
		
		if (
				!(world.getBlockState(pos.up()).getBlock() == Blocks.water)
				&& !(world.getBlockState(pos.up().up()).getBlock() == Blocks.water))
			return false;
		
		if (
				plantTypeTop != null
				&& !(world.getBlockState(pos.up().up().up()).getBlock() == Blocks.water))
			return false;
		
		return true;
	}
	
	public WorldGenDecorationBase setPlantType(EnumAquaticPlant type)
	{
		return setPlantType(type, null);
	}
	
	public WorldGenDecorationBase setPlantType(EnumAquaticPlant type, EnumAquaticPlant typeTop)
	{
		plantType = type;
		plantTypeTop = typeTop;
		return this;
	}
	
	public WorldGenAquaticPlants setGenerateInGroup(boolean group, int size)
	{
		generateInGroup = group;
		groupSize = size;
		return this;
	}
}
