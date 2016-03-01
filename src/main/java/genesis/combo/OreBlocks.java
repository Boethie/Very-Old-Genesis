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
	public static final ObjectType<BlockMultiOre<EnumOre>, ItemBlockMulti<EnumOre>> ORE = ObjectType.createBlock("ore", ReflectionUtils.<BlockMultiOre<EnumOre>>convertClass(BlockMultiOre.class), EnumOre.NO_ORES);
	public static final ObjectType<Block, ItemMulti<EnumOre>> DROP = ObjectType.createItem("ore_drop", Unlocalized.Section.MATERIAL, EnumOre.NO_DROPS);
	
	static
	{
		ORE.setTypeNamePosition(TypeNamePosition.POSTFIX);
		DROP.setResourceName("").setTypeNamePosition(TypeNamePosition.POSTFIX);
	}
	
	public OreBlocks()
	{
		super(ImmutableList.of(ORE, DROP), EnumOre.class, ImmutableList.copyOf(EnumOre.values()));
		
		EnumOre.setDrops(this);
		setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
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
