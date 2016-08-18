package genesis.combo;

import com.google.common.collect.ImmutableList;

import genesis.block.*;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumPlant.PlantType;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.item.*;
import genesis.util.ReflectionUtils;
import genesis.util.Constants.Unlocalized;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class PlantBlocks extends VariantsOfTypesCombo<EnumPlant>
{
	// Plants
	public static final ObjectType<EnumPlant, BlockPlant<EnumPlant>, ItemBlockMulti<EnumPlant>> PLANT;
	public static final ObjectType<EnumPlant, BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>> DOUBLE_PLANT;
	
	// Ferns
	public static final ObjectType<EnumPlant, BlockPlant<EnumPlant>, ItemBlockMulti<EnumPlant>> FERN;
	public static final ObjectType<EnumPlant, BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>> DOUBLE_FERN;
	
	static
	{
		Class<BlockPlant<EnumPlant>> singleClass = ReflectionUtils.convertClass(BlockPlant.class);
		Class<BlockGenesisDoublePlant<EnumPlant>> doubleClass = ReflectionUtils.convertClass(BlockGenesisDoublePlant.class);
		
		PLANT = ObjectType.createBlock(EnumPlant.class, "plant", Unlocalized.Section.PLANT, singleClass);
		DOUBLE_PLANT = ObjectType.createBlock(EnumPlant.class, "double_plant", Unlocalized.Section.PLANT_DOUBLE, doubleClass);
		
		PLANT.setBlockArguments(DOUBLE_PLANT, GenesisSoundTypes.PLANT)
				.setVariantFilter((v) -> v.getType() == PlantType.PLANT && v.hasSmall())
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE);
		
		DOUBLE_PLANT.setBlockArguments(GenesisSoundTypes.PLANT)
				.setVariantFilter((v) -> v.getType() == PlantType.PLANT && v.hasLarge())
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setVariantNameFunction((v) -> "double_" + v.getName());
		
		FERN = ObjectType.createBlock(EnumPlant.class, "fern", Unlocalized.Section.FERN, singleClass);
		DOUBLE_FERN = ObjectType.createBlock(EnumPlant.class, "double_fern", Unlocalized.Section.FERN_DOUBLE, doubleClass);
		
		FERN.setBlockArguments(DOUBLE_FERN, GenesisSoundTypes.FERN)
				.setVariantFilter((v) -> v.getType() == PlantType.FERN && v.hasSmall())
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE);
		
		DOUBLE_FERN.setBlockArguments(GenesisSoundTypes.FERN)
				.setVariantFilter((v) -> v.getType() == PlantType.FERN && v.hasLarge())
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setVariantNameFunction((v) -> "double_" + v.getName());
	}
	
	public PlantBlocks()
	{
		super("plants", ImmutableList.of(PLANT, DOUBLE_PLANT, FERN, DOUBLE_FERN),
				EnumPlant.class, ImmutableList.copyOf(EnumPlant.values()));
	}
	
	public ObjectType<EnumPlant, BlockPlant<EnumPlant>, ItemBlockMulti<EnumPlant>> getType(EnumPlant variant)
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
	
	public ObjectType<EnumPlant, BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>> getDoubleType(EnumPlant variant)
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
