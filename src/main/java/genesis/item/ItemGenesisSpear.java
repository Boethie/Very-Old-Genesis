package genesis.item;

import genesis.metadata.ToolItems;
import genesis.metadata.ToolItems.ToolObjectType;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.VariantsOfTypesCombo.ItemVariantCount;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;

@SuppressWarnings({"rawtypes", "unchecked"})
@ItemVariantCount(1)
public class ItemGenesisSpear extends ItemSword
{
	public final ToolItems owner;
	
	protected final ToolType type;
	protected final ToolObjectType objType;
	
	public ItemGenesisSpear(ToolType type, ToolItems owner, ToolObjectType objType)
	{
		super(type.toolMaterial);
		
		this.owner = owner;
		this.type = type;
		this.objType = objType;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		owner.addToolInformation(stack, playerIn, tooltip, advanced);
	}
}