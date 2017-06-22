package genesis.capabilities;

import javax.annotation.Nullable;
import genesis.capabilities.playerinventory.CapabilityPlayerInventory;
import genesis.capabilities.playerinventory.IPlayerInventory;
import genesis.util.Constants;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GenesisCapabilities
{

	public static void register()
	{
		CapabilityPlayerInventory.register();
	}
	

	public static class Provider<V> implements ICapabilitySerializable<NBTBase>
	{
		private final Capability<V> capability;
		private final EnumFacing facing;
		
		private V instance;
		
		public Provider(Capability<V> capability, @Nullable EnumFacing facing)
		{
			this.capability = capability;
			this.facing = facing;
			this.instance = capability.getDefaultInstance();
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return this.capability == capability;
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			return this.capability == capability ? this.capability.cast(instance) : null;
		}
		
		@Override
		public NBTBase serializeNBT()
		{
			return this.capability.getStorage().writeNBT(this.capability, this.instance, null);
		}
		
		@Override
		public void deserializeNBT(NBTBase nbt)
		{
			this.capability.getStorage().readNBT(this.capability, this.instance, null, nbt);
		}
	}
	
	
	public static class Storage<V extends INBTSerializable> implements Capability.IStorage<V>
	{
		@Override
		public NBTBase writeNBT(Capability<V> capability, V instance, EnumFacing side)
		{
			return ((INBTSerializable)instance).serializeNBT();
		}
		
		@Override
		public void readNBT(Capability<V> capability, V instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound)nbt);
		}
	}
	
}
