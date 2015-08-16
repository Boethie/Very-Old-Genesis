package genesis.metadata;

import genesis.block.BlockGenesisLeaves;
import genesis.block.BlockGenesisLogs;
import genesis.block.BlockGenesisRottenLogs;
import genesis.block.BlockGenesisSaplings;
import genesis.block.BlockWattleFence;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.item.ItemMulti;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import genesis.util.FuelHandler;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("rawtypes")
public class TreeBlocksAndItems extends VariantsOfTypesCombo<ObjectType, EnumTree>
{
	public static final ObjectType<BlockGenesisLogs, ItemBlockMulti> LOG = new ObjectType<BlockGenesisLogs, ItemBlockMulti>("log", BlockGenesisLogs.class, null);
	public static final ObjectType<BlockGenesisSaplings, ItemBlockMulti> SAPLING = new ObjectType<BlockGenesisSaplings, ItemBlockMulti>("sapling", BlockGenesisSaplings.class, null)
			{
				@Override
				public void afterConstructed(BlockGenesisSaplings block, ItemBlockMulti item, List<? extends IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					FuelHandler.setBurnTime(item, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.sapling)), true);
				}
			}
			.setIgnoredProperties(BlockSapling.STAGE);
	public static final ObjectType<BlockGenesisLeaves, ItemBlockMulti> LEAVES = new ObjectType<BlockGenesisLeaves, ItemBlockMulti>("leaves", BlockGenesisLeaves.class, null)
			.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
	public static final ObjectType<Block, ItemMulti> BILLET = new ObjectType<Block, ItemMulti>("billet", Unlocalized.Section.MATERIAL + "billet", null, ItemMulti.class, EnumTree.NO_BILLET)
			{
				@Override
				public void afterConstructed(Block block, ItemMulti item, List<? extends IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					FuelHandler.setBurnTime(item, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.planks)), true);
				}
			};
	public static final ObjectType<BlockWattleFence, ItemBlockMulti> WATTLE_FENCE = new ObjectType<BlockWattleFence, ItemBlockMulti>("wattle_fence", "wattleFence", BlockWattleFence.class, null, EnumTree.NO_BILLET);
	public static final ObjectType<BlockGenesisRottenLogs, ItemBlockMulti> ROTTEN_LOG = new ObjectType<BlockGenesisRottenLogs, ItemBlockMulti>("rotten_log", "log.rotten", BlockGenesisRottenLogs.class, null, EnumTree.PSARONIUS)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	
	public TreeBlocksAndItems()
	{
		super(new ObjectType[]{LOG, SAPLING, LEAVES, BILLET, WATTLE_FENCE, ROTTEN_LOG}, EnumTree.values());

		setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
		
		for (EnumTree variant : getSharedValidVariants(LOG, BILLET))
		{
			ItemStack logStack = getStack(LOG, variant, 1);
			ItemStack billetStack = getStack(BILLET, variant, 4);
			
			if (variant == EnumTree.SIGILLARIA || variant == EnumTree.LEPIDODENDRON)
			{
				billetStack.stackSize = 1;
			}
			
			GameRegistry.addShapelessRecipe(billetStack, logStack);
		}
	}
}
