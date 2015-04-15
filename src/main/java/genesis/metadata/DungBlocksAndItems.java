package genesis.metadata;

import net.minecraftforge.common.util.EnumHelper;
import genesis.block.BlockDung;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.metadata.VariantsOfTypesCombo.ObjectType.ObjectNamePosition;

public class DungBlocksAndItems extends VariantsOfTypesCombo
{
	public static final ObjectType<BlockDung> DUNG_BLOCK = new ObjectType("dung_block", "dung", BlockDung.class, null)
			.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.PREFIX);
	public static final ObjectType<BlockDung> DUNG = new ObjectType("dung", null, null)
			.setCreativeTab(GenesisCreativeTabs.MATERIALS).setNamePosition(ObjectNamePosition.PREFIX);
	
	public DungBlocksAndItems()
	{
		super(new ObjectType[] {DUNG_BLOCK, DUNG}, EnumDung.values());
	}
}
