package genesis.util;

import genesis.item.IMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public final class Metadata
{
	private static final HashMap<Class, ArrayList> LOOKUP = new HashMap<Class, ArrayList>();

	public static <T extends IMetadata> ArrayList<T> getLookup(Class<? extends T> clazz)
	{
		ArrayList<T> metaLookup = LOOKUP.get(clazz);

		if (metaLookup == null)
		{
			LOOKUP.put(clazz, metaLookup = new ArrayList<T>());
		}

		return metaLookup;
	}

	public static void add(Class clazz, IMetadata meta)
	{
		getLookup(clazz).add(meta);
	}

	public static <T extends IMetadata> T get(Class<? extends T> clazz, int metadata)
	{
		ArrayList<T> metaLookup = getLookup(clazz);
		T meta;

		try
		{
			meta = metaLookup.get(metadata);
		}
		catch (IndexOutOfBoundsException e)
		{
			e.printStackTrace();
			meta = metaLookup.get(0);
		}

		return meta;
	}

	public static int getMetadata(Enum meta)
	{
		return meta.ordinal();
	}

	public static int getMetadata(IBlockState state, IProperty property)
	{
		return getMetadata((Enum) state.getValue(property));
	}

	public static ItemStack newStack(Enum meta)
	{
		return newStack(meta, 1);
	}

	public static ItemStack newStack(Enum meta, int amount)
	{
		return new ItemStack(((IMetadata) meta).getItem(), amount, getMetadata(meta));
	}

	public static <T extends Enum & IMetadata> IBlockState getState(Block block, IProperty property, Class<T> clazz, int meta)
	{
		return block.getDefaultState().withProperty(property, Metadata.get(clazz, meta));
	}

	public static <T extends Enum & IMetadata> void getSubItems(Class<T> clazz, List list)
	{
		for (Enum<? extends IMetadata> meta : getLookup(clazz))
		{
			list.add(newStack(meta));
		}
	}

	public static String getUnlocalizedName(String unlocalizedName, int metadata, Class metaClass)
	{
		return unlocalizedName + "." + get(metaClass, metadata).getUnlocalizedName();
	}
}
