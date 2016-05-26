package genesis.world;

import com.google.common.collect.Lists;
import genesis.combo.variant.EnumMenhirActivator;
import genesis.common.Genesis;
import genesis.common.GenesisItems;
import genesis.world.iworldgenerators.WorldGenPortal;
import genesis.world.iworldgenerators.WorldGenHut;
import genesis.world.iworldgenerators.WorldGenMenhirActivators;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

/**
 * Created by Vorquel on 10/27/15
 */
public class WorldGenerators
{
	public static ArrayList<ItemStack> menhirActivatorsOverworld = Lists.newArrayList();
	public static ArrayList<ItemStack> menhirActivatorsGenesis = Lists.newArrayList(); //todo: use this for genesis dimension activator generation
	
	public static void register()
	{
		GameRegistry.registerWorldGenerator(new WorldGenPortal(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenMenhirActivators(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenHut(), 48);
		
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
