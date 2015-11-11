package genesis.block.tileentity.portal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import genesis.common.GenesisItems;
import genesis.metadata.EnumMenhirActivator;
import genesis.metadata.IMetadata;
import genesis.util.ItemStackKey;
import net.minecraft.item.ItemStack;

public enum EnumGlyph implements IMetadata
{
	NONE("none"),
	VEGETAL("vegetal",
			GenesisItems.menhir_activators.getStack(EnumMenhirActivator.ANCIENT_AMBER)),
	ANIMAL("animal",
			GenesisItems.menhir_activators.getStack(EnumMenhirActivator.FOSSILIZED_EGG)),
	HOMINID("hominid",
			GenesisItems.menhir_activators.getStack(EnumMenhirActivator.BROKEN_CEREMONIAL_AXE),
			GenesisItems.menhir_activators.getStack(EnumMenhirActivator.BROKEN_SPIRIT_MASK)),
	COSMOS("cosmos",
			GenesisItems.menhir_activators.getStack(EnumMenhirActivator.RUSTED_OCTAEDRITE_FRAGMENT));
	
	public static final EnumGlyph[] VALID = {VEGETAL, ANIMAL, HOMINID, COSMOS};
	
	final String name;
	final String unlocalizedName;
	final ItemStack defaultActivator;
	final Set<ItemStackKey> activators = new HashSet<ItemStackKey>();
	
	EnumGlyph(String name, String unlocalizedName, Object... activators)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		
		ItemStack defaultActivator = null;
		
		for (Object activator : activators)
		{
			ItemStackKey key = null;
			
			if (activator instanceof ItemStack)
			{
				key = addActivator((ItemStack) activator);
			}
			else if (activator instanceof ItemStackKey)
			{
				key = addActivator((ItemStackKey) activator);
			}
			else
			{
				throw new IllegalArgumentException("Invalid activator item " + activator + " for glyph with name \"" + name + "\".");
			}
			
			if (defaultActivator == null)
			{
				defaultActivator = key.createNewStack();
			}
		}
		
		this.defaultActivator = defaultActivator;
	}
	
	EnumGlyph(String name, Object... activators)
	{
		this(name, name, activators);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public Set<ItemStackKey> getActivators()
	{
		return Collections.unmodifiableSet(activators);
	}
	
	public boolean isActivator(ItemStack stack)
	{
		return activators.contains(new ItemStackKey(stack));
	}
	
	public ItemStackKey addActivator(ItemStackKey stack)
	{
		activators.add(stack);
		return stack;
	}
	
	public ItemStackKey addActivator(ItemStack stack)
	{
		return addActivator(new ItemStackKey(stack));
	}
	
	public ItemStack getDefaultActivator()
	{
		return defaultActivator;
	}
}