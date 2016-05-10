package genesis.combo;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableList;

import genesis.block.*;
import genesis.combo.variant.EnumOre;
import genesis.item.*;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import genesis.util.ReflectionUtils;

public class OreBlocks extends VariantsOfTypesCombo<EnumOre>
{
	public static final ObjectType<EnumOre, BlockMultiOre<EnumOre>, ItemBlockMulti<EnumOre>> ORE =
			ObjectType.createBlock(EnumOre.class, "ore", ReflectionUtils.convertClass(BlockMultiOre.class));
	public static final ObjectType<EnumOre, Block, ItemMulti<EnumOre>> DROP =
			ObjectType.createItem(EnumOre.class, "ore_drop", Unlocalized.Section.MATERIAL);
	
	static
	{
		ORE.setVariantFilter((v) -> v.hasOre())
				.setTypeNamePosition(TypeNamePosition.POSTFIX);
		DROP.setVariantFilter((v) -> v.hasDrop())
				.setResourceName("").setTypeNamePosition(TypeNamePosition.POSTFIX);
	}
	
	public OreBlocks()
	{
		super("ores", ImmutableList.of(ORE, DROP),
				EnumOre.class, ImmutableList.copyOf(EnumOre.values()));
		
		EnumOre.setDrops(this);
		setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
	}
	
	public IBlockState getOreState(EnumOre ore)
	{
		return getBlockState(ORE, ore);
	}
	
	public ItemStack getOreStack(EnumOre ore)
	{
		return getStack(ORE, ore);
	}
	
	public ItemStack getDrop(EnumOre ore)
	{
		return getStack(DROP, ore);
	}
}
