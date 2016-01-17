package genesis.block;

import java.util.List;

import genesis.common.GenesisCreativeTabs;
import genesis.util.AABBUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class BlockGenesisFence extends BlockFence
{
	protected final float poleRadius;
	protected final float poleHeight;
	protected final float sideRadius;
	protected final float sideHeight;
	protected final float fakeHeight;
	
	public BlockGenesisFence(Material material, float poleRadius, float poleHeight, float sideRadius, float sideHeight, float fakeHeight)
	{
		super(material);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		this.poleRadius = poleRadius;
		this.poleHeight = poleHeight;
		this.sideRadius = sideRadius;
		this.sideHeight = sideHeight;
		this.fakeHeight = fakeHeight;
	}
	
	public BlockGenesisFence(Material material, float radius, float height, float fakeHeight)
	{
		this(material, radius, height, radius, height, fakeHeight);
	}
	
	private static void addBox(List<AxisAlignedBB> list, AxisAlignedBB mask, AxisAlignedBB bb)
	{
		if (mask.intersectsWith(bb))
			list.add(bb);
	}
	
	public boolean canConnectTo(IBlockAccess world, BlockPos fencePos, EnumFacing side)
	{
		BlockPos pos = fencePos.offset(side);
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if (block instanceof BlockGenesisFence)
			return true;
		
		if (block instanceof BlockGenesisWall)
			return true;
		
		if (block instanceof BlockFenceGate)
			return true;
		
		if (block.isSideSolid(world, pos, side.getOpposite()))
			return true;
		
		//state = block.getActualState(state, world, pos);
		
		return false;
	}
	
	// All code below is duplicated in BlockGenesisWall.
	@Override
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		// Make particles collide with the actual top of the fence, rather than the raised version.
		boolean realHeight = fakeHeight <= 0
							|| collidingEntity instanceof EntityFX
							|| collidingEntity instanceof EntityFallingBlock;
		
		AxisAlignedBB base = new AxisAlignedBB(0.5, 0, 0.5, 0.5, 0, 0.5)
				.offset(pos.getX(), pos.getY(), pos.getZ());
		
		addBox(list, mask, base.addCoord(0, realHeight ? poleHeight : fakeHeight, 0).expand(poleRadius, 0, poleRadius));
		
		double height = realHeight ? sideHeight : fakeHeight;
		
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			if (canConnectTo(world, pos, facing))
			{
				AxisAlignedBB sideBB = AABBUtils.offset(base.addCoord(0, height, 0), facing, poleRadius);
				sideBB = AABBUtils.extend(sideBB, facing, 0.5 - poleRadius);
				sideBB = AABBUtils.expand(sideBB, facing.rotateY(), sideRadius);
				addBox(list, mask, sideBB);
			}
		}
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		AxisAlignedBB bb = new AxisAlignedBB(0.5, 0, 0.5, 0.5, poleHeight, 0.5)
				.expand(poleRadius, 0, poleRadius);
		boolean connected = false;
		
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			if (canConnectTo(world, pos, facing))
			{
				if (!connected)
				{
					bb = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ,
											bb.maxX, Math.max(bb.maxY, sideHeight), bb.maxZ);
					connected = true;
				}
				
				bb = AABBUtils.extend(bb, facing, 0.5 - poleRadius);
				
				switch (facing.getAxis())
				{
				case X:
					bb = new AxisAlignedBB(Math.min(bb.minX, 0.5 - sideRadius), bb.minY, bb.minZ,
											Math.max(bb.maxX, 0.5 + sideRadius), bb.maxY, bb.maxZ);
					break;
				case Z:
					bb = new AxisAlignedBB(bb.minX, bb.minY, Math.min(bb.minZ, 0.5 - sideRadius),
											bb.maxX, bb.maxY, Math.max(bb.maxZ, 0.5 + sideRadius));
					break;
				default:
					break;
				}
			}
		}
		
		this.minX = bb.minX;
		this.minY = bb.minY;
		this.minZ = bb.minZ;
		this.maxX = bb.maxX;
		this.maxY = bb.maxY;
		this.maxZ = bb.maxZ;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack held = player.getHeldItem();
		
		if (held != null && held.getItem() instanceof ItemLead)
			return world.isRemote ? true : ItemLead.attachToFence(player, world, pos);
		
		return false;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.withProperty(NORTH, canConnectTo(world, pos, EnumFacing.NORTH))
				.withProperty(EAST, canConnectTo(world, pos, EnumFacing.EAST))
				.withProperty(SOUTH, canConnectTo(world, pos, EnumFacing.SOUTH))
				.withProperty(WEST, canConnectTo(world, pos, EnumFacing.WEST));
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		Block block = world.getBlockState(pos).getBlock();
		
		if (block.doesSideBlockRendering(world, pos, side))
			return false;
		
		if (block == this)
			return false;
		
		return true;
	}
}
