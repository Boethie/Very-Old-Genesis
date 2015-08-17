package genesis.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackKey
{
	protected final Item item;
	protected final int metadata;
	protected final NBTTagCompound compound;
	
	public ItemStackKey(Item item, int metadata, NBTTagCompound compound)
	{
		if (item == null)
		{
			throw new IllegalArgumentException("Item passed to constructor was null.");
		}
		
		this.item = item;
		this.metadata = metadata;
		this.compound = compound;
	}
	
	public ItemStackKey(Item item)
	{
		this(item, OreDictionary.WILDCARD_VALUE, null);
	}
	
	public ItemStackKey(ItemStack stack)
	{
		this(stack.getItem(), stack.getItemDamage(), stack.getTagCompound() == null ? null : (NBTTagCompound) stack.getTagCompound().copy());
	}
	
	@Override
	public int hashCode()
	{
		return item.hashCode() + (compound != null ? compound.hashCode() : 0);
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		
		if (other instanceof ItemStackKey)
		{
			ItemStackKey otherKey = (ItemStackKey) other;
			
			if (item.equals(otherKey.item))
			{
				if (metadata == OreDictionary.WILDCARD_VALUE || otherKey.metadata == OreDictionary.WILDCARD_VALUE || metadata == otherKey.metadata)
				{
					if (compound == otherKey.compound || (compound != null && compound.equals(otherKey.compound)))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean equalsStack(ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}
		
		return equals(new ItemStackKey(stack));
	}
	
	public ItemStack createNewStack()
	{
		ItemStack stack = new ItemStack(item, 1, metadata);
		
		if (compound != null)
			stack.setTagCompound((NBTTagCompound) compound.copy());
		
		return stack;
	}
	
	@Override
	public String toString()
	{
		return createNewStack().toString();
	}
}