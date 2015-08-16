package genesis.item;

import genesis.metadata.ToolItems;
import genesis.metadata.ToolItems.ToolObjectType;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.VariantsOfTypesCombo.ItemVariantCount;

import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Multimap;

@ItemVariantCount(1)
public class ItemChoppingTool extends ItemAxe
{
	public final ToolItems owner;
	
	protected final ToolType type;
	protected final ToolObjectType objType;
	
	public ItemChoppingTool(ToolType type, ToolItems owner, ToolObjectType objType)
	{
		super(type.toolMaterial);
		
		this.owner = owner;
		this.type = type;
		this.objType = objType;
	}
    
	@Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers()
    {
        Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers();
        String key = SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName();
        map.removeAll(key);
        map.put(key, new AttributeModifier(itemModifierUUID, "Tool modifier", 1 + getToolMaterial().getDamageVsEntity(), 0));
        return map;
    }
	
	@Override
	public int getMetadata(ItemStack stack)
	{
		return 0;
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
