package genesis.combo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import genesis.block.*;
import genesis.combo.variant.EnumPlant;
import genesis.item.*;
import genesis.util.ReflectionUtils;
import genesis.util.Constants.Unlocalized;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class PlantBlocks extends VariantsOfTypesCombo<EnumPlant>
{
	// Plants
	public static final ObjectType<BlockPlant<EnumPlant>, ItemBlockMulti<EnumPlant>> PLANT;
	public static final ObjectType<BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>> DOUBLE_PLANT;
	
	// Ferns
	public static final ObjectType<BlockPlant<EnumPlant>, ItemBlockMulti<EnumPlant>> FERN;
	public static final ObjectType<BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>> DOUBLE_FERN;
	
	static
	{
		Class<BlockPlant<EnumPlant>> singleClass = ReflectionUtils.convertClass(BlockPlant.class);
		Class<BlockGenesisDoublePlant<EnumPlant>> doubleClass = ReflectionUtils.convertClass(BlockGenesisDoublePlant.class);
		
		PLANT = new ObjectType<>("plant", Unlocalized.Section.PLANT, singleClass, null);
		DOUBLE_PLANT = new ObjectType<>("double_plant", Unlocalized.Section.PLANT_DOUBLE, doubleClass, null);
		PLANT.setBlockArguments(DOUBLE_PLANT, SoundType.PLANT)
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(Sets.intersection(EnumPlant.PLANTS, EnumPlant.SINGLES));
		DOUBLE_PLANT.setBlockArguments(SoundType.PLANT)
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(Sets.intersection(EnumPlant.PLANTS, EnumPlant.DOUBLES))
				.setVariantNameFunction((v) -> "double_" + v.getName());
		
		FERN = new ObjectType<>("fern", Unlocalized.Section.FERN, singleClass, null);
		DOUBLE_FERN = new ObjectType<>("double_fern", Unlocalized.Section.FERN_DOUBLE, doubleClass, null);
		FERN.setBlockArguments(DOUBLE_FERN, SoundType.PLANT)
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(Sets.intersection(EnumPlant.FERNS, EnumPlant.SINGLES));
		DOUBLE_FERN.setBlockArguments(SoundType.PLANT)
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(Sets.intersection(EnumPlant.FERNS, EnumPlant.DOUBLES))
				.setVariantNameFunction((v) -> "double_" + v.getName());
	}
	
	public PlantBlocks()
	{
		super(ImmutableList.of(PLANT, DOUBLE_PLANT, FERN, DOUBLE_FERN), EnumPlant.class, ImmutableList.copyOf(EnumPlant.values()));
	}
	
	public ObjectType<BlockPlant<EnumPlant>, ItemBlockMulti<EnumPlant>> getType(EnumPlant variant)
	{
		switch (variant.getType())
		{
		case PLANT:
			return PLANT;
		case FERN:
			return FERN;
		}
		
		return null;
	}
	
	public ObjectType<BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>> getDoubleType(EnumPlant variant)
	{
		switch (variant.getType())
		{
		case PLANT:
			return DOUBLE_PLANT;
		case FERN:
			return DOUBLE_FERN;
		}
		
		return null;
	}
	
	public ItemStack getPlantStack(EnumPlant variant, int stackSize)
	{
		return getStack(getType(variant), variant, stackSize);
	}
	
	public ItemStack getPlantStack(EnumPlant variant)
	{
		return getPlantStack(variant, 1);
	}
	
	public ItemStack getDoublePlantStack(EnumPlant variant, int stackSize)
	{
		return getStack(getDoubleType(variant), variant, stackSize);
	}
	
	public ItemStack getDoublePlantStack(EnumPlant variant)
	{
		return getDoublePlantStack(variant, 1);
	}
	
	public BlockPlant<EnumPlant> getPlantBlock(EnumPlant variant)
	{
		return getBlock(getType(variant), variant);
	}
	
	public BlockGenesisDoublePlant<EnumPlant> getDoublePlantBlock(EnumPlant variant)
	{
		return getBlock(getDoubleType(variant), variant);
	}
	
	public IBlockState getPlantBlockState(EnumPlant variant)
	{
		return getBlockState(getType(variant), variant);
	}
	
	public IBlockState getDoublePlantBlockState(EnumPlant variant)
	{
		return getBlockState(getDoubleType(variant), variant);
	}
}
