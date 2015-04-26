package genesis.metadata;

import genesis.block.*;
import genesis.common.GenesisCreativeTabs;
import genesis.item.*;
import genesis.util.*;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;

public class TreeBlocksAndItems extends VariantsOfTypesCombo
{
	public static final ObjectType<BlockGenesisLogs> LOG = new ObjectType("log", BlockGenesisLogs.class, null);
	public static final ObjectType<BlockGenesisSaplings> SAPLING = new ObjectType("sapling", BlockGenesisSaplings.class, null)
			.setIgnoredProperties(BlockSapling.STAGE);
	public static final ObjectType<BlockGenesisLeaves> LEAVES = new ObjectType("leaves", BlockGenesisLeaves.class, null)
			.setIgnoredProperties(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
	public static final ObjectType<ItemMulti> BILLET = new ObjectType("billet", null, ItemMulti.class, EnumTree.NO_BILLET);
	public static final ObjectType<BlockWattleFence> WATTLE_FENCE = new ObjectType("wattle_fence", "wattleFence", BlockWattleFence.class, null, EnumTree.NO_BILLET)
			.setIgnoredProperties(BlockFence.NORTH, BlockFence.EAST, BlockFence.SOUTH, BlockFence.WEST);
	public static final ObjectType<BlockGenesisLogs> ROTTEN_LOG = new ObjectType("rotten_log", "log.rotten", BlockGenesisLogs.class, null, EnumTree.PSARONIUS)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	
	public TreeBlocksAndItems()
	{
		super(new ObjectType[]{LOG, SAPLING, LEAVES, BILLET, WATTLE_FENCE, ROTTEN_LOG}, EnumTree.values());

		for (IMetadata variant : getValidVariants(BILLET))
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
