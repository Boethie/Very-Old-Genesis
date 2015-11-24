package genesis.item;

import java.util.List;

import genesis.common.GenesisItems;
import genesis.metadata.*;
import genesis.metadata.ItemsCeramicBowls.*;
import genesis.metadata.MultiMetadataList.MultiMetadata;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPorridge extends ItemGenesisFood<MultiMetadata>
{
	public ItemPorridge(VariantsOfTypesCombo<MultiMetadata> owner, ObjectType<Block, ? extends ItemPorridge> type, List<MultiMetadata> variants, Class<MultiMetadata> variantClass)
	{
		super(owner, type, variants, variantClass);
		
		setHasSubtypes(true);
	}
	
	@Override
	public int getHealAmount(ItemStack stack)
	{
		return ((EnumPorridge) owner.getVariant(stack).getOriginal()).getFoodAmount();
	}
	
	@Override
	public float getSaturationModifier(ItemStack stack)
	{
		return ((EnumPorridge) owner.getVariant(stack).getOriginal()).getSaturationModifier();
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player)
	{
		ItemStack out = super.onItemUseFinish(stack, world, player);
		
		ItemStack bowl = GenesisItems.bowls.getStack(EnumCeramicBowls.BOWL);
		
		if (stack.stackSize == 0)
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); i++)
			{
				if (player.inventory.getStackInSlot(i) == stack)
				{
					return bowl;
				}
			}
		}
		else if (!player.inventory.addItemStackToInventory(bowl))
		{
			player.dropItem(bowl, false, true);
		}
		
		return out;
	}
}
