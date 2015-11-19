package genesis.command;

import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

public class CommandTPGenesis implements ICommand
{
	@Override
	public String getCommandName()
	{
		return "BackToTheFuture";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "command.genesis.tpgenesis.usage";
	}
	
	@Override
	public List<String> getCommandAliases()
	{
		return Lists.newArrayList("tpg", "tpgenesis", "ForwardToThePast", "forwardtothepast");
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if (sender instanceof Entity)
		{
			Entity entity = (Entity) sender;
			int dimension = GenesisConfig.genesisDimId;
			
			if (entity.dimension == dimension)
			{
				dimension = 0;
			}
			
			GenesisDimensions.teleportToDimension(entity, null, dimension, true);
			// Currently forces teleportation without resetting player.
			// TODO: Figure out permissions.
		}
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return true;
	}
	
	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		return null;
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return false;
	}
	
	@Override
	public int compareTo(Object o)
	{
		return 0;
	}
}
