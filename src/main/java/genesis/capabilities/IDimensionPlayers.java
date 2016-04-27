package genesis.capabilities;

import java.util.*;

import genesis.util.PlayerWorldState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.DimensionType;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

public interface IDimensionPlayers
{
	void setPlayer(EntityPlayerMP player);
	
	void restorePlayer(DimensionType dim, EntityPlayerMP newPlayer);
	void storePlayer(DimensionType dim);
	
	Set<DimensionType> getDimensions();
	NBTTagCompound getData(DimensionType dim);
	void setData(DimensionType dim, NBTTagCompound data);
	
	public static class Storage implements IStorage<IDimensionPlayers>
	{
		@Override
		public NBTBase writeNBT(Capability<IDimensionPlayers> capability, IDimensionPlayers instance, EnumFacing side)
		{
			NBTTagCompound compound = new NBTTagCompound();
			
			for (DimensionType dim : instance.getDimensions())
			{
				compound.setTag(String.valueOf(dim.getId()), instance.getData(dim));
			}
			
			return compound;
		}
		
		@Override
		public void readNBT(Capability<IDimensionPlayers> capability, IDimensionPlayers instance, EnumFacing side, NBTBase nbt)
		{
			NBTTagCompound compound = (NBTTagCompound) nbt;
			
			for (String key : compound.getKeySet())
			{
				instance.setData(DimensionType.getById(Integer.parseInt(key)), compound.getCompoundTag(key));
			}
		}
	}
	
	public static class Impl implements IDimensionPlayers
	{
		private EntityPlayerMP player;
		private Map<DimensionType, NBTTagCompound> map = new EnumMap<>(DimensionType.class);
		
		@Override
		public void setPlayer(EntityPlayerMP player)
		{
			this.player = player;
		}
		
		@Override
		public void restorePlayer(DimensionType dim, EntityPlayerMP newPlayer)
		{
			newPlayer.dimension = dim.getId();
			newPlayer.capabilities.isFlying = player.capabilities.isFlying;
			newPlayer.interactionManager.setGameType(player.interactionManager.getGameType());
			newPlayer.fallDistance = player.fallDistance;
			PlayerWorldState playerState = new PlayerWorldState(newPlayer);
			
			newPlayer.readFromNBT(getData(dim));
			
			// Restore the player's state in the world.
			playerState.restore(newPlayer);
			
			newPlayer.inventory.currentItem = player.inventory.currentItem;	// Keep the current selected hotbar item.
			// Send the player's inventory, stats and current item.
			FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().syncPlayerInventory(newPlayer);
			
			// Send the player's current potion effects.
			for (PotionEffect effect : newPlayer.getActivePotionEffects())
				newPlayer.playerNetServerHandler.sendPacket(new SPacketEntityEffect(newPlayer.getEntityId(), effect));
		}
		
		@Override
		public void storePlayer(DimensionType dim)
		{
			NBTTagCompound storingData = new NBTTagCompound();
			player.writeToNBT(storingData);
		}
		
		@Override
		public Set<DimensionType> getDimensions()
		{
			return map.keySet();
		}
		
		@Override
		public NBTTagCompound getData(DimensionType dim)
		{
			return map.get(dim);
		}
		
		@Override
		public void setData(DimensionType dim, NBTTagCompound data)
		{
			map.put(dim, data);
		}
	}
}
