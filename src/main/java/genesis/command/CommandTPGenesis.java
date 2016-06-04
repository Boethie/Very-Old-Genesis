package genesis.command;

import genesis.common.GenesisDimensions;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;

import com.google.common.collect.ImmutableList;

public class CommandTPGenesis implements ICommand
{
	private static final List<String> ALIASES = ImmutableList.of("tpg", "tpgenesis", "ForwardToThePast", "forwardtothepast");
	
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
		return ALIASES;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (sender instanceof Entity)
		{
			Entity entity = (Entity) sender;
			DimensionType dimension = GenesisDimensions.GENESIS_DIMENSION;
			
			if (entity.dimension == dimension.getId())
			{
				dimension = DimensionType.OVERWORLD;
			}
			
			GenesisDimensions.teleportToDimension(entity, null, dimension, true);
			// Currently forces teleportation without resetting player.
			// TODO: Figure out permissions.
		}
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		return null;
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return false;
	}
	
	@Override
	public int compareTo(ICommand o)
	{
		return 0;
	}
}
