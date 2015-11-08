package genesis.item;

import genesis.metadata.ToolItems;
import genesis.metadata.ToolItems.ToolObjectType;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.VariantsOfTypesCombo.ItemVariantCount;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;

@ItemVariantCount(1)
public class ItemGenesisClub extends ItemSword
{
	public final ToolItems owner;
	
	protected final ToolType type;
	protected final ToolObjectType<Block, ItemGenesisClub> objType;
	
	public ItemGenesisClub(ToolType type, ToolItems owner, ToolObjectType<Block, ItemGenesisClub> objType)
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
	
	@Override
	public Multimap getItemAttributeModifiers()
	{
		Multimap modifiers = super.getItemAttributeModifiers();
		modifiers.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
				new AttributeModifier(itemModifierUUID, "Weapon modifier", 3.5F + type.toolMaterial.getDamageVsEntity(), 0));
		return modifiers;
	}
}