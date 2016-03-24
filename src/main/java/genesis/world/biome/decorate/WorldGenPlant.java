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
import genesis.common.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenPlant<V extends IPlantMetadata<V>> extends WorldGenDecorationBase implements PlantGenerator
{
	public static <V extends IPlantMetadata<V>> WorldGenPlant<V> create(VariantsOfTypesCombo<V> combo, ObjectType<? extends BlockPlant<V>, ? extends Item> type, V variant)
	{
		return new WorldGenPlant<V>(combo, type, variant);
	}
	
	public static WorldGenPlant<EnumPlant> create(ObjectType<? extends BlockPlant<EnumPlant>, ? extends Item> type, EnumPlant variant)
	{
		return create(GenesisBlocks.plants, type, variant);
	}
	
	public static WorldGenPlant<EnumPlant> create(EnumPlant variant)
	{
		ObjectType<? extends BlockPlant<EnumPlant>, ? extends Item> type = null;
		
		switch (variant.getType())
		{
		case PLANT:
			if (variant.hasLarge())
				type = PlantBlocks.DOUBLE_PLANT;
			else
				type = PlantBlocks.PLANT;
			break;
		case FERN:
			if (variant.hasLarge())
				type = PlantBlocks.DOUBLE_FERN;
			else
				type = PlantBlocks.FERN;
			break;
		}
		
		if (type == null)
			throw new IllegalArgumentException("Unexpected variant passed to create a new WorldGenPlant: " + variant);
		
		return create(type, variant);
	}
	
	protected final VariantsOfTypesCombo<V> combo;
	protected final ObjectType<? extends BlockPlant<V>, ? extends Item> type;
	protected final V variant;
	private final List<Block> allowedBlocks = new ArrayList<Block>();
	private boolean nextToWater = false;
	private int waterRadius = 1;
	private int waterHeight = 1;
	
	public WorldGenPlant(VariantsOfTypesCombo<V> combo, ObjectType<? extends BlockPlant<V>, ? extends Item> type, V variant)
	{
		this.combo = combo;
		this.type = type;
		this.variant = variant;
		
		this.allowedBlocks.add(Blocks.dirt);
		this.allowedBlocks.add(GenesisBlocks.moss);
	}
	
	public WorldGenPlant<V> addAllowedBlocks(Block... blocks)
	{
		for (int i = 0; i < blocks.length; ++i)
			allowedBlocks.add(blocks[i]);
		
		return this;
	}
	
	public WorldGenPlant<V> setNextToWater(boolean nextToWater)
	{
		this.nextToWater = nextToWater;
		return this;
	}
	
	public WorldGenPlant<V> setWaterProximity(int radius, int height)
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
		
		if (random.nextInt(rarity) != 0)
			return false;
		
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
		int additional = random.nextInt(getPatchSize() + 1);
		
		for (int i = 0; i < additional; ++i)
		{
			secondPos = pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
			
			if (allowedBlocks.contains(world.getBlockState(secondPos).getBlock()))
			{
				placePlant(world, secondPos.up(), random);
			}
		}
		
		return true;
	}
	
	@Override
	public void placePlant(World world, BlockPos pos, Random random)
	{
		combo.getBlock(type, variant).placeAt(world, pos, variant, 2);
	}
}
