package genesis.metadata;

import java.util.List;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.block.*;
import genesis.common.GenesisBlocks;
import genesis.item.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import genesis.util.random.drops.BlockDrops;
import genesis.util.random.drops.VariantDrop;

@SuppressWarnings("rawtypes")
public class GraniteOreBlocks extends VariantsOfTypesCombo<ObjectType, EnumGraniteOre>
{
	public static final ObjectType<BlockMultiOre, ItemBlockMulti> ORE = new ObjectType<BlockMultiOre, ItemBlockMulti>("granite_ore", "ore", BlockMultiOre.class, ItemBlockMulti.class)
			.setResourceName("ore");
	public static final ObjectType<Block, ItemMulti> DROP = new ObjectType<Block, ItemMulti>("granite_ore_drop", Unlocalized.Section.MATERIAL, null, ItemMulti.class)
			.setResourceName("");
	
	public GraniteOreBlocks()
	{
		super(new ObjectType[]{ORE, DROP}, EnumGraniteOre.values());
		EnumGraniteOre.setDrops(this);

		setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
	}
	
	public IBlockState getOreState(EnumGraniteOre ore)
	{
		return getBlockState(ORE, ore);
	}
	
	public ItemStack getOreStack(EnumGraniteOre ore)
	{
		return getStack(ORE, ore);
	}
	
	public ItemStack getDrop(EnumGraniteOre ore)
	{
		return getStack(DROP, ore);
	}
}
