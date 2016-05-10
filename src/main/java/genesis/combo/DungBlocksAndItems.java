package genesis.combo;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;

import genesis.block.*;
import genesis.combo.variant.EnumDung;
import genesis.common.*;
import genesis.item.*;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;

public class DungBlocksAndItems extends VariantsOfTypesCombo<EnumDung>
{
	public static final ObjectType<EnumDung, BlockDung, ItemBlockMulti<EnumDung>> DUNG_BLOCK;
	public static final ObjectType<EnumDung, Block, ItemDung> DUNG;
	
	static
	{
		DUNG_BLOCK = ObjectType.createBlock(EnumDung.class, "dung_block", "dung", BlockDung.class);
		DUNG = ObjectType.createItem(EnumDung.class, "dung", Unlocalized.Section.MATERIAL + "dung", ItemDung.class);
		
		DUNG_BLOCK.setUseSeparateVariantJsons(false)
				.setConstructedFunction((b, i) -> b.clearDrops().addDrop(DUNG, 9, 9).setHardness(0.5F));
		DUNG.setCreativeTab(GenesisCreativeTabs.MATERIALS);
	}
	
	public DungBlocksAndItems()
	{
		super("dungs", ImmutableList.of(DUNG_BLOCK, DUNG),
				EnumDung.class, ImmutableList.copyOf(EnumDung.values()));
		
		setNames(Constants.MOD_ID, Unlocalized.PREFIX);
	}
}
