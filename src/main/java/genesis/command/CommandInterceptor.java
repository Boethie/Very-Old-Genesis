package genesis.command;

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
			if (sender.getEntityWorld().provider.getClass() == WorldProviderGenesis.class)
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
							time = parseInt(value, 0);
						}
						
						setTime(sender, time);
						notifyOperators(sender, this, "commands.time.set", time);
						event.setCanceled(true);
					}
					else if (mode.equals("add"))
					{
						int add = parseInt(value);
						addTime(sender, add);
						notifyOperators(sender, this, "commands.time.added", add);
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
	
	@Override
	protected void setTime(ICommandSender sender, int time)
	{
		boolean genesis = sender.getEntityWorld().provider.getClass() == WorldProviderGenesis.class;
		
		for (WorldServer world : MinecraftServer.getServer().worldServers)
			if (world.provider.getClass() == WorldProviderGenesis.class ? genesis : !genesis)
				world.setWorldTime(time);
	}
	
	@Override
	protected void addTime(ICommandSender sender, int time)
	{
		boolean genesis = sender.getEntityWorld().provider.getClass() == WorldProviderGenesis.class;
		
		for (WorldServer world : MinecraftServer.getServer().worldServers)
			if (world.provider.getClass() == WorldProviderGenesis.class ? genesis : !genesis)
				world.setWorldTime(world.getWorldTime() + time);
	}
}
