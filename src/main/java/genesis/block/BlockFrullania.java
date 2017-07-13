package genesis.block;

import java.util.Random;
import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFrullania extends BlockVine
{
	public enum EnumPosition implements IStringSerializable
	{
		BOTTOM("bottom"),
		MIDDLE("middle"),
		TOP("top");
		
		private final String name;
		
		EnumPosition(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return name;
		}
	}
	
	public static final PropertyEnum<EnumPosition> POSITION = PropertyEnum.create("position", EnumPosition.class);
	
	public BlockFrullania()
	{
		blockState = new BlockStateContainer(this, UP, NORTH, EAST, SOUTH, WEST, POSITION);
		setDefaultState(blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(POSITION, EnumPosition.BOTTOM));
		setHardness(0.2F);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setSoundType(SoundType.PLANT);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		
		if (!state.getValue(UP))
		{
			if (world.getBlockState(pos.down()).getBlock() != this)
				return state.withProperty(POSITION, EnumPosition.BOTTOM);
			
			if (world.getBlockState(pos.up()).getBlock() != this)
				return state.withProperty(POSITION, EnumPosition.TOP);
		}
		
		return state.withProperty(POSITION, EnumPosition.MIDDLE);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		state = state.getActualState(source, pos);
		if (state.getValue(NORTH).booleanValue())
		{
			return NORTH_AABB;
		}
		else if (state.getValue(EAST).booleanValue())
		{
			return EAST_AABB;
		}
		else if (state.getValue(SOUTH).booleanValue())
		{
			return SOUTH_AABB;
		}
		else if (state.getValue(WEST).booleanValue())
		{
			return WEST_AABB;
		}
		else return NULL_AABB;
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote && world.rand.nextInt(7) == 0)
		{
			// search in the vicinity for other blocks of the same type 
			int tries = 4;
			boolean found = false;
			searching:
			for (int x = -4; x <= 4; ++x)
			{
				for (int z = -4; z <= 4; ++z)
				{
					for (int y = -2; y <= 2; ++y)
					{
						if (world.getBlockState(pos.add(x, y, z)).getBlock() == this)
						{
							--tries;
							if (tries <= 0)
							{
								found = true;
								break searching;
							}
						}
					}
				}
			}

			// if we've found enough other blocks in the vicinity, don't grow any more
			if (found) return;
			
			EnumFacing enumfacing1 = EnumFacing.random(rand);
			BlockPos posUp = pos.up();

			// if we've randomly decided to try to grow up, and we're below the build height, and the block directly above is air...
			if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && world.isAirBlock(posUp))
			{
				IBlockState iblockstate2 = state;

				for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
				{
					if (rand.nextBoolean() || !this.canAttachVineOn(world.getBlockState(posUp.offset(enumfacing2))))
					{
						iblockstate2 = iblockstate2.withProperty(getPropertyFor(enumfacing2), Boolean.valueOf(false));
					}
				}

				if (iblockstate2.getValue(NORTH).booleanValue() || iblockstate2.getValue(EAST).booleanValue() || iblockstate2.getValue(SOUTH).booleanValue() || iblockstate2.getValue(WEST).booleanValue())
				{
					world.setBlockState(posUp, iblockstate2, 2);
				}
			}
			
			// if we've randomly decided to try and grow horizontally...
			else if (enumfacing1.getAxis().isHorizontal() && !state.getValue(getPropertyFor(enumfacing1)).booleanValue())
			{
				BlockPos posHorizontal = pos.offset(enumfacing1);
				IBlockState stateHorizontal = world.getBlockState(posHorizontal);

				// if the adjacent block in our chosen direction is air...
				if (stateHorizontal.getMaterial() == Material.AIR)
				{
					EnumFacing enumfacing3 = enumfacing1.rotateY();
					EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
					boolean flag1 = state.getValue(getPropertyFor(enumfacing3)).booleanValue();
					boolean flag2 = state.getValue(getPropertyFor(enumfacing4)).booleanValue();
					BlockPos blockpos = posHorizontal.offset(enumfacing3);
					BlockPos blockpos1 = posHorizontal.offset(enumfacing4);

					if (flag1 && this.canAttachVineOn(world.getBlockState(blockpos)))
					{
						world.setBlockState(posHorizontal, this.getDefaultState().withProperty(getPropertyFor(enumfacing3), Boolean.valueOf(true)), 2);
					}
					else if (flag2 && this.canAttachVineOn(world.getBlockState(blockpos1)))
					{
						world.setBlockState(posHorizontal, this.getDefaultState().withProperty(getPropertyFor(enumfacing4), Boolean.valueOf(true)), 2);
					}
					else if (flag1 && world.isAirBlock(blockpos) && this.canAttachVineOn(world.getBlockState(pos.offset(enumfacing3))))
					{
						world.setBlockState(blockpos, this.getDefaultState().withProperty(getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
					}
					else if (flag2 && world.isAirBlock(blockpos1) && this.canAttachVineOn(world.getBlockState(pos.offset(enumfacing4))))
					{
						world.setBlockState(blockpos1, this.getDefaultState().withProperty(getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
					}
					else if (this.canAttachVineOn(world.getBlockState(posHorizontal.up())))
					{
						world.setBlockState(posHorizontal, this.getDefaultState(), 2);
					}
				}
				else if (stateHorizontal.getMaterial().isOpaque() && stateHorizontal.isFullCube())
				{
					world.setBlockState(pos, state.withProperty(getPropertyFor(enumfacing1), Boolean.valueOf(true)), 2);
				}
			}
			
			// if we've randomly decided to try and grow downwards, and we're above the minimum build height...
			else if (pos.getY() > 1)
			{
				BlockPos posDown = pos.down();
				IBlockState stateDown = world.getBlockState(posDown);
				Block blockDown = stateDown.getBlock();

				// if there's already a block of the same type below this one...
				if (blockDown == this)
				{
					IBlockState iblockstate4 = stateDown;

					for (EnumFacing enumfacing5 : EnumFacing.Plane.HORIZONTAL)
					{
						PropertyBool propertybool = getPropertyFor(enumfacing5);

						if (rand.nextBoolean() && ((Boolean)state.getValue(propertybool)).booleanValue())
						{
							iblockstate4 = iblockstate4.withProperty(propertybool, Boolean.valueOf(true));
						}
					}

					if (iblockstate4.getValue(NORTH).booleanValue() || iblockstate4.getValue(EAST).booleanValue() || iblockstate4.getValue(SOUTH).booleanValue() || iblockstate4.getValue(WEST).booleanValue())
					{
						world.setBlockState(posDown, iblockstate4, 2);
					}
				}
			}
		}
	}
	
	private boolean canAttachVineOn(IBlockState state)
	{
		return state.isFullCube() && state.getMaterial().blocksMovement();
	}
	
	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	{ 
		return false; 
	}
}
