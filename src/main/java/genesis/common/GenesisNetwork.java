package genesis.common;

import java.util.*;

import genesis.block.BlockPebble.PebbleBreakMessage;
import genesis.block.tileentity.TileEntityKnapper.KnappingSlotMessage;
import genesis.common.GenesisSounds.MovingEntitySoundMessage;
import genesis.entity.living.flying.EntityMeganeura.MeganeuraUpdateMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.NetworkRegistry.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class GenesisNetwork extends SimpleNetworkWrapper
{
	protected int currentID = 0;
	
	public GenesisNetwork(String channelName)
	{
		super(channelName);
	}
	
	public <Q extends IMessage, A extends IMessage> void registerMessage(IMessageHandler<? super Q, ? extends A> messageHandler, Class<Q> requestMessageType, Side handlerSide)
	{
		registerMessage(messageHandler, requestMessageType, currentID++, handlerSide);
	}
	
	public void registerMessages()
	{
		registerMessage(new MovingEntitySoundMessage.Handler(), MovingEntitySoundMessage.class, Side.CLIENT);
		registerMessage(new PebbleBreakMessage.Handler(), PebbleBreakMessage.class, Side.SERVER);
		registerMessage(new MeganeuraUpdateMessage.Handler(), MeganeuraUpdateMessage.class, Side.CLIENT);
		registerMessage(new KnappingSlotMessage.Handler(), KnappingSlotMessage.class, Side.SERVER);
	}
	
	public void sendToAllAround(IMessage message, World world, double x, double y, double z, double range)
	{
		sendToAllAround(message, new TargetPoint(world.provider.getDimensionId(), x, y, z, range));
	}
	
	public void sendToAllAround(IMessage message, World world, Vec3 vec, double range)
	{
		sendToAllAround(message, world, vec.xCoord, vec.yCoord, vec.zCoord, range);
	}
	
	public void sendToAllTracking(IMessage message, Entity entity)
	{
		if (!entity.worldObj.isRemote)
		{
			Set<? extends EntityPlayer> players = ((WorldServer) entity.worldObj).getEntityTracker().getTrackingPlayers(entity);
			
			for (EntityPlayer player : players)
			{
				sendTo(message, (EntityPlayerMP) player);
			}
		}
		else
		{
			Genesis.logger.warn("Something attempted to send a message to other players from the client.", new Exception());
		}
	}
}
