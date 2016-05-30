package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 9/14/2015.
 * Heavily modified by Zaggy1024 on 11/10/2015.
 */
public class BlockTallTorch extends Block
{
	public enum Part implements IStringSerializable
	{
		TOP("top"),
		BOTTOM("bottom");
		
		final String name;
		
		Part(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
	
	public static final PropertyEnum<Part> PART = PropertyEnum.create("part", Part.class);
	public static final PropertyDirection FACING = BlockTorch.FACING;
	
	public BlockTallTorch()
	{
		super(Material.circuits);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		setDefaultState(getBlockState().getBaseState().withProperty(PART, Part.BOTTOM));
		
		setLightLevel(0.9375F);
		
		setSoundType(SoundType.WOOD);
		
		setTickRandomly(true);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART, FACING);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, PART, FACING);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, PART, FACING);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return state.getValue(PART) == Part.BOTTOM ? super.getItemDropped(state, rand, fortune) : null;
	}
	
	public boolean canTorchStay(World world, BlockPos pos, IBlockState state, boolean placing)
	{
		BlockPos below = pos.down();
		IBlockState stateBelow = world.getBlockState(below);
		
		if (state.getValue(PART) == Part.TOP)
		{
			if (placing)
				return world.isBlockLoaded(below) && stateBelow.getBlock().isReplaceable(world, below)
						&& canTorchStay(world, below, state.withProperty(PART, Part.BOTTOM), true);
			else
				return stateBelow.getBlock() == this && stateBelow.getValue(PART) == Part.BOTTOM;
		}
		
		BlockPos above = pos.up();
		IBlockState stateAbove = world.getBlockState(pos.up());
		
		if (placing && stateAbove.getBlock().isReplaceable(world, above) ||
			stateAbove.getBlock() == this && stateAbove.getValue(PART) == Part.TOP)
		{
			if (placing && !world.isBlockLoaded(pos.up()))
				return false;
			
			EnumFacing facing = state.getValue(FACING);
			
			if (world.isSideSolid(pos.offset(facing.getOpposite()), facing, true))
				return true;
			
			if (facing == EnumFacing.UP && stateBelow.getBlock().canPlaceTorchOnTop(stateBelow, world, pos))
				return true;
		}
		
		return false;
	}
	
	public boolean canPlaceTorchAt(World world, BlockPos pos, Part part)
	{
		IBlockState state = getDefaultState().withProperty(PART, part);
		
		for (EnumFacing facing : EnumFacing.values())
		{
			if (FACING.getAllowedValues().contains(facing) && canTorchStay(world, pos, state.withProperty(FACING, facing), true))
			{
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return canPlaceTorchAt(world, pos, Part.BOTTOM) ||
				canPlaceTorchAt(world, pos.down(), Part.TOP);
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos,
			EnumFacing hitSide, float hitX, float hitY, float hitZ,
			int metadata, EntityLivingBase placer)
	{
		IBlockState out = getDefaultState().withProperty(PART, Part.BOTTOM);
		
		if (!world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()))
		{
			out = out.withProperty(PART, Part.TOP);
		}
		
		EnumFacing[] priority = null;
		
		switch (hitSide)
		{
		case UP:
		case DOWN:
			priority = new EnumFacing[]{ EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST };
			break;
		case EAST:
		case NORTH:
		case SOUTH:
		case WEST:
			priority = new EnumFacing[]{ hitSide, hitSide.rotateYCCW(), hitSide.rotateY(), hitSide.getOpposite(), EnumFacing.UP };
			break;
		}
		
		for (EnumFacing checkSide : priority)
		{
			out = out.withProperty(FACING, checkSide);
			
			if (canTorchStay(world, pos, out, true))
			{
				return out;
			}
		}
		
		return getDefaultState();
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		switch (state.getValue(PART))
		{
		case BOTTOM:
			world.setBlockState(pos.up(), state.withProperty(PART, Part.TOP));
			break;
		case TOP:
			world.setBlockState(pos.down(), state.withProperty(PART, Part.BOTTOM));
			break;
		}
	}
	
	protected BlockPos getOther(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() == this)
		{
			Part part = state.getValue(PART);
			BlockPos other = part == Part.BOTTOM ? pos.up() : pos.down();
			IBlockState otherState = world.getBlockState(other);
			
			if (otherState.getBlock() == this && otherState.getValue(PART) != part)
			{
				return other;
			}
		}
		
		return null;
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (!canTorchStay(world, pos, state, false))
		{
			world.destroyBlock(pos, true);
			
			BlockPos other = getOther(world, pos);
			
			if (other != null)
			{
				world.destroyBlock(other, true);
			}
		}
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		BlockPos other = getOther(world, pos);
		IBlockState otherState = other != null ? world.getBlockState(other) : null;
		
		if (other != null)
		{
			world.playAuxSFX(2001, other, Block.getStateId(otherState));
			
			if (willHarvest && !player.capabilities.isCreativeMode)
			{
				dropBlockAsItem(world, other, otherState,
						EnchantmentHelper.getEnchantmentLevel(Enchantments.fortune, player.getHeldItemMainhand()));
			}
			
			world.setBlockState(other, Blocks.air.getDefaultState(), 2);
		}
		
		boolean out = super.removedByPlayer(state, world, pos, player, willHarvest);
		
		if (other != null)
		{
			world.markAndNotifyBlock(other, world.getChunkFromBlockCoords(other), otherState, world.getBlockState(other), 1);
		}
		
		return out;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		double radius = 0.1;
		double height = 1.6;
		AxisAlignedBB bb = AABBUtils.create(0.5, height, 0.5).expand(radius, 0, radius);
		
		double topDown = 0.0625;
		double toOuter = 0.5F - radius;
		
		EnumFacing facing = state.getValue(FACING);
		
		switch (facing) {
		case EAST:
		case WEST:
		case SOUTH:
		case NORTH:
			bb = bb.expand(0, -0.1875, 0);
			bb = AABBUtils.extend(bb, facing.getOpposite(), toOuter);
			bb = bb.addCoord(0, topDown, 0);
		default:
			break;
		}
		
		if (state.getValue(PART) == Part.TOP)
		{
			bb = bb.offset(0, -1, 0);
		}
		
		return bb;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos,
			AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if (state.getValue(PART) == Part.TOP)
		{
			EnumFacing facing = state.getValue(FACING);
			double xPos = pos.getX() + 0.5;
			double yPos = pos.getY() + 0.7;
			double zPos = pos.getZ() + 0.5;
			
			double off = 0.015625;
			
			xPos += off * facing.getFrontOffsetX();
			yPos -= facing.getAxis().isHorizontal() ? 0.125 : 0;
			zPos += off * facing.getFrontOffsetZ();
			
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xPos, yPos, zPos, 0, 0, 0);
			world.spawnParticle(EnumParticleTypes.FLAME, xPos, yPos, zPos, 0, 0, 0);
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
}
