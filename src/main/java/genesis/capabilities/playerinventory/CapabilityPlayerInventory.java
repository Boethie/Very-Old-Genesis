package genesis.capabilities.playerinventory;

import genesis.capabilities.GenesisCapabilities;
import genesis.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityPlayerInventory
{

	@CapabilityInject(IPlayerInventory.class)
	public static final Capability<IPlayerInventory> PLAYER_INVENTORY_CAPABILITY = null;

	public static final EnumFacing DEFAULT_FACING = null;

	public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "PlayerInventory");

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IPlayerInventory.class, new GenesisCapabilities.Storage<>(), () -> new PlayerInventory());
	}

	@EventBusSubscriber
	public static class EventHandler
	{
		@SubscribeEvent
		public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
		{
			if (event.getObject() instanceof EntityLivingBase)
			{
				event.addCapability(ID, new GenesisCapabilities.Provider<>(PLAYER_INVENTORY_CAPABILITY, DEFAULT_FACING));
			}
		}

		@SubscribeEvent
		public static void playerClone(PlayerEvent.Clone event)
		{
			final IPlayerInventory oldHandler = event.getOriginal().getCapability(PLAYER_INVENTORY_CAPABILITY, null);
			final IPlayerInventory newHandler = event.getEntityPlayer().getCapability(PLAYER_INVENTORY_CAPABILITY, null);
			
			newHandler.deserializeNBT(oldHandler.serializeNBT());
		}
	}

}
