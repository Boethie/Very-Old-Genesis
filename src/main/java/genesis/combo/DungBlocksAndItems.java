package genesis.combo;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;

import genesis.block.*;
import genesis.combo.variant.EnumDung;
import genesis.common.*;
import genesis.item.*;
import genesis.util.Constants.Unlocalized;

public class DungBlocksAndItems extends VariantsOfTypesCombo<EnumDung>
{
	private static final Class<BlockDung> DUNG_CLASS = BlockDung.class;
	
	public static final ObjectType<BlockDung, ItemBlockMulti<EnumDung>> DUNG_BLOCK =
			new ObjectType<BlockDung, ItemBlockMulti<EnumDung>>("dung_block", "dung", DUNG_CLASS, null)
					.setUseSeparateVariantJsons(false);
	public static final ObjectType<Block, ItemDung> DUNG =
			new ObjectType<Block, ItemDung>("dung", Unlocalized.Section.MATERIAL + "dung", null, ItemDung.class)
					.setCreativeTab(GenesisCreativeTabs.MATERIALS);
	
	static
	{
		DUNG_BLOCK.setConstructedFunction((b, i) -> b.clearDrops().addDrop(DUNG, 9, 9).setHardness(0.5F));
	}
	
	public DungBlocksAndItems()
	{
		super(ImmutableList.of(DUNG_BLOCK, DUNG), EnumDung.class, ImmutableList.copyOf(EnumDung.values()));
		
		setUnlocalizedPrefix(Unlocalized.PREFIX);
	}
}
