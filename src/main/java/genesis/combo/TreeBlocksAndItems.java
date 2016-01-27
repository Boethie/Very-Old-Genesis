package genesis.combo;

import genesis.block.*;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.IMetadata;
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
	public static final ObjectType<BlockGenesisSaplings, ItemBlockMulti<EnumTree>> SAPLING =
			new ObjectType<BlockGenesisSaplings, ItemBlockMulti<EnumTree>>("sapling", BlockGenesisSaplings.class, null)
			{
				@Override
				public <V extends IMetadata<V>> void afterConstructed(BlockGenesisSaplings block, ItemBlockMulti<EnumTree> item, List<V> variants)
				{
					super.afterConstructed(block, item, variants);
					FuelHandler.setBurnTime(item, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.sapling)), true);
				}
			}.setIgnoredProperties(BlockSapling.STAGE);
	public static final ObjectType<BlockGenesisLeaves, ItemBlockMulti<EnumTree>> LEAVES =
			new ObjectType<BlockGenesisLeaves, ItemBlockMulti<EnumTree>>("leaves", BlockGenesisLeaves.class, null)
					.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
	public static final ObjectType<BlockGenesisLeavesFruit, ItemBlockMulti<EnumTree>> LEAVES_FRUIT =
			new ObjectType<BlockGenesisLeavesFruit, ItemBlockMulti<EnumTree>>("leaves_fruit", "leaves.fruit", BlockGenesisLeavesFruit.class, null)
					.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE)
					.setValidVariants(EnumTree.FRUIT_LEAVES);
	public static final ObjectType<BlockHangingFruit, Item> HANGING_FRUIT =
			ObjectType.create("hanging_fruit", "hanging.fruit", BlockHangingFruit.class, null)
					.setValidVariants(EnumTree.FRUIT_HANGING);
	public static final ObjectType<Block, ItemGenesisFood<EnumTree>> FRUIT =
			ObjectType.createItem("fruit", Unlocalized.Section.FOOD + "fruit", ReflectionUtils.<ItemGenesisFood<EnumTree>>convertClass(ItemGenesisFood.class))
					.setValidVariants(EnumTree.FRUIT_ITEMS);
	public static final ObjectType<Block, ItemMulti<EnumTree>> BILLET =
			new ObjectType<Block, ItemMulti<EnumTree>>("billet", Unlocalized.Section.MATERIAL + "billet", null, null, EnumTree.NO_BILLET)
			{
				@Override
				public <V extends IMetadata<V>> void afterConstructed(Block block, ItemMulti<EnumTree> item, List<V> variants)
				{
					super.afterConstructed(block, item, variants);
					FuelHandler.setBurnTime(item, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.planks)), true);
				}
			};
	public static final ObjectType<BlockWattleFence, ItemBlockMulti<EnumTree>> WATTLE_FENCE =
			new ObjectType<BlockWattleFence, ItemBlockMulti<EnumTree>>("wattle_fence", "wattleFence", BlockWattleFence.class, null, EnumTree.NO_BILLET);
	public static final ObjectType<BlockGenesisDeadLogs, ItemBlockMulti<EnumTree>> DEAD_LOG =
			new ObjectType<BlockGenesisDeadLogs, ItemBlockMulti<EnumTree>>("dead_log", "log.dead", BlockGenesisDeadLogs.class, null, EnumTree.NO_DEAD)
					.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	
	public static final ImmutableList<? extends ObjectType<?, ?>> TYPES =
			ImmutableList.of(
					LOG, SAPLING, LEAVES, LEAVES_FRUIT, HANGING_FRUIT,
					BILLET, WATTLE_FENCE, FRUIT,
					DEAD_LOG);
	
	static
	{
		for (ObjectType<?, ?> type : TYPES)
		{
			type.setTypeNamePosition(TypeNamePosition.POSTFIX);
		}
	}
	
	public TreeBlocksAndItems()
	{
		super(TYPES, EnumTree.class, ImmutableList.copyOf(EnumTree.values()));
		
		setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
	}
}
