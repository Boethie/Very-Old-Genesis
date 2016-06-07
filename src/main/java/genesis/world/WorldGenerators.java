package genesis.world;

import genesis.combo.variant.EnumMenhirActivator;
import genesis.common.GenesisItems;
import genesis.world.iworldgenerators.*;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.*;

/**
 * Created by Vorquel on 10/27/15
 */
public class WorldGenerators
{
	public static List<ItemStack> menhirActivatorsOverworld = new ArrayList<>();
	public static List<ItemStack> menhirActivatorsGenesis = new ArrayList<>(); //todo: use this for genesis dimension activator generation
	
	public static void register()
	{
		GameRegistry.registerWorldGenerator(new WorldGenPortal(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenMenhirActivators(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenHut(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenSmallCamp(), 0);
		
		for (EnumMenhirActivator activator : EnumMenhirActivator.values())
		{
			if (activator.isForOverworld())
			{
				menhirActivatorsOverworld.add(GenesisItems.menhir_activators.getStack(activator));
			}
			else
			{
				menhirActivatorsGenesis.add(GenesisItems.menhir_activators.getStack(activator));
			}
		}
	}
}
