package genesis.metadata;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.block.*;
import genesis.common.GenesisSounds;
import genesis.item.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants.Unlocalized;

public class PlantBlocks extends VariantsOfTypesCombo<ObjectType<?, ?>, IPlantMetadata>
{
	public static final ObjectType<BlockPlant, ItemBlockMulti> PLANT = new ObjectType<BlockPlant, ItemBlockMulti>("plant", Unlocalized.Section.PLANT, BlockPlant.class, ItemBlockMulti.class)
			.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE)
			.setValidVariants(EnumPlant.SINGLES);
	public static final ObjectType<BlockGenesisDoublePlant, ItemBlockMulti> DOUBLE_PLANT = new ObjectType<BlockGenesisDoublePlant, ItemBlockMulti>("double_plant", Unlocalized.Section.PLANT_DOUBLE, BlockGenesisDoublePlant.class, ItemBlockMulti.class)
			{
				@Override
				public String getVariantName(IMetadata variant)
				{
					return "double_" + variant.getName();
				}
			}
			.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE)
			.setValidVariants(ImmutableList.copyOf(EnumPlant.DOUBLES));
	
	private static void afterFernConstructed(Block block, ItemBlockMulti item, List<? extends IMetadata> variants)
	{
		block.setStepSound(GenesisSounds.FERN);
	}
	
	public static final ObjectType<BlockPlant, ItemBlockMulti> FERN = new ObjectType<BlockPlant, ItemBlockMulti>("fern", Unlocalized.Section.FERN, BlockPlant.class, ItemBlockMulti.class)
			{
				@Override
				public void afterConstructed(BlockPlant block, ItemBlockMulti item, List<? extends IMetadata> variants)
				{
					afterFernConstructed(block, item, variants);
				}
			}
			.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE)
			.setValidVariants(EnumFern.SINGLES);
	public static final ObjectType<BlockGenesisDoublePlant, ItemBlockMulti> DOUBLE_FERN = new ObjectType<BlockGenesisDoublePlant, ItemBlockMulti>("double_fern", Unlocalized.Section.FERN_DOUBLE, BlockGenesisDoublePlant.class, ItemBlockMulti.class)
			{
				@Override
				public void afterConstructed(BlockGenesisDoublePlant block, ItemBlockMulti item, List<? extends IMetadata> variants)
				{
					afterFernConstructed(block, item, variants);
				}
				
				@Override
				public String getVariantName(IMetadata variant)
				{
					return "double_" + variant.getName();
				}
			}
			.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE)
			.setValidVariants(ImmutableList.copyOf(EnumFern.DOUBLES));
	
	public PlantBlocks()
	{
		super(new ImmutableList.Builder<ObjectType<?, ?>>().add(PLANT, DOUBLE_PLANT, FERN, DOUBLE_FERN).build(), new ImmutableList.Builder<IPlantMetadata>().add(EnumPlant.values()).add(EnumFern.values()).build());
	}
	
	// -------- Plants --------
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
	
	// -------- Ferns --------
	public ItemStack getFernStack(EnumFern variant, int stackSize)
	{
		return getStack(FERN, variant, stackSize);
	}
	
	public ItemStack getFernStack(EnumFern variant)
	{
		return getStack(FERN, variant, 1);
	}
	
	public ItemStack getDoubleFernStack(EnumFern variant, int stackSize)
	{
		return getStack(DOUBLE_FERN, variant, stackSize);
	}
	
	public ItemStack getDoubleFernStack(EnumFern variant)
	{
		return getStack(DOUBLE_FERN, variant, 1);
	}
	
	public BlockPlant getFernBlock(EnumFern variant)
	{
		return getBlock(FERN, variant);
	}
	
	public BlockGenesisDoublePlant getDoubleFernBlock(EnumFern variant)
	{
		return getBlock(DOUBLE_FERN, variant);
	}
	
	public IBlockState getFernBlockState(EnumFern variant)
	{
		return getBlockState(FERN, variant);
	}
	
	public IBlockState getDoubleFernBlockState(EnumFern variant)
	{
		return getBlockState(DOUBLE_FERN, variant);
	}
}
