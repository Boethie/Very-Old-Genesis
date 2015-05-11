package genesis.metadata;

import genesis.block.*;
import genesis.common.GenesisCreativeTabs;
import genesis.item.*;
import genesis.util.*;
import genesis.metadata.VariantsOfTypesCombo.*;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;

public class TreeBlocksAndItems extends VariantsOfTypesCombo<ObjectType, EnumTree>
{
	public static final ObjectType<BlockGenesisLogs, ItemBlockMulti> LOG = new ObjectType("log", BlockGenesisLogs.class, null);
	public static final ObjectType<BlockGenesisSaplings, ItemBlockMulti> SAPLING = new ObjectType<BlockGenesisSaplings, ItemBlockMulti>("sapling", BlockGenesisSaplings.class, null)
			{
				@Override
				public void afterConstructed(BlockGenesisSaplings block, ItemBlockMulti item, List<IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					
					FuelHandler.setBurnTime(item, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.sapling)), true);
				}
			}
			.setIgnoredProperties(BlockSapling.STAGE);
	public static final ObjectType<BlockGenesisLeaves, ItemBlockMulti> LEAVES = new ObjectType("leaves", BlockGenesisLeaves.class, null)
			.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
	public static final ObjectType<Block, ItemMulti> BILLET = new ObjectType("billet", null, ItemMulti.class, EnumTree.NO_BILLET);
	public static final ObjectType<BlockWattleFence, ItemBlockMulti> WATTLE_FENCE = new ObjectType("wattle_fence", "wattleFence", BlockWattleFence.class, null, EnumTree.NO_BILLET)
			.setIgnoredProperties(BlockFence.NORTH, BlockFence.EAST, BlockFence.SOUTH, BlockFence.WEST);
	public static final ObjectType<BlockGenesisRottenLogs, ItemBlockMulti> ROTTEN_LOG = new ObjectType("rotten_log", "log.rotten", BlockGenesisRottenLogs.class, null, EnumTree.PSARONIUS)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	
	public TreeBlocksAndItems()
	{
		super(new ObjectType[]{LOG, SAPLING, LEAVES, BILLET, WATTLE_FENCE, ROTTEN_LOG}, EnumTree.values());

		for (EnumTree variant : getValidVariants(BILLET))
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
