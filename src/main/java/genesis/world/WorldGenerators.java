package genesis.world;

import java.util.ArrayList;
import java.util.List;

import genesis.combo.variant.EnumMenhirActivator;
import genesis.common.GenesisItems;
import genesis.world.iworldgenerators.WorldGenHut;
import genesis.world.iworldgenerators.WorldGenMenhirActivators;
import genesis.world.iworldgenerators.WorldGenMetaHouse;
import genesis.world.iworldgenerators.WorldGenPortal;
import genesis.world.iworldgenerators.WorldGenSmallCamp;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
		GameRegistry.registerWorldGenerator(new WorldGenMetaHouse(), 0);
		
		for (EnumMenhirActivator activator : EnumMenhirActivator.values())
		{
			if (activator.isForOverworld())
			{
				menhirActivatorsOverworld.add(GenesisItems.MENHIR_ACTIVATORS.getStack(activator));
			}
			else
			{
				menhirActivatorsGenesis.add(GenesisItems.MENHIR_ACTIVATORS.getStack(activator));
			}
		}
	}
}
