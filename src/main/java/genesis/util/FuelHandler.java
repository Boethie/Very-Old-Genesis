package genesis.util;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public final class FuelHandler implements IFuelHandler
{
	public static final FuelHandler INSTANCE = new FuelHandler();
	public final HashMap<ItemStack, Integer> fuels = new HashMap<ItemStack, Integer>();

	public void setBurnTime(Block fuel, int burnTime)
	{
		setBurnTime(Item.getItemFromBlock(fuel), burnTime);
	}

	public void setBurnTime(Item fuel, int burnTime)
	{
		setBurnTime(new ItemStack(fuel), burnTime);
	}

	public void setBurnTime(ItemStack fuel, int burnTime)
	{
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
			if (entry.getKey().isItemEqual(fuel))
				return entry.getValue();
		}

		return 0;
	}
}
