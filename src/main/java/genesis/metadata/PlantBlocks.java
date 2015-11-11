package genesis.metadata;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.block.*;
import genesis.common.GenesisSounds;
import genesis.item.*;
import genesis.util.Constants.Unlocalized;

public class PlantBlocks extends VariantsOfTypesCombo<IPlantMetadata>
{
	// Plants
	public static final ObjectType<BlockPlant, ItemBlockMulti<IPlantMetadata>> PLANT = ObjectType.create("plant", Unlocalized.Section.PLANT, BlockPlant.class, null);
	public static final ObjectType<BlockGenesisDoublePlant, ItemBlockMulti<IPlantMetadata>> DOUBLE_PLANT = new ObjectType<BlockGenesisDoublePlant, ItemBlockMulti<IPlantMetadata>>("double_plant", Unlocalized.Section.PLANT_DOUBLE, BlockGenesisDoublePlant.class, null)
			{
				@Override
				public String getVariantName(IMetadata variant)
				{
					return "double_" + variant.getName();
				}
			};
	
	// Ferns
	public static final ObjectType<BlockPlant, ItemBlockMulti<IPlantMetadata>> FERN = new ObjectType<BlockPlant, ItemBlockMulti<IPlantMetadata>>("fern", Unlocalized.Section.FERN, BlockPlant.class, null)
			{
				@Override
				public void afterConstructed(BlockPlant block, ItemBlockMulti<IPlantMetadata> item, List<? extends IMetadata> variants)
				{
					afterFernConstructed(block, item, variants);
				}
			};
	public static final ObjectType<BlockGenesisDoublePlant, ItemBlockMulti<IPlantMetadata>> DOUBLE_FERN = new ObjectType<BlockGenesisDoublePlant, ItemBlockMulti<IPlantMetadata>>("double_fern", Unlocalized.Section.FERN_DOUBLE, BlockGenesisDoublePlant.class, null)
			{
				@Override
				public void afterConstructed(BlockGenesisDoublePlant block, ItemBlockMulti<IPlantMetadata> item, List<? extends IMetadata> variants)
				{
					afterFernConstructed(block, item, variants);
				}
				
				@Override
				public String getVariantName(IMetadata variant)
				{
					return "double_" + variant.getName();
				}
			};
	
	static
	{
		PLANT.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(EnumPlant.SINGLES);
		DOUBLE_PLANT.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(ImmutableList.copyOf(EnumPlant.DOUBLES));
		
		FERN.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(EnumFern.SINGLES);
		DOUBLE_FERN.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
				.setValidVariants(ImmutableList.copyOf(EnumFern.DOUBLES));
	}
	
	private static void afterFernConstructed(Block block, ItemBlockMulti<IPlantMetadata> item, List<? extends IMetadata> variants)
	{
		block.setStepSound(GenesisSounds.FERN);
	}
	
	public PlantBlocks()
	{
		super(ImmutableList.of(PLANT, DOUBLE_PLANT, FERN, DOUBLE_FERN), new ImmutableList.Builder<IPlantMetadata>().add(EnumPlant.values()).add(EnumFern.values()).build());
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
