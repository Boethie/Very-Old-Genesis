package genesis.metadata;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.block.*;
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
			}
			.setValidVariants(EnumPlant.FERNS);
	
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
	
	private static void afterFernConstructed(Block block, ItemBlockMulti<EnumPlant> item, List<? extends IMetadata<?>> variants)
	{
		block.setStepSound(GenesisSounds.FERN);
	}
	
	public PlantBlocks()
	{
		super(ImmutableList.of(PLANT, DOUBLE_PLANT, FERN, DOUBLE_FERN), EnumPlant.class, ImmutableList.copyOf(EnumPlant.values()));
	}
	
	// -------- Plants --------
	public ItemStack getPlantStack(EnumPlant variant, int stackSize)
	{
		return !variant.isFern() ? getStack(PLANT, variant, stackSize) : getStack(FERN, variant, stackSize);
	}
	
	public ItemStack getPlantStack(EnumPlant variant)
	{
		return getPlantStack(variant, 1);
	}
	
	public ItemStack getDoublePlantStack(EnumPlant variant, int stackSize)
	{
		return !variant.isFern() ? getStack(DOUBLE_PLANT, variant, stackSize) : getStack(DOUBLE_FERN, variant, stackSize);
	}
	
	public ItemStack getDoublePlantStack(EnumPlant variant)
	{
		return getDoublePlantStack(variant, 1);
	}
	
	public BlockPlant<EnumPlant> getPlantBlock(EnumPlant variant)
	{
		return !variant.isFern() ? getBlock(PLANT, variant) : getBlock(FERN, variant);
	}
	
	public BlockGenesisDoublePlant<EnumPlant> getDoublePlantBlock(EnumPlant variant)
	{
		return !variant.isFern() ? getBlock(DOUBLE_PLANT, variant) : getBlock(DOUBLE_FERN, variant);
	}
	
	public IBlockState getPlantBlockState(EnumPlant variant)
	{
		return !variant.isFern() ? getBlockState(PLANT, variant) : getBlockState(FERN, variant);
	}
	
	public IBlockState getDoublePlantBlockState(EnumPlant variant)
	{
		return !variant.isFern() ? getBlockState(DOUBLE_PLANT, variant) : getBlockState(DOUBLE_FERN, variant);
	}
}
