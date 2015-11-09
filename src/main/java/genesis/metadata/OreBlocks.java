package genesis.metadata;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableList;

import genesis.block.*;
import genesis.item.*;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;

public class OreBlocks extends VariantsOfTypesCombo<EnumOre>
{
	public static final ObjectType<BlockMultiOre<EnumOre>, ItemBlockMulti<EnumOre>> ORE = ObjectType.create("ore", BlockMultiOre.class, ItemBlockMulti.class);
	public static final ObjectType<Block, ItemMulti<EnumOre>> DROP = ObjectType.createItem("ore_drop", Unlocalized.Section.MATERIAL, EnumOre.noDrops);
	
	static
	{
		ORE.setTypeNamePosition(TypeNamePosition.POSTFIX);
		DROP.setResourceName("").setTypeNamePosition(TypeNamePosition.POSTFIX);
	}
	
	public OreBlocks()
	{
		super(ImmutableList.of(ORE, DROP), ImmutableList.copyOf(EnumOre.values()));
		
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
