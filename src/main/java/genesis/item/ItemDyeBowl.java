package genesis.item;

import java.util.List;

import genesis.metadata.GenesisDye;
import genesis.metadata.IMetadata;
import genesis.metadata.ItemsCeramicBowls;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemDyeBowl extends ItemMulti<IMetadata>
{
	protected final ItemsCeramicBowls bowlsOwner;
	
	public ItemDyeBowl(List<IMetadata> variants, ItemsCeramicBowls owner, ObjectType<? extends Block, ? extends ItemMulti<IMetadata>> type)
	{
		super(variants, owner, type);
		
		bowlsOwner = owner;
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		return bowlsOwner.getStack(EnumCeramicBowls.BOWL);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target)
	{
		boolean consume = false;
		EnumDyeColor color = ((GenesisDye) bowlsOwner.getVariant(stack)).getColor();
		
		if (target instanceof EntitySheep)
		{
			EntitySheep sheep = (EntitySheep)target;
			
			if (!sheep.getSheared() && sheep.getFleeceColor() != color)
			{
				sheep.setFleeceColor(color);
				consume = true;
			}
		}
		else if (target instanceof EntityWolf)
		{
			EntityWolf wolf = (EntityWolf) target;
			
			if (wolf.getCollarColor() != color)
			{
				wolf.setCollarColor(color);
				consume = true;
				
				if (wolf.isOwner(player))
				{	// Set the AI's sitting value to the watchable sitting value, which will not have updated. 
	                wolf.getAISit().setSitting(wolf.isSitting());	// This keeps it in the same state as before interacting.
				}
			}
		}
		
		if (consume && !player.capabilities.isCreativeMode)
		{
			ItemStack empty = bowlsOwner.getStack(EnumCeramicBowls.BOWL);
			
			if (--stack.stackSize <= 0)
			{
				ForgeEventFactory.onPlayerDestroyItem(player, stack);
				stack.stackSize = 1;	// Hack to make the code calling this ignore that the stack has been destroyed.
				player.inventory.setInventorySlotContents(player.inventory.currentItem, empty);
			}
			else if (!player.worldObj.isRemote && !player.inventory.addItemStackToInventory(empty))
			{
				player.dropPlayerItemWithRandomChoice(empty, false);
			}
		}
		
		return consume;
	}
}
