package genesis.block.tileentity.portal;

import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import genesis.portal.GenesisPortal;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockGenesisPortal extends Block
{
	public BlockGenesisPortal()
	{
		super(Material.air);
		
		setLightLevel(1);
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state)
	{
		return null;
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, BlockPos pos, Vec3 start, Vec3 end)
	{
		return null;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if (!world.isRemote)
		{
			GenesisPortal portal = GenesisPortal.fromPortalBlock(world, pos);
			portal.updatePortalStatus(world);	// TODO: Make this check if the portal is active instead of disabling it.
			// So that map authors can use the attraction force creatively without it randomly being removed.
			
			if ((state = world.getBlockState(pos)).getBlock() == this)
			{
				int dimension = GenesisConfig.genesisDimId;
				
				if (entity.dimension == dimension)
				{
					dimension = 0;
				}
				
				final float tpDist = 1.25F;
				Vec3 entityCenter = entity.getPositionVector().addVector(0, entity.getEyeHeight() / 2, 0);
				Vec3 blockCenter = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				
				if (entityCenter.squareDistanceTo(blockCenter) <= tpDist * tpDist)
				{
					if (entity.timeUntilPortal > 0)
					{
						entity.timeUntilPortal = GenesisPortal.COOLDOWN;
					}
					else
					{
						GenesisDimensions.teleportToDimension(entity, portal, dimension, false);
					}
				}
			}
		}
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntityGenesisPortal createTileEntity(World world, IBlockState state)
	{
		return new TileEntityGenesisPortal();
	}
}
