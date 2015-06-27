package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
		this.setCreativeTab(GenesisCreativeTabs.BLOCK);
	}

	@Override
	public BlockKomatiiticLava setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(Constants.PREFIX + unlocalizedName);

		return this;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		this.checkForMixing(world, pos, state);
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(world, pos, state, neighborBlock);
		this.checkForMixing(world, pos, state);
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		if (this.maxScaledLight == 0)
		{
			return this.getLightValue();
		}
		else if (block != this)
		{
			return block.getLightValue(world, pos);
		}
		int data = this.quantaPerBlock - ((Integer) world.getBlockState(pos).getValue(LEVEL)).intValue() - 1;
		return (int) (data / this.quantaPerBlockFloat * this.maxScaledLight);
	}

	public boolean checkForMixing(World worldIn, BlockPos pos, IBlockState state)
	{
		if (this.blockMaterial == Material.lava)
		{
			boolean flag = false;
			EnumFacing[] aenumfacing = EnumFacing.values();
			int i = aenumfacing.length;

			for (int j = 0; j < i; ++j)
			{
				EnumFacing enumfacing = aenumfacing[j];

				if (enumfacing != EnumFacing.DOWN && worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getMaterial() == Material.water)
				{
					flag = true;
					break;
				}
			}

			if (flag)
			{
				Integer integer = (Integer) state.getValue(LEVEL);

				if (integer.intValue() <= 4)
				{
					worldIn.setBlockState(pos, GenesisBlocks.komatiite.getDefaultState());
					this.triggerMixEffects(worldIn, pos);
					return true;
				}
			}
		}

		return false;
	}

	protected void triggerMixEffects(World worldIn, BlockPos pos)
	{
		double d0 = pos.getX();
		double d1 = pos.getY();
		double d2 = pos.getZ();
		worldIn.playSoundEffect(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

		for (int i = 0; i < 8; ++i)
		{
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

}