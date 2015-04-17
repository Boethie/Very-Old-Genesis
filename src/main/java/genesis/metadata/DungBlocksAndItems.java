package genesis.metadata;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import genesis.block.*;
import genesis.client.GenesisSounds;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.metadata.VariantsOfTypesCombo.ObjectType.ObjectNamePosition;

public class DungBlocksAndItems extends VariantsOfTypesCombo
{
	public static final ObjectType<BlockGenesisVariants> DUNG_BLOCK = new ObjectType<BlockGenesisVariants>("dung_block", "dung", BlockGenesisVariants.class, null)
			{
				@Override
				public void afterConstructed(Block block, Item item, List<IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					
					BlockGenesisVariants dung = (BlockGenesisVariants) block;
					
					dung.setHardness(0.5F);
					dung.setStepSound(GenesisSounds.DUNG);
					
					dung.clearDrops();
					dung.addDrop(GenesisBlocks.dungs.DUNG, 4, 4);
					
					Blocks.fire.setFireInfo(dung, 5, 5);
				}
			}.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.PREFIX).setBlockArguments(Material.ground);
	public static final ObjectType<ItemBlockMulti> DUNG = new ObjectType("dung", null, null)
			.setCreativeTab(GenesisCreativeTabs.MATERIALS).setNamePosition(ObjectNamePosition.PREFIX);
	
	public DungBlocksAndItems()
	{
		super(new ObjectType[] {DUNG_BLOCK, DUNG}, EnumDung.values());
	}
}
