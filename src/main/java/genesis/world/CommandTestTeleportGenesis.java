package genesis.world;

import genesis.common.Genesis;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandTestTeleportGenesis extends CommandBase{

	public String getName()
	{
		return "genesisteleport";
	}

	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.genesisteleport.usage";
	}

	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		if(sender instanceof EntityPlayerMP) 
		{
			EntityPlayerMP player = (EntityPlayerMP) sender;

			if(player.dimension != Genesis.dimensionId) 
			{
				MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, Genesis.dimensionId, new TeleporterGenesis(MinecraftServer.getServer().worldServerForDimension(player.dimension)));
			}
			else 
			{
				MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, 0, new TeleporterGenesis(MinecraftServer.getServer().worldServerForDimension(player.dimension)));
			}
		}
	}

}
