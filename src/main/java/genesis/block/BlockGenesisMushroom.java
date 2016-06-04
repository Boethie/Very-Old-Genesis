package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.util.AABBUtils;
import genesis.util.BlockStateToMetadata;
import genesis.util.WorldUtils;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockGenesisMushroom extends BlockBush
{
	public enum MushroomGrowType
	{
		GROW_TOP,
		GROW_SIDE;
		
		public boolean isTop()
		{
			return this == GROW_TOP;
		}
		
		public boolean isSide()
		{
			return this == GROW_SIDE;
		}
	}
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private final MushroomGrowType growType;
	private float boundsRadius;
	private float boundsHeight;
	private float boundsBottom;
	
	public BlockGenesisMushroom(MushroomGrowType type)
	{
		super();
		
		growType = type;
		
		switch (growType)
		{
		case GROW_TOP:
			blockState = new BlockStateContainer(this);
			break;
		case GROW_SIDE:
			blockState = new BlockStateContainer(this, FACING);
			break;
		}
		
		setDefaultState(blockState.getBaseState());
		
		setSoundType(GenesisSoundTypes.MUSHROOM);
	}
	
	public MushroomGrowType getGrowType()
	{
		return growType;
	}
	
	public BlockGenesisMushroom setBoundsSize(float radius, float height, float bottom)
	{
		boundsRadius = radius;
		boundsHeight = height;
		boundsBottom = bottom;
		return this;
	}
	
	public float getBoundsRadius()
	{
		return boundsRadius;
	}
	
	public float getBoundsHeight()
	{
		return boundsHeight;
	}
	
	public float getBoundsBottom()
	{
		return boundsBottom;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		float radius = getBoundsRadius();
		float height = getBoundsHeight();
		float bottom = getBoundsBottom();
		AxisAlignedBB bb = AABBUtils.createExpansion(0.5, bottom + height, 0.5, EnumFacing.Plane.HORIZONTAL, radius);
		
		if (getGrowType().isSide())
		{
			EnumFacing facing = state.getValue(FACING);
			double offsetAmount = 0.5 - radius;
			bb = AABBUtils.offset(bb, facing, offsetAmount);
		}
		
		return bb;
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (rand.nextInt(25) == 0)
		{
			int shroomsLeft = 5; // Max number of shrooms
			
			Iterable<BlockPos> box = WorldUtils.getArea(pos.add(-4, -1, -4), pos.add(4, 1, 4)); // 8x2x8 area
			
			for (BlockPos checkPos : box)
			{
				if (world.getBlockState(checkPos).getBlock() == this)
				{
					--shroomsLeft;
					
					if (shroomsLeft <= 0)
						break;
				}
			}
			
			if (shroomsLeft > 0)
			{
				BlockPos placePos = null;
				
				for (int i = 0; i < 4; i++)
				{
					BlockPos randPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
					
					if (world.isAirBlock(randPos) && canBlockStay(world, randPos, getDefaultState()))
					{
						placePos = randPos;
					}
				}
				
				if (placePos != null)
				{
					world.setBlockState(placePos, getDefaultState(), 2);
				}
			}
		}
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
	{
		switch (growType)
		{
		case GROW_TOP:
			return super.canPlaceBlockAt(world, pos) && canBlockStay(world, pos, getDefaultState());
		case GROW_SIDE:
			EnumFacing facingSide = side.getOpposite();
			return facingSide.getAxis().isHorizontal() && canBlockStay(world, pos, getDefaultState().withProperty(FACING, facingSide));
		}
		
		return false;
	}
	
	@Override
	protected boolean canSustainBush(IBlockState ground)
	{
		return !growType.isTop() || ground.isFullBlock();
	}
	
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		if (pos.getY() >= 0 && pos.getY() < 256)
		{
			switch (growType)
			{
			case GROW_SIDE:
				EnumFacing side = state.getValue(FACING);
				BlockPos sidePos = pos.offset(side);
				return canGrowOnSide(world, pos, state, side, sidePos, world.getBlockState(sidePos));
			case GROW_TOP:
				BlockPos downPos = pos.down();
				return canGrowOnTop(world, pos, state, downPos, world.getBlockState(downPos));
			}
		}
		
		return false;
	}
	
	protected boolean canGrowOnSide(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side, BlockPos sidePos, IBlockState sideState)
	{
		Block sideBlock = sideState.getBlock();
		
		if (sideBlock instanceof IMushroomBase)
		{
			IMushroomBase base = (IMushroomBase) sideBlock;
			return base.canSustainMushroom(world, sidePos, side.getOpposite(), state);
		}
		else
		{
			return sideState.getMaterial() == Material.wood;
		}
	}
	
	protected boolean canGrowOnTop(World world, BlockPos pos, IBlockState state, BlockPos bottomPos, IBlockState bottomState)
	{
		Block bottomBlock = bottomState.getBlock();
		
		if (bottomBlock instanceof IMushroomBase)
		{
			IMushroomBase base = (IMushroomBase) bottomBlock;
			return base.canSustainMushroom(world, pos, EnumFacing.UP, state);
		}
		else if (bottomBlock == Blocks.dirt && bottomState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL)
		{
			return true;
		}
		else if (world.getLightFromNeighbors(pos) < 13 && bottomBlock.canSustainPlant(bottomState, world, bottomPos, EnumFacing.UP, this))
		{
			return true;
		}
		else
		{
			return bottomBlock == GenesisBlocks.moss || bottomBlock == Blocks.mycelium || bottomBlock instanceof BlockLog;
		}
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
		
		if (growType.isSide())
		{
			return state.withProperty(FACING, facing.getOpposite());
		}
		else
		{
			return state;
		}
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}
}
