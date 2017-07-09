package genesis.block;

import com.google.common.collect.Sets;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.util.blocks.IAquaticBlock;
import genesis.util.blocks.ISitOnBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
import java.util.Set;

public class BlockSmoker extends BlockGenesis implements IAquaticBlock, ISitOnBlock
{
	public static final Set<Material> VALID_GROUND = Sets.newHashSet(Material.SAND, Material.ROCK, Material.GROUND, Material.CLAY);
	public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.25F, 0F, 0.25F, 0.75F, 1.0F, 0.75F);

	public BlockSmoker()
	{
		super(Material.WATER, SoundType.STONE);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setDefaultState(blockState.getBaseState().withProperty(BlockLiquid.LEVEL, 0));
		setTickRandomly(true);
		setHarvestLevel("pickaxe", 0);
		setHardness(0.65F);
		setResistance(10.0F);
	}

	/*@Override
	public int tickRate(World world)
	{
		return 300;
	}*/

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BlockLiquid.LEVEL, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockLiquid.LEVEL);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockLiquid.LEVEL);
	}

	@Override
	public boolean isReplaceable(IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand)
	{
		BlockPos abovePos = pos.up();
		IBlockState aboveState = world.getBlockState(abovePos);
		if (aboveState.getMaterial().isLiquid() && !(aboveState.getBlock() instanceof BlockSmoker))
		{
			for (int i = 0; i < 8; ++i)
			{
				EnumParticleTypes particleType = world.rand.nextInt(10) == 0 ? EnumParticleTypes.WATER_BUBBLE : EnumParticleTypes.SMOKE_LARGE;
				double x = pos.getX() + 0.5;
				double y = abovePos.getY() + i * 0.1;
				double z = pos.getZ() + 0.5;
				world.spawnParticle(particleType, x, y, z, 0.0D, 0.01D, 0.0D);
			}
		}
	}

	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!canPlaceBlockAt(world, pos))
		{
			world.setBlockState(pos, Blocks.WATER.getDefaultState());
		}
		else if (state.getValue(BlockLiquid.LEVEL) == 0 && world.getBlockState(pos.down()).getBlock() instanceof BlockSmoker)
		{
			world.setBlockState(pos, state.withProperty(BlockLiquid.LEVEL, 1));
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		updateTick(world, pos, state, world.rand);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		if (world.getBlockState(pos.up()).getMaterial() != Material.WATER)
		{
			return false;
		}

		IBlockState belowState = world.getBlockState(pos.down());
		return VALID_GROUND.contains(belowState.getMaterial()) || belowState.getBlock() instanceof BlockSmoker;
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getDefaultState();

		if (world.getBlockState(pos.down()).getBlock() instanceof BlockSmoker)
		{
			state = state.withProperty(BlockLiquid.LEVEL, 1);
		}

		return state;
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return new ItemStack(GenesisBlocks.SMOKER);
	}

	@Override
	public IBlockState getReplacementBlockState()
	{
		return Blocks.WATER.getDefaultState();
	}
}
