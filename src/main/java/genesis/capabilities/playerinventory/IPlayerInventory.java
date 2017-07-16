package genesis.capabilities.playerinventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IPlayerInventory extends IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound>
{
	void storeInventory(EntityPlayer player);
	
	void restoreInventory(EntityPlayer player);
}
