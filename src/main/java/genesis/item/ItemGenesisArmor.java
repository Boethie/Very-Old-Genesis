package genesis.item;

import genesis.client.render.CamouflageColorEventHandler;
import genesis.combo.ClothingItems;
import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumClothing;
import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@ItemVariantCount(1)
public class ItemGenesisArmor extends ItemArmor
{
	protected final ClothingItems owner;
	protected final ObjectType<EnumClothing, Block, ItemGenesisArmor> type;
	
	protected final EnumClothing variant;
	
	public ItemGenesisArmor(ClothingItems owner,
			ObjectType<EnumClothing, Block, ItemGenesisArmor> type,
			EnumClothing variant, Class<EnumClothing> variantClass,
			EntityEquipmentSlot slot)
	{
		super(variant.getMaterial(), -1, slot);
		
		this.owner = owner;
		this.type = type;
		
		this.variant = variant;
		
		setCreativeTab(GenesisCreativeTabs.COMBAT);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, type.getUnlocalizedName());
	}
	
	@Override
	public int getColor(ItemStack stack)
	{
		if(((ItemGenesisArmor)stack.getItem()).getArmorMaterial() == EnumClothing.CAMOUFLAGE.getMaterial())
		{
			return CamouflageColorEventHandler.color;
		}
		else
		{
			return super.getColor(stack);
		}
	}
}
