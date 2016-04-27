package genesis.item;

import java.util.List;

import genesis.combo.ToolItems;
import genesis.combo.ToolItems.ToolObjectType;
import genesis.combo.VariantsOfTypesCombo.ItemVariantCount;
import genesis.combo.variant.ToolTypes.ToolType;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

@ItemVariantCount(1)
public class ItemGenesisAxe extends ItemAxe
{
	public static final ToolMaterial PLACEHOLDER_MATERIAL = ToolMaterial.WOOD;
	
	public final ToolItems owner;
	
	protected final ToolType type;
	protected final ToolObjectType<Block, ? extends ItemGenesisAxe> objType;
	
	public ItemGenesisAxe(ToolItems owner, ToolObjectType<Block, ? extends ItemGenesisAxe> objType,
			ToolType type, Class<ToolType> variantClass)
	{
		super(PLACEHOLDER_MATERIAL);
		
		this.owner = owner;
		this.type = type;
		this.objType = objType;
		
		toolMaterial = type.toolMaterial;
		setMaxDamage(toolMaterial.getMaxUses());
		efficiencyOnProperMaterial = toolMaterial.getEfficiencyOnProperMaterial();
		damageVsEntity = toolMaterial.getDamageVsEntity();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		owner.addToolInformation(stack, playerIn, tooltip, advanced);
	}
}
