package genesis.item;

import com.google.common.collect.Multimap;

import genesis.combo.*;
import genesis.combo.ToolItems.ToolObjectType;
import genesis.combo.VariantsOfTypesCombo.ItemVariantCount;
import genesis.combo.variant.ToolTypes.ToolType;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;

@ItemVariantCount(1)
public class ItemChoppingTool extends ItemGenesisAxe
{
	public ItemChoppingTool(ToolItems owner, ToolObjectType<Block, ? extends ItemChoppingTool> objType,
			ToolType type, Class<ToolType> variantClass)
	{
		super(owner, objType, type, variantClass);
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		Material material = state.getMaterial();
		
		if (material == Material.wood
				|| material == Material.plants
				|| material == Material.vine)
			return efficiencyOnProperMaterial;
		
		return super.getStrVsBlock(stack, state);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
		
		if (slot == EntityEquipmentSlot.MAINHAND)
		{
			String key = SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName();
			map.removeAll(key);
			map.put(key, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 1 + getToolMaterial().getDamageVsEntity(), 0));
		}
		
		return map;
	}
}
