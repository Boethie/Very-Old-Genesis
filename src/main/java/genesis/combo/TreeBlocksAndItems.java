package genesis.combo;

import genesis.block.*;
import genesis.block.tileentity.BlockRack;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.EnumTree.FruitType;
import genesis.common.GenesisCreativeTabs;
import genesis.item.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;

import java.util.function.Predicate;

import com.google.common.collect.*;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityFurnace;

public class TreeBlocksAndItems extends VariantsOfTypesCombo<EnumTree>
{
	public static final ObjectType<EnumTree, BlockGenesisLogs, ItemBlockMulti<EnumTree>> LOG;
	
	public static final ObjectType<EnumTree, BlockBranch, ItemBlockMulti<EnumTree>> BRANCH;
	
	public static final ObjectType<EnumTree, BlockGenesisSaplings, ItemBlockMulti<EnumTree>> SAPLING;
	
	public static final ObjectType<EnumTree, BlockGenesisLeaves, ItemBlockMulti<EnumTree>> LEAVES;
	
	public static final ObjectType<EnumTree, BlockGenesisLeavesFruit, ItemBlockMulti<EnumTree>> LEAVES_FRUIT;
	
	public static final ObjectType<EnumTree, BlockHangingFruit, Item> HANGING_FRUIT;
	
	public static final ObjectType<EnumTree, Block, ItemFruit> FRUIT;
	
	public static final ObjectType<EnumTree, Block, ItemMulti<EnumTree>> BILLET;
	
	public static final ObjectType<EnumTree, BlockWattleFence, ItemBlockMulti<EnumTree>> WATTLE_FENCE;
	
	public static final ObjectType<EnumTree, BlockRack, ItemRack> RACK;
	
	public static final ObjectType<EnumTree, BlockGenesisDeadLogs, ItemBlockMulti<EnumTree>> DEAD_LOG;
	
	public static final ImmutableList<? extends ObjectType<EnumTree, ?, ?>> TYPES;
	
	static
	{
		Predicate<EnumTree> billet = (v) -> v.hasBillet();
		
		LOG = ObjectType.createBlock(EnumTree.class, "log", BlockGenesisLogs.class);
		LOG.setVariantFilter((v) -> !v.isBush());
		
		BRANCH = ObjectType.createBlock(EnumTree.class, "branch", BlockBranch.class);
		BRANCH.setVariantFilter((v) -> v.isBush());
		
		SAPLING = ObjectType.createBlock(EnumTree.class, "sapling", BlockGenesisSaplings.class);
		SAPLING.setConstructedFunction((b, i) ->
						FuelHandler.setBurnTime(i, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.sapling)), true))
				.setIgnoredProperties(BlockSapling.STAGE);
		
		LEAVES = ObjectType.createBlock(EnumTree.class, "leaves", BlockGenesisLeaves.class);
		LEAVES.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
		
		LEAVES_FRUIT = ObjectType.createBlock(EnumTree.class, "leaves_fruit", "leaves.fruit", BlockGenesisLeavesFruit.class);
		LEAVES_FRUIT.setVariantFilter((v) -> v.getFruitType() == FruitType.LEAVES)
				.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
		
		HANGING_FRUIT = ObjectType.create(EnumTree.class, "hanging_fruit", "hanging.fruit", BlockHangingFruit.class, null);
		HANGING_FRUIT.setVariantFilter((v) -> v.getFruitType() == FruitType.HANGING);
		
		FRUIT = ObjectType.createItem(EnumTree.class, "fruit", Unlocalized.Section.FOOD + "fruit", ItemFruit.class);
		FRUIT.setVariantFilter((v) -> v.getFruitType() != FruitType.NONE);
		
		BILLET = ObjectType.createItem(EnumTree.class, "billet", Unlocalized.Section.MATERIAL + "billet");
		BILLET.setVariantFilter(billet)
				.setConstructedFunction((b, i) ->
						FuelHandler.setBurnTime(i, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.planks)), true));
		
		WATTLE_FENCE = ObjectType.createBlock(EnumTree.class, "wattle_fence", "wattleFence", BlockWattleFence.class);
		WATTLE_FENCE.setVariantFilter(billet);
		
		RACK = ObjectType.create(EnumTree.class, "rack", BlockRack.class, ItemRack.class);
		RACK.setVariantFilter(billet)
				.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
				.setUseSeparateVariantJsons(false);
		
		DEAD_LOG = ObjectType.createBlock(EnumTree.class, "dead_log", "log.dead", BlockGenesisDeadLogs.class);
		DEAD_LOG.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
				.setVariantFilter((v) -> v.hasDead());
		
		TYPES = ImmutableList.of(
						LOG, BRANCH,
						SAPLING,
						LEAVES, LEAVES_FRUIT,
						HANGING_FRUIT, FRUIT,
						BILLET, WATTLE_FENCE, RACK,
						DEAD_LOG);
		
		for (ObjectType<EnumTree, ?, ?> type : TYPES)
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
