package genesis.block;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockKomatiiticLava extends BlockFluidClassic
{
	public BlockKomatiiticLava(Fluid fluid)
	{
		super(fluid, Material.lava);
		setQuantaPerBlock(4);
		lightValue = maxScaledLight;
		setTickRandomly(true);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		checkForMixing(world, pos, state);
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(world, pos, state, neighborBlock);
		checkForMixing(world, pos, state);
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if (rand.nextInt(500) == 0)
		{
			world.playSound(pos.getX(), pos.getY(), pos.getZ(),
					SoundEvents.block_lava_pop, SoundCategory.BLOCKS,
					1, 1.2F / (rand.nextFloat() * 0.2F + 0.9F), false);
			world.spawnParticle(EnumParticleTypes.LAVA, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
		}
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		/*Block block = world.getBlockState(pos).getBlock();
		
		if (block != this)
		{
			return block.getLightValue(state, world, pos);
		}
		
		float levelF = world.getBlockState(pos).getValue(LEVEL) / quantaPerBlockFloat;
		return Math.round((1 - levelF) * maxScaledLight);*/
		return maxScaledLight;
	}
	
	public boolean checkForMixing(World world, BlockPos pos, IBlockState state)
	{
		boolean mix = false;
		
		try
		{
			for (EnumFacing side : EnumFacing.VALUES)
			{
				if (side != EnumFacing.DOWN && world.getBlockState(pos.offset(side)).getMaterial() == Material.water)
				{
					mix = true;
					break;
				}
			}
			
			if (mix)
			{
				Integer level = state.getValue(LEVEL);
				
				if (level.intValue() <= 4)
				{
					world.setBlockState(pos, GenesisBlocks.komatiite.getDefaultState());
					triggerMixEffects(world, pos);
					return true;
				}
			}
		}
		catch (Exception e)
		{
			// Do nothing
		}
		
		return false;
	}
	
	protected void triggerMixEffects(World world, BlockPos pos)
	{
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		world.playSound(null, x + 0.5D, y + 0.5D, z + 0.5D,
				SoundEvents.block_lava_extinguish, SoundCategory.BLOCKS,
				0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		
		for (int i = 0; i < 8; i++)
		{
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + Math.random(), y + 1.2D, z + Math.random(), 0, 0, 0);
		}
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		
		if (world.getGameRules().getBoolean("doFireTick"))
		{
			int i = rand.nextInt(3);
			
			if (i > 0)
			{
				BlockPos randPos = pos;
				
				for (int j = 0; j < i; ++j)
				{
					randPos = randPos.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
					
					if (world.isAirBlock(randPos))
					{
						if (isSurroundingBlockFlammable(world, randPos))
						{
							world.setBlockState(randPos, Blocks.fire.getDefaultState());
							return;
						}
					}
					else if (world.getBlockState(randPos).getMaterial().blocksMovement())
					{
						return;
					}
				}
			}
			else
			{
				for (int k = 0; k < 3; ++k)
				{
					BlockPos randPos = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
					
					if (world.isAirBlock(randPos.up()) && getCanBlockBurn(world, randPos))
					{
						world.setBlockState(randPos.up(), Blocks.fire.getDefaultState());
					}
				}
			}
		}
	}
	
	protected boolean isSurroundingBlockFlammable(World world, BlockPos pos)
	{
		for (EnumFacing side : EnumFacing.VALUES)
		{
			if (getCanBlockBurn(world, pos.offset(side)))
			{
				return true;
			}
		}
		
		return false;
	}
	
	protected static boolean getCanBlockBurn(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getMaterial().getCanBurn();
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		return null;
	}
}
