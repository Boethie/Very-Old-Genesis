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

	public static void add(IMetadata meta)
	{
		getLookup(meta.getClass()).add(meta);
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

	public static <T extends IMetadata> T getRandom(Class<? extends T> clazz)
	{
		ArrayList<T> metaLookup = getLookup(clazz);
		return metaLookup.get(Constants.RANDOM.nextInt(metaLookup.size()));
	}

	public static <T extends Enum & IMetadata> T getEnum(Class<? extends T> clazz, int metadata)
	{
		return get(clazz, metadata);
	}

	public static int getMetadata(IMetadata meta)
	{
		return ((Enum) meta).ordinal();
	}

	public static int getMetadata(IBlockState state, IProperty property)
	{
		return getMetadata((IMetadata) state.getValue(property));
	}

	public static ItemStack newStack(IMetadata meta)
	{
		return newStack(meta, 1);
	}

	public static ItemStack newStack(IMetadata meta, int amount)
	{
		return new ItemStack(meta.getItem(), amount, getMetadata(meta));
	}

	public static IBlockState getState(Block block, IProperty property, Class clazz, int meta)
	{
		return block.getDefaultState().withProperty(property, getEnum(clazz, meta));
	}

	public static void getSubItems(Class<? extends IMetadata> clazz, List list)
	{
		for (IMetadata meta : getLookup(clazz))
		{
			list.add(newStack(meta));
		}
	}

	public static String getUnlocalizedName(String unlocalizedName, int metadata, Class metaClass)
	{
		return unlocalizedName + "." + get(metaClass, metadata).getUnlocalizedName();
	}
}
