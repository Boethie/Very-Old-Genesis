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
	public static final ObjectType<EnumDung, BlockDung, ItemBlockDung> DUNG_BLOCK;
	public static final ObjectType<EnumDung, Block, ItemDung> DUNG;
	
	static
	{
		DUNG_BLOCK = ObjectType.create(EnumDung.class, "dung_block", "dung", BlockDung.class, ItemBlockDung.class);
		DUNG = ObjectType.createItem(EnumDung.class, "dung", Unlocalized.Section.MATERIAL + "dung", ItemDung.class);
		
		DUNG_BLOCK.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
				.setUseSeparateVariantJsons(false).setShouldRegisterVariantModels(false);
		DUNG.setCreativeTab(GenesisCreativeTabs.MATERIALS);
	}
	
	public DungBlocksAndItems()
	{
		super("dungs", ImmutableList.of(DUNG_BLOCK, DUNG),
				EnumDung.class, ImmutableList.copyOf(EnumDung.values()));
		
		setNames(Constants.MOD_ID, Unlocalized.PREFIX);
	}
}
