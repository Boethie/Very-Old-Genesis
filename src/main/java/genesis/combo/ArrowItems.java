package genesis.combo;

import java.util.List;

import genesis.combo.variant.*;
import genesis.combo.variant.ArrowTypes.ArrowType;
import genesis.item.ItemGenesisArrow;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import genesis.util.Constants.Unlocalized.Section;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ArrowItems extends VariantsCombo<ArrowType, Block, ItemGenesisArrow>
{
	public ArrowItems()
	{
		super("arrows",
				ObjectType.createItem(ArrowType.class, "arrow", ItemGenesisArrow.class),
				ArrowType.class, ArrowTypes.getAll());
		
		setNames(Constants.MOD_ID, Unlocalized.PREFIX);
	}
	
	public void addArrowInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		ArrowType type = getVariant(stack);
		
		if (type != null)
			tooltip.add(I18n.format(Unlocalized.ITEM_PREFIX + Section.TOOL + Section.MATERIAL + type.tip.getUnlocalizedName()));
	}
	
	public ItemStack getStack(EnumArrowShaft shaft, EnumToolMaterial material, int size)
	{
		return getStack(ArrowTypes.getToolHead(shaft, material), size);
	}
	
	public ItemStack getStack(EnumArrowShaft shaft, EnumToolMaterial material)
	{
		return getStack(shaft, material, 1);
	}
}
