package genesis.metadata;

import com.google.common.collect.ImmutableList;

public enum EnumMenhirPart implements IMetadata
{
	GLYPH("glyph", false), RECEPTACLE("receptacle", false), TOP("top", true);
	
	public static final ImmutableList<EnumMenhirPart> ORDERED = ImmutableList.of(GLYPH, RECEPTACLE, TOP);
	
	final String name;
	final String unlocalizedName;
	final boolean canStack;
	
	EnumMenhirPart(String name, String unlocalizedName, boolean canStack)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.canStack = canStack;
	}
	
	EnumMenhirPart(String name, boolean canStack)
	{
		this(name, name, canStack);
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
	
	public boolean canStack()
	{
		return canStack;
	}
	
	public int getOrderedIndex()
	{
		return ORDERED.indexOf(this);
	}
	
	public EnumMenhirPart getOffset(int offset)
	{
		int index = getOrderedIndex() + offset;
		return index >= 0 && index < ORDERED.size() ? ORDERED.get(index) : null;
	}
	
	public boolean isFirst()
	{
		return getOrderedIndex() == 0;
	}
	
	public boolean isLast()
	{
		return getOrderedIndex() == ORDERED.size() - 1;
	}
}