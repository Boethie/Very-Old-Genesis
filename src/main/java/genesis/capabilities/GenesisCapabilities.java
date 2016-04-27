package genesis.capabilities;

import genesis.util.Constants;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GenesisCapabilities
{
	public static final ResourceLocation DIMENSION_PLAYERS_ID = new ResourceLocation(Constants.MOD_ID, "dimension_players");
	@CapabilityInject(IDimensionPlayers.class)
	public static final Capability<IDimensionPlayers> DIMENSION_PLAYERS = null;
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IDimensionPlayers.class, new IDimensionPlayers.Storage(), IDimensionPlayers.Impl.class);
		MinecraftForge.EVENT_BUS.register(new GenesisCapabilities());
	}
	
	@SubscribeEvent
	public void onEntityConstruct(AttachCapabilitiesEvent event)
	{
		event.addCapability(DIMENSION_PLAYERS_ID, new SerializingCapabilityProvider<>(DIMENSION_PLAYERS));
	}
	
	public static class SerializingCapabilityProvider<V> implements ICapabilitySerializable<NBTBase>
	{
		private final Capability<V> wrapping;
		private V instance;
		
		public SerializingCapabilityProvider(Capability<V> capability)
		{
			this.wrapping = capability;
			this.instance = capability.getDefaultInstance();
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return capability == wrapping;
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			return capability == wrapping ? wrapping.cast(instance) : null;
		}
		
		@Override
		public NBTBase serializeNBT()
		{
			return wrapping.getStorage().writeNBT(wrapping, instance, null);
		}
		
		@Override
		public void deserializeNBT(NBTBase nbt)
		{
			wrapping.getStorage().readNBT(wrapping, instance, null, nbt);
		}
	}
}
