package genesis.block;

import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.util.BlockStateToMetadata;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockGenesisMushroom extends BlockBush implements IGrowable
{
	public static final PropertyEnum FACING = PropertyEnum.create("facing", BlockGenesisMushroom.MushroomEnumFacing.class);
	private MushroomGrowType growType;
	
	public enum MushroomGrowType {
		Grow_top,
		Grow_side
	}
	
	public BlockGenesisMushroom()
	{
		this.blockState = this.createBlockState();
		this.setDefaultState(this.blockState.getBaseState());
		this.setTickRandomly(true);
		this.setStepSound(soundTypeGrass);
	}
	
	public BlockGenesisMushroom setGrowType(MushroomGrowType type)
	{
		this.growType = type;
		//BlockLog
		switch(type)
		{
		case Grow_side:
			this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.6F, 0.8F, 0.9F);
			break;
		default:
			this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 0.8F, 0.9F);
			break;
		}
		
		return this;
	}
	
	public MushroomGrowType getGrowType()
	{
		return this.growType;
	}
	
	public BlockGenesisMushroom setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(name);
		return this;
	}
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (rand.nextInt(25) == 0)
		{
			int i = 5;
			
			@SuppressWarnings("rawtypes")
			Iterator iterator = BlockPos.getAllInBox(pos.add(-4, -1, -4), pos.add(4, 1, 4)).iterator();
			
			while (iterator.hasNext())
			{
				BlockPos blockpos1 = (BlockPos) iterator.next();
				
				if (worldIn.getBlockState(blockpos1).getBlock() == this)
				{
					--i;
					if (i <= 0)
						return;
				}
			}
			
			BlockPos blockpos2 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
			
			for (int j = 0; j < 4; ++j)
			{
				if (worldIn.isAirBlock(blockpos2) && this.canBlockStay(worldIn, blockpos2, this.getDefaultState()))
				{
					pos = blockpos2;
				}
				
				blockpos2 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
			}
			
			if (worldIn.isAirBlock(blockpos2) && this.canBlockStay(worldIn, blockpos2, this.getDefaultState()))
			{
				worldIn.setBlockState(blockpos2, this.getDefaultState(), 2);
			}
		}
	}
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		if (this.growType == MushroomGrowType.Grow_top)
			return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos, this.getDefaultState());
		else
			return this.canBlockStay(worldIn, pos, this.getDefaultState());
	}
	
	protected boolean canPlaceBlockOn(Block ground)
	{
		if (this.growType == MushroomGrowType.Grow_top)
			return ground.isFullBlock();
		else
			return true;
	}
	
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (pos.getY() >= 0 && pos.getY() < 256)
		{
			IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
			
			switch (this.growType)
			{
			case Grow_side:
				boolean placeNorth = checkBlockIsBase(worldIn.getBlockState(pos.north()));
				boolean placeSouth = checkBlockIsBase(worldIn.getBlockState(pos.south()));
				boolean placeEast = checkBlockIsBase(worldIn.getBlockState(pos.east()));
				boolean placeWest = checkBlockIsBase(worldIn.getBlockState(pos.west()));
				
				return placeNorth || placeSouth || placeEast || placeWest;
			default:
				return iblockstate1.getBlock() == Blocks.mycelium ? true
						: ((iblockstate1.getBlock() == GenesisBlocks.moss)? true 
								: (iblockstate1.getBlock() == Blocks.dirt && iblockstate1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) ? true
								: worldIn.getLight(pos) < 13
										&& iblockstate1.getBlock().canSustainPlant(
												worldIn, pos.down(),
												net.minecraft.util.EnumFacing.UP,
												this))? true
														: iblockstate1.getBlock() instanceof IGenesisMushroomBase;
			}
		}
		else
		{
			return false;
		}
	}
	
	private boolean checkBlockIsBase(IBlockState state)
	{
		return (state.getBlock() == Blocks.log) ? true
				: (state.getBlock() == Blocks.log2) ? true
						: state.getBlock() instanceof IGenesisMushroomBase;
	}
	
	public boolean generateBigMushroom(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		return false;
	}
	
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return false;
	}
	
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		
		if (this.growType == MushroomGrowType.Grow_side)
		{
			int l = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			
			switch(l)
			{
			case 1:
				state = state.withProperty(FACING, MushroomEnumFacing.WEST);
				this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.6F, 0.8F, 0.9F);
				break;
			case 2:
				state = state.withProperty(FACING, MushroomEnumFacing.NORTH);
				this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 0.8F, 0.6F);
				break;
			case 3:
				state = state.withProperty(FACING, MushroomEnumFacing.EAST);
				this.setBlockBounds(0.4F, 0.0F, 0.1F, 0.9F, 0.8F, 0.9F);
				break;
			default:
				state = state.withProperty(FACING, MushroomEnumFacing.SOUTH);
				this.setBlockBounds(0.1F, 0.0F, 0.4F, 0.9F, 0.8F, 0.9F);
				break;
			}
		}
		
		return state;
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[]{FACING});
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, FACING);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, FACING);
	}
	
	public static enum MushroomEnumFacing implements IStringSerializable
	{
		NORTH("north"),
        SOUTH("south"),
        EAST("east"),
        WEST("west");
        private final String name;
        
        private MushroomEnumFacing(String name)
        {
            this.name = name;
        }
        
        public String toString()
        {
            return this.name;
        }
        
        public String getName()
        {
            return this.name;
        }
	}
}
