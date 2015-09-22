package genesis.metadata;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.block.*;
import genesis.item.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants.Unlocalized;

@SuppressWarnings("rawtypes")
public class PlantBlocks extends VariantsOfTypesCombo<ObjectType<?, ?>, EnumPlant>
{
	public static final ObjectType<BlockPlant, ItemBlockMulti> PLANT = new ObjectType<BlockPlant, ItemBlockMulti>("plant", Unlocalized.Section.PLANT, BlockPlant.class, ItemBlockMulti.class, EnumPlant.NO_SINGLES)
			.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE);
	public static final ObjectType<BlockGenesisDoublePlant, ItemBlockMulti> DOUBLE_PLANT = new ObjectType<BlockGenesisDoublePlant, ItemBlockMulti>("double_plant", Unlocalized.Section.PLANT_DOUBLE, BlockGenesisDoublePlant.class, ItemBlockMulti.class)
	{
		@Override
		public String getVariantName(IMetadata variant)
		{
			return "double_" + variant.getName();
		}
	}.setValidVariants(ImmutableList.copyOf(EnumPlant.DOUBLES)).setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE);
	
	public PlantBlocks()
	{
		super(new ObjectType[]{PLANT, DOUBLE_PLANT}, EnumPlant.values());
	}
	
	public ItemStack getPlantStack(EnumPlant variant, int stackSize)
	{
		return getStack(PLANT, variant, stackSize);
	}
	
	public ItemStack getPlantStack(EnumPlant variant)
	{
		return getStack(PLANT, variant, 1);
	}
	
	public ItemStack getDoublePlantStack(EnumPlant variant, int stackSize)
	{
		return getStack(DOUBLE_PLANT, variant, stackSize);
	}
	
	public ItemStack getDoublePlantStack(EnumPlant variant)
	{
		return getStack(DOUBLE_PLANT, variant, 1);
	}
	
	public BlockPlant getPlantBlock(EnumPlant variant)
	{
		return getBlock(PLANT, variant);
	}
	
	public BlockGenesisDoublePlant getDoublePlantBlock(EnumPlant variant)
	{
		return getBlock(DOUBLE_PLANT, variant);
	}
	
	public IBlockState getPlantBlockState(EnumPlant variant)
	{
		return getBlockState(PLANT, variant);
	}
	
	public IBlockState getDoublePlantBlockState(EnumPlant variant)
	{
		return getBlockState(DOUBLE_PLANT, variant);
	}
}
