package genesis.world.biome.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.block.BlockPlant;
import genesis.combo.PlantBlocks;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.VariantsOfTypesCombo.ObjectType;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.IPlantMetadata;
import genesis.common.GenesisBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"rawtypes", "unchecked"})
public class WorldGenPlant extends WorldGenDecorationBase
{
	protected final VariantsOfTypesCombo combo;
	protected final ObjectType<? extends BlockPlant, ? extends Item> type;
	protected final IPlantMetadata variant;
	private final List<Block> allowedBlocks = new ArrayList<Block>();
	private boolean nextToWater = false;
	private int waterRadius = 1;
	private int waterHeight = 1;
	
	public WorldGenPlant(VariantsOfTypesCombo combo, ObjectType<? extends BlockPlant, ? extends Item> type, IPlantMetadata variant)
	{
		this.combo = combo;
		this.type = type;
		this.variant = variant;
		
		this.allowedBlocks.add(Blocks.dirt);
		this.allowedBlocks.add(GenesisBlocks.moss);
	}
	
	public WorldGenPlant(EnumPlant variant)
	{	// TODO: This should be changed to make less assumptions. Use EnumPlant.getType(), SINGLES, DOUBLES, etc as necessary.
		this(GenesisBlocks.plants, variant.getType() == EnumPlant.PlantType.FERN ? PlantBlocks.DOUBLE_FERN : PlantBlocks.PLANT, variant);
	}
	
	public WorldGenPlant(ObjectType<? extends BlockPlant, ? extends Item> type, EnumPlant variant)
	{
		this(GenesisBlocks.plants, type, variant);
	}
	
	public WorldGenPlant addAllowedBlocks(Block... blocks)
	{
		for (int i = 0; i < blocks.length; ++i)
			allowedBlocks.add(blocks[i]);
		
		return this;
	}
	
	public WorldGenPlant setNextToWater(boolean nextToWater)
	{
		this.nextToWater = nextToWater;
		return this;
	}
	
	public WorldGenPlant setWaterProximity(int radius, int height)
	{
		this.waterRadius = radius;
		this.waterHeight = height;
		return this;
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
		
		if (!(allowedBlocks.contains(world.getBlockState(pos).getBlock())))
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		boolean water_exists = findBlockInRange(world, pos, Blocks.water.getDefaultState(), waterRadius, waterHeight, waterRadius);
		
		if (!water_exists && nextToWater)
			return false;
		
		placePlant(world, pos.up(), random);
		
		if (getPatchSize() == 1)
			return true;
		
		BlockPos secondPos;
		int additional = random.nextInt(getPatchSize() - 1);
		
		for (int i = 0; i <= additional; ++i)
		{
			secondPos = pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
			
			if (allowedBlocks.contains(world.getBlockState(secondPos).getBlock()))
				placePlant(world, secondPos, random);
		}
		
		return true;
	}
	
	protected void placePlant(World world, BlockPos pos, Random random)
	{
		((BlockPlant) combo.getBlock(type, variant)).placeAt(world, pos, variant, 2);
	}
}
