package genesis.block.tileentity.portal;

import genesis.block.tileentity.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityMenhirGlyph extends TileEntityBase
{
	protected EnumGlyph glyph = EnumGlyph.NONE;
	
	public TileEntityMenhirGlyph()
	{
	}
	
	public void setGlyph(EnumGlyph glyph)
	{
		this.glyph = glyph;
		markDirty();
	}
	
	public EnumGlyph getGlyph()
	{
		return glyph;
	}
	
	@Override
	protected void writeVisualData(NBTTagCompound compound, boolean save)
	{
		compound.setString("glyph", glyph.getName());
	}
	
	@Override
	protected void readVisualData(NBTTagCompound compound, boolean save)
	{
		setGlyph(getGlyph(compound));
	}
	
	public static EnumGlyph getGlyph(NBTTagCompound compound)
	{
		String glyphName = compound.getString("glyph");
		
		for (EnumGlyph checkGlyph : EnumGlyph.values())
			if (glyphName.equals(checkGlyph.getName()))
				return checkGlyph;
		
		return null;
	}
}
