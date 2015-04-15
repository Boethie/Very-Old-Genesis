package genesis.util;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import genesis.metadata.EnumDung;
import genesis.metadata.IMetadata;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.oredict.OreDictionary;

public final class FuelHandler implements IFuelHandler
{
	public static final FuelHandler INSTANCE = new FuelHandler();
	public final HashMap<ItemStack, Integer> fuels = new HashMap<ItemStack, Integer>();

	private FuelHandler()
	{
	}

	public void setBurnTime(Block fuel, int burnTime, boolean wildcard)
	{
		setBurnTime(Item.getItemFromBlock(fuel), burnTime, wildcard);
	}

	public void setBurnTime(Item fuel, int burnTime, boolean wildcard)
	{
		setBurnTime(new ItemStack(fuel), burnTime, wildcard);
	}

	public void setBurnTime(ItemStack fuel, int burnTime, boolean wildcard)
	{
		if (fuel == null)
		{
			throw new IllegalArgumentException("Attempted to register a null ItemStack as a fuel.");
		}
		
		if (wildcard)
		{
			fuel.setItemDamage(OreDictionary.WILDCARD_VALUE);
		}
		
		fuels.put(fuel, burnTime);
	}

	public int getBurnTime(Block fuel)
	{
		return getBurnTime(Item.getItemFromBlock(fuel));
	}

	public int getBurnTime(Item fuel)
	{
		return getBurnTime(new ItemStack(fuel));
	}

	@Override
	public int getBurnTime(ItemStack fuel)
	{
		for (Entry<ItemStack, Integer> entry : fuels.entrySet())
		{
			ItemStack registryFuel = entry.getKey();
			
			if (fuel != null)
			{
				if (registryFuel.getItem() == fuel.getItem())
				{
					if (registryFuel.getMetadata() == OreDictionary.WILDCARD_VALUE ||
							registryFuel.getMetadata() == fuel.getMetadata())
					{
						return entry.getValue();
					}
				}
			}
		}

		return 0;
	}
}
