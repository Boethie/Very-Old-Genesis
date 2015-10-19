package genesis.metadata;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.block.*;
import genesis.item.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;

@SuppressWarnings("rawtypes")
public class OreBlocks extends VariantsOfTypesCombo<ObjectType<?, ?>, EnumOre>
{
	public static final ObjectType<BlockMultiOre, ItemBlockMulti> ORE = new ObjectType<BlockMultiOre, ItemBlockMulti>("ore", BlockMultiOre.class, ItemBlockMulti.class);
	public static final ObjectType<Block, ItemMulti> DROP = new ObjectType<Block, ItemMulti>("ore_drop", Unlocalized.Section.MATERIAL, null, ItemMulti.class, EnumOre.noDrops)
			.setResourceName("");
	
	public OreBlocks()
	{
		super(new ObjectType[]{ORE, DROP}, EnumOre.values());
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
