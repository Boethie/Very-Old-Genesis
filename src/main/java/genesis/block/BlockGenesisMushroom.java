package genesis.block;

import genesis.common.GenesisBlocks;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockGenesisMushroom extends BlockBush implements IGrowable
{
	public BlockGenesisMushroom()
	{
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		this.setTickRandomly(true);
		this.setStepSound(soundTypeGrass);
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
		return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos, this.getDefaultState());
	}
	
	protected boolean canPlaceBlockOn(Block ground)
	{
		return ground.isFullBlock();
	}
	
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (pos.getY() >= 0 && pos.getY() < 256)
		{
			IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
			
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
		else
		{
			return false;
		}
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
}
