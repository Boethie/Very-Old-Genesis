package genesis.world.biome.decorate;

import java.util.Random;

import genesis.block.BlockPlant;
import genesis.combo.ObjectType;
import genesis.combo.PlantBlocks;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.IPlantMetadata;
import genesis.common.*;
import genesis.util.WorldUtils;
import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenPlant<V extends IPlantMetadata<V>> extends WorldGenDecorationBase
{
	public static <V extends IPlantMetadata<V>> WorldGenPlant<V> create(VariantsOfTypesCombo<V> combo, ObjectType<V, ? extends BlockPlant<V>, ? extends Item> type, V variant)
	{
		return new WorldGenPlant<V>(combo, type, variant);
	}
	
	public static WorldGenPlant<EnumPlant> create(ObjectType<EnumPlant, ? extends BlockPlant<EnumPlant>, ? extends Item> type, EnumPlant variant)
	{
		return create(GenesisBlocks.plants, type, variant);
	}
	
	public static WorldGenPlant<EnumPlant> create(EnumPlant variant)
	{
		ObjectType<EnumPlant, ? extends BlockPlant<EnumPlant>, ? extends Item> type = null;
		
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
	protected final ObjectType<V, ? extends BlockPlant<V>, ? extends Item> type;
	protected final V variant;
	private boolean nextToWater = false;
	private int waterRadius = 1;
	private int waterHeight = 1;
	
	public WorldGenPlant(VariantsOfTypesCombo<V> combo, ObjectType<V, ? extends BlockPlant<V>, ? extends Item> type, V variant)
	{
		super(WorldBlockMatcher.STANDARD_AIR, WorldBlockMatcher.TRUE);
		
		this.combo = combo;
		this.type = type;
		this.variant = variant;
		
		setPatchRadius(9);
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
	public boolean place(World world, Random random, BlockPos pos)
	{
		if (nextToWater && !WorldUtils.waterInRange(world, pos, waterRadius, waterHeight))
			return false;
		
		return combo.getBlock(type, variant).placeAt(world, pos, variant, 2);
	}
}
