package genesis.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;

public class PlayerWorldState
{
	private int dim;
	
	private double x;
	private double y;
	private double z;
	
	private double motionX;
	private double motionY;
	private double motionZ;
	
	private float yaw;
	private float pitch;
	
	private float fallDistance;
	
	private GameType gamemode;
	private boolean isFlying;
	
	private PlayerWorldState(int dim,
			double x, double y, double z,
			double motionX, double motionY, double motionZ,
			float yaw, float pitch,
			float fallDistance,
			GameType gamemode, boolean isFlying)
	{
		this.dim = dim;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		
		this.yaw = yaw;
		this.pitch = pitch;
		
		this.fallDistance = fallDistance;
		this.isFlying = isFlying;
	}
	
	public PlayerWorldState(DimensionType dim,
			double x, double y, double z,
			double motionX, double motionY, double motionZ,
			float yaw, float pitch,
			float fallDistance,
			GameType gamemode, boolean isFlying)
	{
		this(dim.getId(), x, y, z, motionX, motionY, motionZ, yaw, pitch, fallDistance, gamemode, isFlying);
	}
	
	public PlayerWorldState(EntityPlayerMP player)
	{
		this(player.dimension,
				player.posX, player.posY, player.posZ,
				player.motionX, player.motionY, player.motionZ,
				player.rotationYaw, player.rotationPitch,
				player.fallDistance,
				player.interactionManager.getGameType(), player.capabilities.isFlying);
	}
	
	public void restore(EntityPlayerMP player)
	{
		player.dimension = dim;
		
		player.connection.setPlayerLocation(x, y, z, yaw, pitch);
		
		player.motionX = motionX;
		player.motionY = motionY;
		player.motionZ = motionZ;
		player.connection.sendPacket(new SPacketEntityVelocity(player));
		
		player.fallDistance = fallDistance;
		
		player.capabilities.isFlying = isFlying;	// Will be sent to client by setGameType.
		player.interactionManager.setGameType(gamemode);
	}
}
