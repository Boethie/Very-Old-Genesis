package genesis.command;

import genesis.common.GenesisDimensions;
import genesis.world.WorldProviderGenesis;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandTime;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandInterceptor extends CommandTime
{
	@SubscribeEvent
	public void onCommand(CommandEvent event)
	{
		ICommandSender sender = event.sender;
		Class<? extends ICommand> commandClass = event.command.getClass();
		String[] args = event.parameters;
		
		try
		{
			if (GenesisDimensions.isGenesis(sender.getEntityWorld()))
			{
				if (commandClass == CommandTime.class && args.length >= 2)
				{
					String mode = args[0];
					String value = args[1];
					
					if (mode.equals("set"))
					{
						int time;
						
						if (value.equals("day"))
						{
							time = (int) (0.04167F * WorldProviderGenesis.DAY_LENGTH);
						}
						else if (value.equals("night"))
						{
							time = (int) (0.54167F * WorldProviderGenesis.DAY_LENGTH);
						}
						else
						{
							time = parseInt(value);
						}
						
						setTime(sender, time);
						event.setCanceled(true);
					}
					else if (mode.equals("add"))
					{
						int add = parseInt(value);
						addTime(sender, add);
						event.setCanceled(true);
					}
				}
			}
		}
		catch (CommandException ex)
		{
			ChatComponentTranslation message = new ChatComponentTranslation(ex.getMessage(), ex.getErrorObjects());
			message.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(message);
			event.setCanceled(true);
		}
	}
	
	protected int getDayLength(boolean genesis)
	{
		return genesis ? WorldProviderGenesis.DAY_LENGTH : 24000;
	}
	
	@Override
	protected void setTime(ICommandSender sender, int time)
	{
		boolean genesis = GenesisDimensions.isGenesis(sender.getEntityWorld());
		
		int dayLength = getDayLength(genesis);
		time %= dayLength;
		if (time < 0)
			time += dayLength;
		
		notifyOperators(sender, this, "commands.time.set", time);
		
		for (WorldServer world : MinecraftServer.getServer().worldServers)
			if (GenesisDimensions.isGenesis(world) ? genesis : !genesis)
				world.setWorldTime(time);
	}
	
	@Override
	protected void addTime(ICommandSender sender, int time)
	{
		boolean genesis = GenesisDimensions.isGenesis(sender.getEntityWorld());
		
		for (WorldServer world : MinecraftServer.getServer().worldServers)
		{
			if (GenesisDimensions.isGenesis(world) ? genesis : !genesis)
			{
				long worldTime = world.getWorldTime() + time;
				int dayLength = getDayLength(genesis);
				if (worldTime < 0)
					worldTime = (worldTime > -dayLength ? dayLength : 0) + (worldTime % dayLength);
				
				world.setWorldTime(worldTime);
			}
		}
		
		notifyOperators(sender, this, "commands.time.added", time);
	}
}
