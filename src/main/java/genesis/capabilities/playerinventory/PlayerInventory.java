package genesis.capabilities.playerinventory;

import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import genesis.common.Genesis;
import genesis.util.ReflectionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class PlayerInventory extends ItemStackHandler implements IPlayerInventory, IItemHandlerModifiable, INBTSerializable<NBTTagCompound>
{
	
	public PlayerInventory()
	{
		super(41);
	}
	
	@Override
	public void storeInventory(EntityPlayer player)
	{
		for ( int i = 0 ; i < player.inventory.getSizeInventory() ; i++ )
		{
			ItemStack stack = player.inventory.removeStackFromSlot(i);
			this.insertItem(i, stack, false);
		}
	}
	
	@Override
	public void restoreInventory(EntityPlayer player)
	{
		for ( int i = 0 ; i < player.inventory.getSizeInventory() ; i++ )
		{
			ItemStack stack = this.extractItem(i, 64, false);
			if (stack != null)
			{
				if (player.inventory.getStackInSlot(i) == null)
				{
					player.inventory.setInventorySlotContents(i, stack);
				}
				else
				{
					InventoryHelper.spawnItemStack(player.worldObj, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), stack);
				}
			}
		}
	}

}
