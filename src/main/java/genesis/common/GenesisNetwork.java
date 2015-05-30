package genesis.common;

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
}
