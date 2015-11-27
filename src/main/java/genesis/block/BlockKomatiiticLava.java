package genesis.block;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
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
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (rand.nextInt(500) == 0)
		{
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), "liquid.lavapop", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F), true);
			world.spawnParticle(EnumParticleTypes.LAVA, pos.getX(), pos.getY(), pos.getZ(), 0.0D, 1.2D, 0.0D, 1);
		}
	}
	
	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		
		if (block != this)
		{
			return block.getLightValue(world, pos);
		}
		
		float levelF = world.getBlockState(pos).getValue(LEVEL) / quantaPerBlockFloat;
		return Math.round((1 - levelF) * maxScaledLight);
	}
	
	public boolean checkForMixing(World world, BlockPos pos, IBlockState state)
	{
		boolean mix = false;
		EnumFacing[] sides = EnumFacing.values();
		
		try
		{
			for (EnumFacing side : sides)
			{
				if (side != EnumFacing.DOWN && world.getBlockState(pos.offset(side)).getBlock().getMaterial() == Material.water)
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
			;
		}
		
		return false;
	}
	
	protected void triggerMixEffects(World world, BlockPos pos)
	{
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

		for (int i = 0; i < 8; ++i)
		{
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + Math.random(), y + 1.2D, z + Math.random(), 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		
		if (world.getGameRules().getGameRuleBooleanValue("doFireTick"))
		{
			int i = rand.nextInt(3);
			
			if (i > 0)
			{
				BlockPos randPos = pos;
				
				for (int j = 0; j < i; ++j)
				{
					randPos = randPos.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
					Block randBlock = world.getBlockState(randPos).getBlock();
					
					if (randBlock.getMaterial() == Material.air)
					{
						if (isSurroundingBlockFlammable(world, randPos))
						{
							world.setBlockState(randPos, Blocks.fire.getDefaultState());
							return;
						}
					}
					else if (randBlock.getMaterial().blocksMovement())
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
		for (EnumFacing side : EnumFacing.values())
		{
			if (getCanBlockBurn(world, pos.offset(side)))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean getCanBlockBurn(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock().getMaterial().getCanBurn();
	}
}
