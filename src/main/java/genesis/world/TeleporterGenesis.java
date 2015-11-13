package genesis.world;

import java.util.*;

import com.google.common.collect.Maps;

import genesis.block.tileentity.portal.*;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.portal.GenesisPortal;
import genesis.util.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class TeleporterGenesis extends Teleporter
{
	protected final WorldServer world;
	protected final Map<BlockPos, PortalPosition> cache = Maps.newHashMap();
	protected final Random random;
	
	protected GenesisPortal portal;
	
	public TeleporterGenesis(WorldServer world)
	{
		super(world);
		
		this.world = world;
		random = new Random(world.getSeed());
		
		if (!world.customTeleporters.contains(this))
		{
			world.customTeleporters.add(this);
		}
	}
	
	public void setOriginatingPortal(GenesisPortal portal)
	{
		this.portal = portal;
	}
	
	@Override
	public void placeInPortal(Entity entity, float rotationYaw)
	{
		if (!placeInExistingPortal(entity, rotationYaw))
		{
			makePortal(entity);	// TODO: Handle when this process does not create a portal block, in case something weird happens.
			placeInExistingPortal(entity, rotationYaw);
		}
		
		setOriginatingPortal(null);
	}
	
	@Override
	public boolean placeInExistingPortal(Entity entity, float rotationYaw)
	{
		BlockPos center = entity.getPosition();
		
		BlockPos portalPos = null;
		
		if (cache.containsKey(center))
		{
			portalPos = cache.get(center);
			
			if (world.getBlockState(portalPos).getBlock() != GenesisBlocks.portal)
			{
				portalPos = null;
			}
		}
		
		if (portalPos == null)
		{
			double portalDistance = -1;
			
			for (BlockPos pos : WorldUtils.getAreaWithHeight(center, 128, 0, world.getActualHeight()))
			{
				double checkDistance = pos.distanceSq(center);
				
				if ((portalDistance < 0 || checkDistance < portalDistance) && world.getBlockState(pos).getBlock() == GenesisBlocks.portal)
				{
					portalDistance = checkDistance;
					portalPos = pos;
				}
			}
		}
		else
		{
			Genesis.logger.info("Cached portal found from location " + center + " at destination location " + portalPos);
		}
		
		if (portalPos != null)
		{
			cache.put(center, new PortalPosition(portalPos, world.getTotalWorldTime()));
			Genesis.logger.info("Caching portal position from location " + center + " to " + portalPos);
			
			Vec3 to = new Vec3(portalPos.getX() + 0.5, portalPos.getY(), portalPos.getZ() + 0.5);
			
			if (!world.getBlockState(portalPos.down()).getBlock().isVisuallyOpaque())
			{
				to = to.subtract(0, entity.getEyeHeight() / 2, 0);
			}
			
			entity.setLocationAndAngles(to.xCoord, to.yCoord, to.zCoord, entity.rotationYaw, entity.rotationPitch);
			entity.setVelocity(0, 0, 0);
			
			//world.playSoundEffect(portalPos.getX() + 0.5, portalPos.getY() + 0.5, portalPos.getZ() + 0.5,
			//		Constants.ASSETS_PREFIX + "portal.exit", 0.9F + world.rand.nextFloat() * 0.2F, 0.8F + world.rand.nextFloat() * 0.4F);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean makePortal(Entity entity)
	{
		GenesisPortal newPortal = GenesisPortal.fromCenterBlock(world, entity.getPosition());
		
		if (!newPortal.setPlacementPosition(world))
		{
			newPortal.createPlatform(world);
		}
		
		if (portal != null)
		{
			newPortal.duplicatePortal(world, portal);
		}
		else
		{
			newPortal.makePortal(world, random);
		}
		
		return true;
	}
	
	@Override
	public void removeStalePortalLocations(long worldTime)
	{
		if (worldTime % 100 == 0)
		{
			long killTime = worldTime - 600;
			Iterator<Map.Entry<BlockPos, PortalPosition>> iterator = cache.entrySet().iterator();
			
			while (iterator.hasNext())
			{
				Map.Entry<BlockPos, PortalPosition> entry = iterator.next();
				
				if (entry.getValue().lastUpdateTime < killTime)
				{
					iterator.remove();
					Genesis.logger.info("Removed portal position from " + entry.getKey() + " to destination " + entry.getValue() + ".");
				}
			}
		}
	}
}
