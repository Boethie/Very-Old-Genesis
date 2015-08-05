package genesis.block;

import static genesis.block.BlockGenesisMushroom.MushroomGrowType.*;
import genesis.common.GenesisBlocks;
import genesis.util.BlockStateToMetadata;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockGenesisMushroom extends BlockBush
{
	public enum MushroomGrowType
	{
		GROW_TOP,
		GROW_SIDE
	}
	
	public static final PropertyEnum FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.HORIZONTALS);
	protected MushroomGrowType growType;
	protected float boundsRadius;
	protected float boundsHeight;
	protected float boundsBottom;
	
	public BlockGenesisMushroom()
	{
		super();
		
		setStepSound(soundTypeGrass);
	}
	
	public BlockGenesisMushroom setGrowType(MushroomGrowType type)
	{
		growType = type;
		
		switch (growType)
		{
		case GROW_TOP:
			blockState = new BlockState(this);
			break;
		case GROW_SIDE:
			blockState = new BlockState(this, FACING);
			break;
		}
		
		setDefaultState(blockState.getBaseState());
		
		return this;
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
	
	public float getRadius()
	{
		return boundsRadius;
	}
	
	public float getHeight()
	{
		return boundsHeight;
	}
	
	public float getBottom()
	{
		return boundsBottom;
	}

	@Override
	public BlockGenesisMushroom setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(name);
		return this;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		float radius = getRadius();
		float height = getHeight();
		float bottom = getBottom();
		AxisAlignedBB bb = new AxisAlignedBB(0.5 - radius, bottom, 0.5 - radius, 0.5 + radius, bottom + height, 0.5 + radius);
		
		if (getGrowType() == GROW_SIDE)
		{
			IBlockState state = world.getBlockState(pos);
			EnumFacing facing = (EnumFacing) state.getValue(FACING);
			
			double offsetAmount = 0.5 - radius;
			bb = bb.offset(facing.getFrontOffsetX() * offsetAmount,
						facing.getFrontOffsetY() * offsetAmount,
						facing.getFrontOffsetZ() * offsetAmount);
		}
		
		minX = bb.minX;
		minY = bb.minY;
		minZ = bb.minZ;
		maxX = bb.maxX;
		maxY = bb.maxY;
		maxZ = bb.maxZ;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (rand.nextInt(25) == 0)
		{
			int shroomsLeft = 5;
			
			Iterable<BlockPos> box = (Iterable<BlockPos>) BlockPos.getAllInBox(pos.add(-4, -1, -4), pos.add(4, 1, 4));
			
			for (BlockPos checkPos : box)
			{
				if (worldIn.getBlockState(checkPos).getBlock() == this)
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
					
					if (worldIn.isAirBlock(randPos) && canBlockStay(worldIn, randPos, getDefaultState()))
					{
						placePos = randPos;
					}
				}
				
				if (placePos != null)
				{
					worldIn.setBlockState(placePos, getDefaultState(), 2);
				}
			}
		}
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		switch (growType)
		{
		case GROW_TOP:
			return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos, getDefaultState());
		case GROW_SIDE:
			EnumFacing facingSide = side.getOpposite();
			
			if (FACING.getAllowedValues().contains(facingSide))
				return canBlockStay(worldIn, pos, getDefaultState().withProperty(FACING, facingSide));
			
			break;
		}
		
		return false;
	}

	@Override
	protected boolean canPlaceBlockOn(Block ground)
	{
		if (this.growType == GROW_TOP)
			return ground.isFullBlock();
		else
			return true;
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		if (pos.getY() >= 0 && pos.getY() < 256)
		{
			switch (this.growType)
			{
			case GROW_SIDE:
				EnumFacing facing = (EnumFacing) state.getValue(FACING);
				BlockPos offPos = pos.offset(facing);
				return checkBlockIsBase(world, offPos, world.getBlockState(offPos));
			case GROW_TOP:
				IBlockState below = world.getBlockState(pos.down());
				Block blockBelow = below.getBlock();
				
				if (blockBelow == GenesisBlocks.moss)
				{
					return true;
				}
				else if (blockBelow == Blocks.mycelium)
				{
					return true;
				}
				else if (blockBelow == Blocks.dirt && below.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL)
				{
					return true;
				}
				else if (blockBelow instanceof BlockLog)
				{
					return true;
				}
				else if (world.getLightFromNeighbors(pos) < 13 &&
						blockBelow.canSustainPlant(world, pos.down(), EnumFacing.UP, this))
				{
					return true;
				}
				else if (blockBelow instanceof IGenesisMushroomBase)
				{
					return ((IGenesisMushroomBase) blockBelow).canSustainMushroom(world, pos, state);
				}
			}
		}
		
		return false;
	}
	
	private boolean checkBlockIsBase(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		Block block = state.getBlock();
		
		if (block.getMaterial() == Material.wood)
		{
			return true;
		}
		
		if (block instanceof IGenesisMushroomBase && ((IGenesisMushroomBase) block).canSustainMushroom(world, pos, state))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
		
		if (growType == GROW_SIDE)
		{
			state = state.withProperty(FACING, facing.getOpposite());
		}
		
		return state;
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
