package genesis.block.tileentity.portal;

import genesis.block.BlockGenesis;
import genesis.common.GenesisConfig;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisDimensions;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockGenesisPortal extends BlockGenesis
{
	public BlockGenesisPortal()
	{
		super(Material.air);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
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
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if (!world.isRemote)
		{
			if (entity.timeUntilPortal <= 0)
			{
				int dimension = GenesisConfig.genesisDimId;
				
				if (entity.dimension == dimension)
				{
					dimension = 0;
				}
				
				Vec3 entityMiddle = entity.getPositionVector().addVector(0, entity.getEyeHeight() / 2, 0);
				AxisAlignedBB blockBounds = new AxisAlignedBB(pos, pos.add(1, 1, 1));
				
				if (blockBounds.isVecInside(entityMiddle))
				{
					GenesisDimensions.teleportToDimension(entity, dimension);
				}
			}
		}
	}
}
