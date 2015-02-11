package genesis.metadata;

import genesis.block.BlockGenesisLogs;
import genesis.item.ItemWoodBillet;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TreeBlocks extends BlocksAndItemsWithVariantsOfTypes
{
	public static final ObjectType<BlockGenesisLogs> LOG = new ObjectType<BlockGenesisLogs>("log", BlockGenesisLogs.class, null);
	public static final ObjectType<ItemWoodBillet> BILLET = new ObjectType<ItemWoodBillet>("billet", null, ItemWoodBillet.class, EnumTree.NO_BILLET);
	
	public TreeBlocks()
	{
		super(new ObjectType[] {LOG, BILLET}, EnumTree.values());
		
		for (IMetadata variant : getValidVariants(BILLET))
		{
			ItemStack logStack = getStack(LOG, variant, 1);
			ItemStack billetStack = getStack(BILLET, variant, 4);
			
			GameRegistry.addShapelessRecipe(billetStack, logStack);
		}
	}
}
