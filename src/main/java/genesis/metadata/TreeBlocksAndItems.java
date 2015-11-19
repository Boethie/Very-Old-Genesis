package genesis.metadata;

import genesis.block.*;
import genesis.common.GenesisCreativeTabs;
import genesis.item.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;

import java.util.*;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityFurnace;

public class TreeBlocksAndItems extends VariantsOfTypesCombo<EnumTree>
{
	public static final ObjectType<BlockGenesisLogs, ItemBlockMulti<EnumTree>> LOG = ObjectType.createBlock("log", BlockGenesisLogs.class);
	public static final ObjectType<BlockGenesisSaplings, ItemBlockMulti<EnumTree>> SAPLING = new ObjectType<BlockGenesisSaplings, ItemBlockMulti<EnumTree>>("sapling", BlockGenesisSaplings.class, null)
			{
				@Override
				public void afterConstructed(BlockGenesisSaplings block, ItemBlockMulti<EnumTree> item, List<? extends IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					FuelHandler.setBurnTime(item, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.sapling)), true);
				}
			}
			.setIgnoredProperties(BlockSapling.STAGE);
	public static final ObjectType<BlockGenesisLeaves, ItemBlockMulti<EnumTree>> LEAVES = new ObjectType<BlockGenesisLeaves, ItemBlockMulti<EnumTree>>("leaves", BlockGenesisLeaves.class, null)
			.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
	public static final ObjectType<Block, ItemMulti<EnumTree>> BILLET = new ObjectType<Block, ItemMulti<EnumTree>>("billet", Unlocalized.Section.MATERIAL + "billet", null, null, EnumTree.NO_BILLET)
			{
				@Override
				public void afterConstructed(Block block, ItemMulti<EnumTree> item, List<? extends IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					FuelHandler.setBurnTime(item, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.planks)), true);
				}
			};
	public static final ObjectType<BlockWattleFence, ItemBlockMulti<EnumTree>> WATTLE_FENCE = new ObjectType<BlockWattleFence, ItemBlockMulti<EnumTree>>("wattle_fence", "wattleFence", BlockWattleFence.class, null, EnumTree.NO_BILLET);
	public static final ObjectType<BlockGenesisRottenLogs, ItemBlockMulti<EnumTree>> ROTTEN_LOG = new ObjectType<BlockGenesisRottenLogs, ItemBlockMulti<EnumTree>>("rotten_log", "log.rotten", BlockGenesisRottenLogs.class, null, EnumTree.NO_ROTTEN)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	
	public static final ImmutableList<? extends ObjectType<?, ?>> TYPES = ImmutableList.of(LOG, SAPLING, LEAVES, BILLET, WATTLE_FENCE, ROTTEN_LOG);
	
	static
	{
		for (ObjectType<?, ?> type : TYPES)
		{
			type.setTypeNamePosition(TypeNamePosition.POSTFIX);
		}
	}
	
	public TreeBlocksAndItems()
	{
		super(TYPES, ImmutableList.copyOf(EnumTree.values()));
		
		setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
	}
}
