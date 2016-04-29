package genesis.combo;

import genesis.block.*;
import genesis.block.tileentity.BlockRack;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.EnumTree.FruitType;
import genesis.common.GenesisCreativeTabs;
import genesis.item.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;

import java.util.*;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityFurnace;

public class TreeBlocksAndItems extends VariantsOfTypesCombo<EnumTree>
{
	public static final ObjectType<BlockGenesisLogs, ItemBlockMulti<EnumTree>> LOG;
	
	public static final ObjectType<BlockBranch, ItemBlockMulti<EnumTree>> BRANCH;
	
	public static final ObjectType<BlockGenesisSaplings, ItemBlockMulti<EnumTree>> SAPLING;
	
	public static final ObjectType<BlockGenesisLeaves, ItemBlockMulti<EnumTree>> LEAVES;
	
	public static final ObjectType<BlockGenesisLeavesFruit, ItemBlockMulti<EnumTree>> LEAVES_FRUIT;
	
	public static final ObjectType<BlockHangingFruit, Item> HANGING_FRUIT;
	
	public static final ObjectType<Block, ItemFruit> FRUIT;
	
	public static final ObjectType<Block, ItemMulti<EnumTree>> BILLET;
	
	public static final ObjectType<BlockWattleFence, ItemBlockMulti<EnumTree>> WATTLE_FENCE;
	
	public static final ObjectType<BlockRack, ItemRack> RACK;
	
	public static final ObjectType<BlockGenesisDeadLogs, ItemBlockMulti<EnumTree>> DEAD_LOG;
	
	public static final ImmutableList<? extends ObjectType<?, ?>> TYPES;
	
	static
	{
		FluentIterable<EnumTree> base = MiscUtils.iterable(EnumTree.values());
		List<EnumTree> billets = base.filter((v) -> v.hasBillet()).toList();
		List<EnumTree> branches = base.filter((v) -> v.isBush()).toList();
		
		LOG = ObjectType.createBlock("log", BlockGenesisLogs.class, branches);
		
		BRANCH = ObjectType.createBlock("branch", BlockBranch.class);
		BRANCH.setValidVariants(branches);
		
		SAPLING = ObjectType.createBlock("sapling", BlockGenesisSaplings.class);
		SAPLING.setConstructedFunction((b, i) ->
						FuelHandler.setBurnTime(i, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.sapling)), true))
				.setIgnoredProperties(BlockSapling.STAGE);
		
		LEAVES = ObjectType.createBlock("leaves", BlockGenesisLeaves.class);
		LEAVES.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
		
		LEAVES_FRUIT = ObjectType.createBlock("leaves_fruit", "leaves.fruit", BlockGenesisLeavesFruit.class);
		LEAVES_FRUIT.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE)
				.setValidVariants(base.filter((v) -> v.getFruitType() == FruitType.LEAVES).toList());
		
		HANGING_FRUIT = ObjectType.create("hanging_fruit", "hanging.fruit", BlockHangingFruit.class, null);
		HANGING_FRUIT.setValidVariants(base.filter((v) -> v.getFruitType() == FruitType.HANGING).toList());
		
		FRUIT = ObjectType.createItem("fruit", Unlocalized.Section.FOOD + "fruit", ItemFruit.class);
		FRUIT.setValidVariants(base.filter((v) -> v.getFruitType() != FruitType.NONE).toList());
		
		BILLET = ObjectType.createItem("billet", Unlocalized.Section.MATERIAL + "billet");
		BILLET.setConstructedFunction((b, i) ->
						FuelHandler.setBurnTime(i, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.planks)), true))
				.setValidVariants(billets);
		
		WATTLE_FENCE = ObjectType.createBlock("wattle_fence", "wattleFence", BlockWattleFence.class);
		WATTLE_FENCE.setValidVariants(billets);
		
		RACK = ObjectType.create("rack", BlockRack.class, ItemRack.class);
		RACK.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
				.setUseSeparateVariantJsons(false)
				.setValidVariants(billets);
		
		DEAD_LOG = ObjectType.createBlock("dead_log", "log.dead", BlockGenesisDeadLogs.class);
		DEAD_LOG.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
				.setValidVariants(base.filter((v) -> v.hasDead()).toList());
		
		TYPES = ImmutableList.of(
						LOG, BRANCH,
						SAPLING,
						LEAVES, LEAVES_FRUIT,
						HANGING_FRUIT, FRUIT,
						BILLET, WATTLE_FENCE, RACK,
						DEAD_LOG);
		
		for (ObjectType<?, ?> type : TYPES)
		{
			type.setTypeNamePosition(TypeNamePosition.POSTFIX);
		}
		
		RACK.setTypeNamePosition(TypeNamePosition.PREFIX);
	}
	
	public TreeBlocksAndItems()
	{
		super(TYPES, EnumTree.class, ImmutableList.copyOf(EnumTree.values()));
		
		setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
	}
}
