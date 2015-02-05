package genesis.block;

import java.util.List;
import java.util.Random;

import genesis.common.GenesisCreativeTabs;
import genesis.metadata.EnumAquaticPlant;
import genesis.util.Constants;
import genesis.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAquaticPlant extends BlockMetadata
{

	public BlockAquaticPlant()
	{
		super(Material.water);
		setHardness(0.0F).setTickRandomly(true).setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}

	@Override
	public BlockAquaticPlant setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(name);

		return this;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (((EnumAquaticPlant) state.getValue(Constants.AQUATIC_PLANT_VARIANT)) == EnumAquaticPlant.CHANCELLORIA)
		{
			entityIn.attackEntityFrom(Constants.CHANCELLORIA_DMG, 0.5F);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		checkAndDropBlock(worldIn, pos, state);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(worldIn, pos, state))
		{
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.water.getStateFromMeta(0), 3);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		worldIn.setBlockState(pos, Blocks.water.getStateFromMeta(0));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canBlockStay(worldIn, pos, getDefaultState());
	}

	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		final IBlockState below = world.getBlockState(pos.down());
		if (!((below.getBlock() == Blocks.sand) || (below.getBlock() == Blocks.dirt) || (below.getBlock() == Blocks.gravel)))
		{
			return false;
		}
		final IBlockState above = world.getBlockState(pos.up());
		if (above.getBlock() != Blocks.water)
		{
			return false;
		}
		final List<IBlockState> blocks = WorldUtils.getBlocksAround(world, pos);
		for (int i = 0; i < blocks.size(); i++)
		{
			final boolean corner0 = isWaterish(blocks.get(i++).getBlock());
			final boolean corner1 = isWaterish(blocks.get(i++).getBlock());
			boolean corner2;
			if (i == blocks.size())
			{
				corner2 = isWaterish(blocks.get(0).getBlock());
			}
			else
			{
				corner2 = isWaterish(blocks.get(i).getBlock());
			}
			if (corner0 && corner1 && corner2)
			{
				return true;
			}
		}
		return false;
	}

	private boolean isWaterish(Block block)
	{
		return (block == Blocks.water) || (block == this);
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, getVariant(), BlockLiquid.LEVEL);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	protected IProperty getVariant()
	{
		return Constants.AQUATIC_PLANT_VARIANT;
	}

	@Override
	protected Class getMetaClass()
	{
		return EnumAquaticPlant.class;
	}

}
