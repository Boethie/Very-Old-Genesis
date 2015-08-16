package genesis.common;

import genesis.block.BlockGenesisPebble.PebbleBreakMessage;
import genesis.entity.flying.EntityMeganeura.MeganeuraUpdateMessage;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class GenesisNetwork extends SimpleNetworkWrapper
{
	protected int currentID = 0;
	
	public GenesisNetwork(String channelName)
	{
		super(channelName);
	}
	
	public <Q extends IMessage, A extends IMessage> void registerMessage(IMessageHandler<? super Q, ? extends A> messageHandler, Class<Q> requestMessageType, Side side)
	{
		registerMessage(messageHandler, requestMessageType, currentID++, side);
	}
	
	public void registerMessages()
	{
		registerMessage(new PebbleBreakMessage.Handler(), PebbleBreakMessage.class, Side.SERVER);
		registerMessage(new MeganeuraUpdateMessage.Handler(), MeganeuraUpdateMessage.class, Side.CLIENT);
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
			Set<EntityPlayer> players = ((WorldServer) entity.worldObj).getEntityTracker().getTrackingPlayers(entity);
			
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
