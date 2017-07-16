package genesis.block;

import java.util.Random;
import genesis.common.GenesisBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockKomatiiticLava extends BlockFluidClassic
{
	public BlockKomatiiticLava(Fluid fluid)
	{
		super(fluid, Material.LAVA);
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
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		checkForMixing(world, pos, state);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if (rand.nextInt(500) == 0)
		{
			world.playSound(pos.getX(), pos.getY(), pos.getZ(),
					SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS,
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
				if (side != EnumFacing.DOWN && world.getBlockState(pos.offset(side)).getMaterial() == Material.WATER)
				{
					mix = true;
					break;
				}
			}

			if (mix)
			{
				Integer level = state.getValue(LEVEL);

				if (level <= 4)
				{
					world.setBlockState(pos, GenesisBlocks.KOMATIITE.getDefaultState());
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
				SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS,
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
							world.setBlockState(randPos, Blocks.FIRE.getDefaultState());
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
						world.setBlockState(randPos.up(), Blocks.FIRE.getDefaultState());
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
	
	/**
	 * Fixes Genesis Bug #90.
	 * 
	 * Fluid models rendering with gaps was a known issue that was fixed in Forge 1.11.2-13.20.0.2279.
	 * This code is taken from Forge pull request #3747, which resolves Forge bug #2993.
	 * 
	 * See:
	 *  * http://www.minecraftforge.net/forum/topic/57885-1102-issues-with-custom-fluids/
	 *  * https://github.com/MinecraftForge/MinecraftForge/issues/2993
	 *  * https://github.com/MinecraftForge/MinecraftForge/pull/3747
	 *  
	 *  This will not be needed once Genesis moves to Minecraft 1.11.2.
	 *  
	 */
	@Override
	public IBlockState getExtendedState(IBlockState oldState, IBlockAccess worldIn, BlockPos pos)
	{
		IExtendedBlockState state = (IExtendedBlockState)oldState;
		state = state.withProperty(FLOW_DIRECTION, (float)getFlowDirection(worldIn, pos));
		float[][] height = new float[3][3];
		float[][] corner = new float[2][2];
		height[1][1] = getFluidHeightForRender(worldIn, pos);
		if(height[1][1] == 1)
		{
			for(int i = 0; i < 2; i++)
			{
				for(int j = 0; j < 2; j++)
				{
					corner[i][j] = 1;
				}
			}
		}
		else
		{
			for(int i = 0; i < 3; i++)
			{
				for(int j = 0; j < 3; j++)
				{
					if(i != 1 || j != 1)
					{
						height[i][j] = getFluidHeightForRender(worldIn, pos.add(i - 1, 0, j - 1));
					}
				}
			}
			for(int i = 0; i < 2; i++)
			{
				for(int j = 0; j < 2; j++)
				{
					corner[i][j] = getFluidHeightAverage(height[i][j], height[i][j + 1], height[i + 1][j], height[i + 1][j + 1]);
				}
			}
		}
		
		if(isCoveredWithFluid(worldIn, pos.add(-1, 0, -1)) || isCoveredWithFluid(worldIn, pos.add(-1, 0,  0)) || isCoveredWithFluid(worldIn, pos.add(0, 0, -1)))
			corner[0][0] = 1;
		if(isCoveredWithFluid(worldIn, pos.add(-1, 0,  0)) || isCoveredWithFluid(worldIn, pos.add(-1, 0,  1)) || isCoveredWithFluid(worldIn, pos.add(0, 0,  1)))
			corner[0][1] = 1;
		if(isCoveredWithFluid(worldIn, pos.add( 0, 0, -1)) || isCoveredWithFluid(worldIn, pos.add( 1, 0, -1)) || isCoveredWithFluid(worldIn, pos.add(1, 0,  0)))
			corner[1][0] = 1;
		if(isCoveredWithFluid(worldIn, pos.add( 1, 0,  0)) || isCoveredWithFluid(worldIn, pos.add( 0, 0,  1)) || isCoveredWithFluid(worldIn, pos.add(1, 0,  1)))
			corner[1][1] = 1;

		state = state.withProperty(LEVEL_CORNERS[0], corner[0][0]);
		state = state.withProperty(LEVEL_CORNERS[1], corner[0][1]);
		state = state.withProperty(LEVEL_CORNERS[2], corner[1][1]);
		state = state.withProperty(LEVEL_CORNERS[3], corner[1][0]);
		return state;
	}
	
	/**
	 * Helper method for {@link getExtendedState}, part of bug fix for Genesis bug #90.
	 */
	private boolean isCoveredWithFluid(IBlockAccess world, BlockPos pos)
	{
		IBlockState here = world.getBlockState(pos);
		IBlockState up = world.getBlockState(pos.down(densityDir));
		if (here.getBlock() == this && (up.getMaterial().isLiquid() || up.getBlock() instanceof IFluidBlock))
		{
				return true;
		}
		return false;
	}
	
}
