package genesis.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TeleporterGenesis extends Teleporter
{

	private final WorldServer server;
	
	public TeleporterGenesis(WorldServer worldIn)
	{
		super(worldIn);
		this.server = worldIn;
	}
	
	// Move the Entity to the portal
	public void teleport(final Entity entity, final World world) {

		// Setup Variables
		final EntityPlayerMP playerMP = (EntityPlayerMP) entity;
		BlockPos pos = world.getSpawnPoint();
		entity.setPosition(pos.getX(), pos.getY(), pos.getZ());

		// Freeze motion
		entity.motionX = entity.motionY = entity.motionZ = 0.0D;
		entity.setPosition(pos.getX(), pos.getY(), pos.getZ());  // silly to do this multiple time,s but it kept offseting entity until this was done

		// Set Dimension
		if (entity.worldObj.provider.getDimensionId() != world.provider.getDimensionId())
		{

			playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, world.provider.getDimensionId(), this);

		}

		entity.setPosition(pos.getX(), pos.getY(), pos.getZ()); // silly to do this multiple time,s but it kept offseting entity until this was done

	}

	@Override
	public void removeStalePortalLocations(final long par1)
	{
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw)
	{
		// TODO Auto-generated method stub
		super.placeInPortal(entityIn, rotationYaw);
	}

	@Override
	public boolean makePortal(Entity p_85188_1_)
	{
		// TODO Auto-generated method stub
		return super.makePortal(p_85188_1_);
	}
	
	

}
