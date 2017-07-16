package genesis.capabilities.playerinventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class PlayerInventory extends ItemStackHandler implements IPlayerInventory, IItemHandlerModifiable, INBTSerializable<NBTTagCompound>
{
	
	private List<PotionEffect> activeEffects;

	private float experience;
	private int experienceLevel;
	private int experienceTotal;
	
	public PlayerInventory()
	{
		super(41);
		this.activeEffects = new ArrayList<>();
	}
	
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		// active effects
		if (!this.activeEffects.isEmpty())
		{
			NBTTagList nbttaglist = new NBTTagList();
			for (PotionEffect potioneffect : this.activeEffects)
			{
				nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
			}
			nbt.setTag("ActiveEffects", nbttaglist);
		}
		
		// experience
		nbt.setFloat("XpP", this.experience);
		nbt.setInteger("XpLevel", this.experienceLevel);
		nbt.setInteger("XpTotal", this.experienceTotal);
		
		// inventory
		nbt.setTag("Inventory", super.serializeNBT());
		
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		// active effects
		this.activeEffects.clear();
		if (nbt.hasKey("ActiveEffects", NBT.TAG_LIST))
		{
			NBTTagList nbttaglist = nbt.getTagList("ActiveEffects", NBT.TAG_COMPOUND);
			for (int i = 0; i < nbttaglist.tagCount(); ++i)
			{
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
				this.activeEffects.add(potioneffect);
			}
		}
		
		// experience
		this.experience = nbt.getFloat("XpP");
		this.experienceLevel = nbt.getInteger("XpLevel");
		this.experienceTotal = nbt.getInteger("XpTotal");
		
		// inventory
		super.deserializeNBT(nbt.getCompoundTag("Inventory"));
	}
	
	
	@Override
	public void storeInventory(EntityPlayer player)
	{
		// active effects
		this.activeEffects = new ArrayList<>();
		for ( PotionEffect effect : player.getActivePotionEffects() )
		{
			this.activeEffects.add(effect);
		}
		player.clearActivePotions();
		
		// experience
		this.experience = player.experience; player.experience = 0F;
		this.experienceLevel = player.experienceLevel; player.experienceLevel = 0;
		this.experienceTotal = player.experienceTotal; player.experienceTotal = 0;
		
		// inventory
		for ( int i = 0 ; i < player.inventory.getSizeInventory() ; i++ )
		{
			ItemStack stack = player.inventory.removeStackFromSlot(i);
			this.insertItem(i, stack, false);
		}
	}
	
	@Override
	public void restoreInventory(EntityPlayer player)
	{
		// active effects
		for ( PotionEffect potioneffect : this.activeEffects )
		{
			player.addPotionEffect(potioneffect);
		}
		
		// experience
		player.experience += this.experience;
		player.experienceLevel += this.experienceLevel;
		player.experienceTotal += this.experienceTotal;
		
		// inventory
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
