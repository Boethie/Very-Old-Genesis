package genesis.world.biome.decorate;

import genesis.block.BlockPlant;
import genesis.common.GenesisBlocks;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"rawtypes", "unchecked"})
public class WorldGenPlant extends WorldGenDecorationBase
{
	protected final VariantsOfTypesCombo combo;
	protected final ObjectType<? extends BlockPlant, ? extends Item> type;
	protected final IMetadata variant;
	
	public WorldGenPlant(VariantsOfTypesCombo combo, ObjectType<? extends BlockPlant, ? extends Item> type, IMetadata variant)
	{
		this.combo = combo;
		this.type = type;
		this.variant = variant;
	}
	
	public WorldGenPlant(EnumPlant variant)
	{
		this(GenesisBlocks.plants, PlantBlocks.PLANT, variant);
	}
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isAir(world, pos) && !block.isLeaves(world, pos))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		placePlant(world, pos.up(), random);
		
		if (getPatchSize() == 1)
			return true;
		
		BlockPos secondPos;
		int additional = random.nextInt(getPatchSize() - 1);
		
		for (int i = 0; i <= additional; ++i)
		{
			secondPos = pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
			if (
					(world.getBlockState(secondPos).getBlock() == GenesisBlocks.moss
					|| world.getBlockState(secondPos).getBlock() == Blocks.dirt))
				placePlant(world, secondPos, random);
		}
		
		return true;
	}
	
	protected void placePlant(World world, BlockPos pos, Random random)
	{
		((BlockPlant) combo.getBlock(type, variant)).placeAt(world, pos, variant, 2);
		
		/*BlockPos placePos = pos.up();
		
		if (world.isAirBlock(placePos) && world.isAirBlock(placePos.up()))
		{
			world.setBlockState(placePos, plant, 2);
			
			if (isDouble)
				world.setBlockState(placePos.up(), plant, 2);
		}*/
	}
}
