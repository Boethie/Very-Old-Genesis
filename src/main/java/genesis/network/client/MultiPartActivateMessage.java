package genesis.network.client;

import genesis.util.GenesisByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MultiPartActivateMessage implements IMessage
{
	private BlockPos pos;
	private short part;
	private EnumHand hand;
	
	public MultiPartActivateMessage() { }
	
	public MultiPartActivateMessage(BlockPos pos, short part, EnumHand hand)
	{
		this.pos = pos;
		this.part = part;
		this.hand = hand;
	}
	
	public MultiPartActivateMessage(BlockPos pos, int part, EnumHand hand)
	{
		this(pos, (short) part, hand);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		
		part = buf.readShort();
		
		hand = GenesisByteBufUtils.readEnum(buf, EnumHand.class);
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		
		buf.writeShort(part);
		
		GenesisByteBufUtils.writeEnum(buf, hand);
	}
	
	public static class Handler implements IMessageHandler<MultiPartActivateMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final MultiPartActivateMessage msg, final MessageContext ctx)
		{
			final EntityPlayer player = ctx.getServerHandler().playerEntity;
			final WorldServer world = (WorldServer) player.worldObj;
			world.addScheduledTask(() ->
			{
				if (!world.isAirBlock(msg.pos))
				{
					IBlockState state = world.getBlockState(msg.pos);
					
					if (state.getBlock() instanceof MultiPartBlock)
					{
						((MultiPartBlock) state.getBlock())
								.activate(state,
										world, msg.pos, msg.part,
										player, player.getHeldItem(msg.hand), msg.hand);
					}
				}
			});
			
			return null;
		}
	}
}
