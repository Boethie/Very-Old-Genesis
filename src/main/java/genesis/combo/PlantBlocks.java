package genesis.combo;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.block.*;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.IMetadata;
import genesis.common.GenesisSounds;
import genesis.item.*;
import genesis.util.ReflectionUtils;
import genesis.util.Constants.Unlocalized;

public class PlantBlocks extends VariantsOfTypesCombo<EnumPlant>
{
	private static final Class<BlockPlant<EnumPlant>> SINGLE = ReflectionUtils.convertClass(BlockPlant.class);
	private static final Class<BlockGenesisDoublePlant<EnumPlant>> DOUBLE = ReflectionUtils.convertClass(BlockGenesisDoublePlant.class);
	
	// Plants
	public static final ObjectType<BlockPlant<EnumPlant>, ItemBlockMulti<EnumPlant>> PLANT = ObjectType.create("plant", Unlocalized.Section.PLANT, SINGLE, null);
	public static final ObjectType<BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>> DOUBLE_PLANT = new ObjectType<BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>>("double_plant", Unlocalized.Section.PLANT_DOUBLE, DOUBLE, null)
	{
		@Override
		public String getVariantName(IMetadata<?> variant)
		{
			return "double_" + variant.getName();
		}
	};
	
	// Ferns
	private static void afterFernConstructed(Block block, ItemBlockMulti<EnumPlant> item, List<? extends IMetadata<?>> variants)
	{
		block.setStepSound(GenesisSounds.FERN);
	}
	
	public static final ObjectType<BlockPlant<EnumPlant>, ItemBlockMulti<EnumPlant>> FERN = new ObjectType<BlockPlant<EnumPlant>, ItemBlockMulti<EnumPlant>>("fern", Unlocalized.Section.FERN, SINGLE, null)
	{
		@Override
		public  <V extends IMetadata<V>> void afterConstructed(BlockPlant<EnumPlant> block, ItemBlockMulti<EnumPlant> item, List<V> variants)
		{
			afterFernConstructed(block, item, variants);
		}
	};
	public static final ObjectType<BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>> DOUBLE_FERN = new ObjectType<BlockGenesisDoublePlant<EnumPlant>, ItemBlockMulti<EnumPlant>>("double_fern", Unlocalized.Section.FERN_DOUBLE, DOUBLE, null)
	{
		@Override
		public  <V extends IMetadata<V>> void afterConstructed(BlockGenesisDoublePlant<EnumPlant> block, ItemBlockMulti<EnumPlant> item, List<V> variants)
		{
			afterFernConstructed(block, item, variants);
		}
		
		@Override
		public String getVariantName(IMetadata<?> variant)
		{
			return "double_" + variant.getName();
		}
	}.setValidVariants(EnumPlant.FERNS);
	
	static
	{
		PLANT.setBlockArguments(DOUBLE_PLANT)
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(Sets.intersection(EnumPlant.PLANTS, EnumPlant.SINGLES));
		DOUBLE_PLANT.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(Sets.intersection(EnumPlant.PLANTS, EnumPlant.DOUBLES));
		
		FERN.setBlockArguments(DOUBLE_FERN)
				.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(Sets.intersection(EnumPlant.FERNS, EnumPlant.SINGLES));
		DOUBLE_FERN.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(Sets.intersection(EnumPlant.FERNS, EnumPlant.DOUBLES));
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
