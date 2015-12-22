package genesis.command;

import genesis.world.WorldProviderGenesis;
import net.minecraft.command.CommandTime;
import net.minecraft.command.ICommand;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandInterceptor extends CommandTime
{
	@SubscribeEvent
	public void onCommand(CommandEvent event)
	{
		Class<? extends ICommand> commandClass = event.command.getClass();
		String[] args = event.parameters;
		
		if (event.sender.getEntityWorld().provider.getClass() == WorldProviderGenesis.class)
		{
			if (commandClass == CommandTime.class)
			{
				if (args.length >= 2 && args[0].equals("set"))
				{
					float timeF = -1;
					
					if (args[1].equals("day"))
					{
						timeF = 0.04167F;
					}
					else if (args[1].equals("night"))
					{
						timeF = 0.54167F;
					}
					
					if (timeF != -1)
					{
						timeF *= WorldProviderGenesis.DAY_LENGTH;
						int time = (int) timeF % WorldProviderGenesis.DAY_LENGTH;
						setTime(event.sender, time);
						notifyOperators(event.sender, event.command, "commands.time.set", time);
						event.setCanceled(true);
					}
				}
			}
		}
	}
}
