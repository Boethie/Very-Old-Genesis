package genesis.world;

import genesis.common.GenesisItems;
import genesis.metadata.EnumMenhirActivator;
import genesis.world.iworldgenerators.WorldGenPortal;
import genesis.world.iworldgenerators.WorldGenMenhirActivators;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Vorquel on 10/27/15
 */
public class WorldGenerators
{
	public static ChestGenHooks menhirHutChest;
	
	public static void register()
	{
		GameRegistry.registerWorldGenerator(new WorldGenPortal(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenMenhirActivators(), 0);
		
		menhirHutChest = ChestGenHooks.getInfo("menhirHutChest");
		menhirHutChest.setMin(2);
		menhirHutChest.setMax(4);
		for (EnumMenhirActivator activator : EnumMenhirActivator.values())
		{
			if (activator.isAncient())
			{
				menhirHutChest.addItem(new WeightedRandomChestContent(GenesisItems.menhir_activators.getStack(activator), 1, 1, 1));
			}
		}
	}
}
