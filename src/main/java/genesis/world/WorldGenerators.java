package genesis.world;

import com.google.common.collect.Lists;
import genesis.combo.variant.EnumMenhirActivator;
import genesis.common.GenesisItems;
import genesis.world.iworldgenerators.WorldGenPortal;
import genesis.world.iworldgenerators.WorldGenMenhirActivators;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

/**
 * Created by Vorquel on 10/27/15
 */
public class WorldGenerators
{
	public static ArrayList<ItemStack> menhirActivatorsAncient = Lists.newArrayList();
	public static ArrayList<ItemStack> menhirActivatorsRecent = Lists.newArrayList(); //todo: use this for genesis dimension activator generation
	
	public static void register()
	{
		GameRegistry.registerWorldGenerator(new WorldGenPortal(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenMenhirActivators(), 0);
		
		for (EnumMenhirActivator activator : EnumMenhirActivator.values())
		{
			if (activator.isAncient())
			{
				menhirActivatorsAncient.add(GenesisItems.menhir_activators.getStack(activator));
			}
			else
			{
				menhirActivatorsRecent.add(GenesisItems.menhir_activators.getStack(activator));
			}
		}
	}
}
