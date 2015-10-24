package genesis.command;

import genesis.world.TeleporterGenesis;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import com.google.common.collect.Lists;

public class CommandTPGenesis implements ICommand
{
	@Override
	public int compareTo(Object o)
	{
		return 0;
	}
	
	@Override
	public String getCommandName()
	{
		return "BackToTheFuture";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "Really?";
	}
	
	@Override
	public List getCommandAliases()
	{
		return Lists.newArrayList("tpg", "tpgenesis", "ForwardToThePast", "forwardtothepast");
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		int dimension = 37;
		
		if (args.length > 0 && args[0].contains("overworld"))
		{
			dimension = 0;
		}
		
		if(DimensionManager.getWorld(dimension) == null)
		{
			DimensionManager.initDimension(dimension);
		}
		WorldServer server = DimensionManager.getWorld(dimension);
		EntityPlayerMP player = (EntityPlayerMP) sender;
		new TeleporterGenesis(player.mcServer.worldServerForDimension(player.worldObj.provider.getDimensionId())).teleport(player, server);
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return sender instanceof EntityPlayerMP;
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		return null;
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return false;
	}
}
