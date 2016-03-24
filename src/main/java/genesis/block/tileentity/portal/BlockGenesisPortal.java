package genesis.block.tileentity.portal;

import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import genesis.portal.GenesisPortal;
import genesis.stats.GenesisAchievements;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockGenesisPortal extends Block
{
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
	
	@Override
	public int getRenderType()
	{
		return 2;
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
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (rand.nextInt(100) == 0)
		{
			worldIn.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					Constants.ASSETS_PREFIX + "portal.ambient", 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}
	}
}
