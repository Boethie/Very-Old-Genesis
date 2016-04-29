package genesis.network.client;

import io.netty.buffer.ByteBuf;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MultiPartBreakMessage implements IMessage
{
	private BlockPos pos;
	private short part;
	
	public MultiPartBreakMessage() { }
	
	public MultiPartBreakMessage(BlockPos pos, short part)
	{
		this.pos = pos;
		this.part = part;
	}
	
	public MultiPartBreakMessage(BlockPos pos, int part)
	{
		this(pos, (short) part);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		
		part = buf.readShort();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		
		buf.writeShort(part);
	}
	
	public static class Handler implements IMessageHandler<MultiPartBreakMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final MultiPartBreakMessage msg, final MessageContext ctx)
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
								.removePart(state,
										world, msg.pos, msg.part,
										player, state.getBlock().canHarvestBlock(world, msg.pos, player));
					}
				}
			});
			
			return null;
		}
	}
}
