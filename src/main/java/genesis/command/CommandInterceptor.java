package genesis.command;

import genesis.common.GenesisDimensions;
import genesis.world.WorldProviderGenesis;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandTime;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandInterceptor extends CommandTime
{
	@SubscribeEvent
	public void onCommand(CommandEvent event)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		ICommandSender sender = event.getSender();
		Class<? extends ICommand> commandClass = event.getCommand().getClass();
		String[] args = event.getParameters();

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

						switch (value) {
							case "day":
								time = (int) (0.04167F * WorldProviderGenesis.DAY_LENGTH);
								break;
							case "night":
								time = (int) (0.54167F * WorldProviderGenesis.DAY_LENGTH);
								break;
							default:
								time = parseInt(value);
								break;
						}

						setAllWorldTimes(server, sender, time);
						event.setCanceled(true);
					}
					else if (mode.equals("add"))
					{
						int add = parseInt(value);
						addTime(server, sender, add);
						event.setCanceled(true);
					}
				}
			}
		}
		catch (CommandException ex)
		{
			TextComponentTranslation message = new TextComponentTranslation(ex.getMessage(), ex.getErrorObjects());
			message.getStyle().setColor(TextFormatting.RED);
			sender.addChatMessage(message);
			event.setCanceled(true);
		}
	}

	protected int getDayLength(boolean genesis)
	{
		return genesis ? WorldProviderGenesis.DAY_LENGTH : 24000;
	}

	protected void setAllWorldTimes(MinecraftServer server, ICommandSender sender, int time)
	{
		boolean genesis = GenesisDimensions.isGenesis(sender.getEntityWorld());

		int dayLength = getDayLength(genesis);
		time %= dayLength;
		if (time < 0)
			time += dayLength;

		notifyCommandListener(sender, this, "commands.time.set", time);

		for (WorldServer world : server.worldServers)
			if (GenesisDimensions.isGenesis(world) == genesis)
				world.setWorldTime(time);
	}

	protected void addTime(MinecraftServer server, ICommandSender sender, int time)
	{
		boolean genesis = GenesisDimensions.isGenesis(sender.getEntityWorld());

		for (WorldServer world : server.worldServers)
		{
			if (GenesisDimensions.isGenesis(world) == genesis)
			{
				long worldTime = world.getWorldTime() + time;
				int dayLength = getDayLength(genesis);
				if (worldTime < 0)
					worldTime = (worldTime > -dayLength ? dayLength : 0) + (worldTime % dayLength);

				world.setWorldTime(worldTime);
			}
		}

		notifyCommandListener(sender, this, "commands.time.added", time);
	}
}
