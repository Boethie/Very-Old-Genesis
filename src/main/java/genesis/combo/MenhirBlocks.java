package genesis.combo;

import genesis.block.tileentity.portal.BlockMenhir;
import genesis.block.tileentity.portal.EnumGlyph;
import genesis.combo.variant.EnumMenhirPart;
import genesis.item.ItemMenhir;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import net.minecraft.item.ItemStack;

public class MenhirBlocks extends VariantsCombo<EnumMenhirPart, BlockMenhir, ItemMenhir>
{
	public MenhirBlocks()
	{
		super("menhirs",
				new ObjectType<>(EnumMenhirPart.class, "menhir", "menhir", BlockMenhir.class, ItemMenhir.class),
				EnumMenhirPart.class, EnumMenhirPart.values());
		
		setNames(Constants.MOD_ID, Unlocalized.PREFIX);
		
		getObjectType().setUseSeparateVariantJsons(false);
		getObjectType().setShouldRegisterVariantModels(false);
	}
	
	public ItemStack getGlyphStack(EnumGlyph glyph)
	{
		ItemStack stack = getStack(EnumMenhirPart.GLYPH);
		EnumGlyph.setGlyphToStack(stack, glyph);
		return stack;
	}
	
	public ItemStack getReceptacleStack()
	{
		return getStack(EnumMenhirPart.RECEPTACLE);
	}
	
	public ItemStack getTopStack()
	{
		return getStack(EnumMenhirPart.TOP);
	}
}
