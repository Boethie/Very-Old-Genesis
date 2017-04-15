package genesis.item;

import java.util.List;

import genesis.block.tileentity.portal.BlockMenhir;
import genesis.combo.*;
import genesis.combo.variant.EnumMenhirPart;
import genesis.block.tileentity.portal.EnumGlyph;
import net.minecraft.item.*;

public class ItemMenhir extends ItemBlock
{
	public final MenhirBlocks owner;
	public final ObjectType<EnumMenhirPart, BlockMenhir, ItemMenhir> type;
	
	protected final List<EnumMenhirPart> variants;

	public ItemMenhir(BlockMenhir block, MenhirBlocks owner,
			ObjectType<EnumMenhirPart, BlockMenhir, ItemMenhir> type,
			List<EnumMenhirPart> variants, Class<EnumMenhirPart> variantClass)
	{
		super(block);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		String out = owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
		
		if (owner.getVariant(stack) == EnumMenhirPart.GLYPH)
		{
			EnumGlyph glyph = EnumGlyph.getGlyphFromStack(stack);
			
			if (glyph == null)
			{
				glyph = EnumGlyph.NONE;
			}
			
			out += "." + glyph.getUnlocalizedName();
		}
		
		return out;
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
