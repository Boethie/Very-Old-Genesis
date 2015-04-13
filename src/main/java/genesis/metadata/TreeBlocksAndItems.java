package genesis.metadata;

import genesis.block.BlockGenesisLogs;
import genesis.block.BlockWattleFence;
import genesis.item.ItemMulti;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TreeBlocksAndItems extends BlocksAndItemsWithVariantsOfTypes
{
	public static final ObjectType<BlockGenesisLogs> LOG = new ObjectType<BlockGenesisLogs>("log", BlockGenesisLogs.class, null);
	public static final ObjectType<ItemMulti> BILLET = new ObjectType<ItemMulti>("billet", null, ItemMulti.class, EnumTree.NO_BILLET);
	public static final ObjectType<BlockWattleFence> WATTLE_FENCE = new ObjectType<BlockWattleFence>("wattle_fence", "wattleFence", BlockWattleFence.class, null, EnumTree.NO_BILLET){
		@Override
		public IStateMapper getStateMapper(BlockWattleFence fenceBlock)
		{
			return new StateMap.Builder()
					.setProperty(fenceBlock.variantProp)
					.addPropertiesToIgnore(BlockFence.NORTH, BlockFence.EAST, BlockFence.SOUTH, BlockFence.WEST)
					.setBuilderSuffix("_" + WATTLE_FENCE.getName())
					.build();
		}
	};
	
	public TreeBlocksAndItems()
	{
		super(new ObjectType[] {LOG, BILLET, WATTLE_FENCE}, EnumTree.values());

		for (IMetadata variant : getValidVariants(BILLET))
		{
			ItemStack logStack = getStack(LOG, variant, 1);
			ItemStack billetStack = getStack(BILLET, variant, 4);

			if (variant.getName().equals("sigillaria") || variant.getName().equals("lepidodendron"))
			{
				billetStack = getStack(BILLET, variant, 1);

			}
			
			GameRegistry.addShapelessRecipe(billetStack, logStack);
		}
	}
}
