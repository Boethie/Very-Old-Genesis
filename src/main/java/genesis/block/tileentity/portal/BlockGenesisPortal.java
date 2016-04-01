package genesis.block.tileentity.portal;

import genesis.common.GenesisDimensions;
import genesis.portal.GenesisPortal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.*;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockGenesisPortal extends Block
{
	public static final float RANGE = 1.25F;
	
	public BlockGenesisPortal()
	{
		super(Material.air);
		
		setLightLevel(1);
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
				DimensionType dimension = GenesisDimensions.GENESIS_DIMENSION;
				
				if (entity.dimension == dimension.getId())
				{
					dimension = DimensionType.OVERWORLD;
				}
				
				Vec3d entityCenter = entity.getPositionVector().addVector(0, entity.getEyeHeight() / 2, 0);
				Vec3d blockCenter = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				
				if (entityCenter.squareDistanceTo(blockCenter) <= RANGE * RANGE)
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
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos,
			AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
	}
	
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end)
	{
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if (rand.nextInt(100) == 0)
		{
			/*world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					Constants.ASSETS_PREFIX + "portal.ambient", 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);*/
		}
	}
}
