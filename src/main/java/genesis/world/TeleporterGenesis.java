package genesis.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterGenesis extends Teleporter 
{

	public TeleporterGenesis(WorldServer worldIn) 
	{
		super(worldIn);
	}

	public void placeInPortal(Entity entityIn, float rotationYaw)
	{
		entityIn.setLocationAndAngles(0, 256, 0, entityIn.rotationYaw, 0.0f);
	}

}
