package genesis.item;

import java.util.List;

import genesis.combo.ArrowItems;
import genesis.combo.ObjectType;
import genesis.combo.variant.ArrowTypes.ArrowType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemGenesisArrow extends ItemArrowMulti<ArrowType>
{
	protected final ArrowItems owner;
	
	public ItemGenesisArrow(ArrowItems owner,
			ObjectType<ArrowType, Block, ? extends ItemGenesisArrow> type,
			List<ArrowType> variants, Class<ArrowType> variantClass)
	{
		super(owner, type, variants, variantClass);
		
		this.owner = owner;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, player, tooltip, advanced);
		owner.addArrowInformation(stack, player, tooltip, advanced);
	}
}
