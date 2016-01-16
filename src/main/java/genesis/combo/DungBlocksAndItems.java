package genesis.combo;

import java.util.*;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import genesis.block.*;
import genesis.combo.variant.EnumDung;
import genesis.combo.variant.IMetadata;
import genesis.common.*;
import genesis.item.*;
import genesis.util.Constants.Unlocalized;
import genesis.util.ReflectionUtils;

public class DungBlocksAndItems extends VariantsOfTypesCombo<EnumDung>
{
	private static final Class<BlockGenesisVariants<EnumDung>> DUNG_CLASS = ReflectionUtils.convertClass(BlockGenesisVariants.class);
	
	public static final ObjectType<BlockGenesisVariants<EnumDung>, ItemBlockMulti<EnumDung>> DUNG_BLOCK =
			new ObjectType<BlockGenesisVariants<EnumDung>, ItemBlockMulti<EnumDung>>("dung_block", "dung", DUNG_CLASS, null)
			{
				@Override
				public <V extends IMetadata<V>> void afterConstructed(BlockGenesisVariants<EnumDung> block, ItemBlockMulti<EnumDung> item, List<V> variants)
				{
					super.afterConstructed(block, item, variants);
					
					block.setHardness(0.5F);
					block.setStepSound(GenesisSounds.DUNG);
					
					block.clearDrops();
					block.addDrop(DUNG, 9, 9);
				}
			}.setUseSeparateVariantJsons(false).setBlockArguments(Material.ground);
	public static final ObjectType<Block, ItemDung> DUNG = new ObjectType<Block, ItemDung>("dung", Unlocalized.Section.MATERIAL + "dung", null, ItemDung.class)
			{
				@Override
				public <V extends IMetadata<V>> void afterConstructed(Block block, ItemDung item, List<V> variants)
				{
					super.afterConstructed(block, item, variants);
				}
			}
			.setCreativeTab(GenesisCreativeTabs.MATERIALS);
	
	public DungBlocksAndItems()
	{
		super(ImmutableList.of(DUNG_BLOCK, DUNG), EnumDung.class, ImmutableList.copyOf(EnumDung.values()));
		
		setUnlocalizedPrefix(Unlocalized.PREFIX);
	}
}
