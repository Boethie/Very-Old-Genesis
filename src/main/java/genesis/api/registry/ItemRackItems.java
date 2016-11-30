package genesis.api.registry;

import java.util.ArrayList;

import net.minecraft.item.Item;

public class ItemRackItems
{
	private static final ArrayList<Item> items = new ArrayList<Item>();
	
	public static void addItem(Item item)
	{
		items.add(item);
	}
	
	public static boolean isItemAllowed(Item item)
	{
		return items.contains(item);
	}
}