package genesis.item;

import java.util.List;

import genesis.block.tileentity.portal.BlockMenhir;
import genesis.block.tileentity.portal.TileEntityMenhirGlyph;
import genesis.combo.*;
import genesis.combo.variant.EnumMenhirPart;
import genesis.block.tileentity.portal.EnumGlyph;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;

public class ItemMenhir extends ItemBlock
{
	public final VariantsOfTypesCombo<EnumMenhirPart> owner;
	public final ObjectType<EnumMenhirPart, BlockMenhir, ItemMenhir> type;
	
	protected final List<EnumMenhirPart> variants;

	public ItemMenhir(BlockMenhir block, VariantsOfTypesCombo<EnumMenhirPart> owner,
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
			EnumGlyph glyph = EnumGlyph.NONE;
			
			NBTTagCompound compound = stack.getTagCompound();
			
			if (compound != null && compound.hasKey("BlockEntityTag", 10))
			{
				TileEntityMenhirGlyph glyphTE = new TileEntityMenhirGlyph();
				glyphTE.readFromNBT(compound.getCompoundTag("BlockEntityTag"));
				glyph = glyphTE.getGlyph();
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
