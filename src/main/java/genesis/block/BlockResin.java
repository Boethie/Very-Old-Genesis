package genesis.block;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumMaterial;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
import genesis.util.AABBUtils;
import genesis.util.BlockStateToMetadata;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockResin extends BlockHorizontal implements IGrowable
{
	public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 0, 3);
	private static final AxisAlignedBB[][] BBS;

	static
	{
		AxisAlignedBB baseL0 = new AxisAlignedBB(0.44D, 0.44D, 0.94D, 0.56D, 0.56D, 1.0D);
		AxisAlignedBB baseL1 = new AxisAlignedBB(0.38D, 0.44D, 0.94D, 0.62D, 0.62D, 1.0D);
		AxisAlignedBB baseL2 = new AxisAlignedBB(0.38D, 0.38D, 0.8D, 0.62D, 0.62D, 1.0D);
		AxisAlignedBB baseL3 = new AxisAlignedBB(0.31000000000000005D, 0.38D, 0.81D, 0.69D, 0.75D, 1.0D);

		BBS = new AxisAlignedBB[4][4];
		for (EnumFacing face : EnumFacing.HORIZONTALS) {
			BBS[face.getHorizontalIndex()][0] = AABBUtils.rotateTo(baseL0, face);
			BBS[face.getHorizontalIndex()][1] = AABBUtils.rotateTo(baseL1, face);
			BBS[face.getHorizontalIndex()][2] = AABBUtils.rotateTo(baseL2, face);
			BBS[face.getHorizontalIndex()][3] = AABBUtils.rotateTo(baseL3, face);
		}
	}

	public BlockResin()
	{
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setTickRandomly(true);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LAYERS, 3));
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return state.getValue(LAYERS) < 3;//TODO Add more conditions
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
	{
		return false;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		world.setBlockState(pos, state.withProperty(LAYERS, state.getValue(LAYERS) + 1), 2);
	}

	protected static boolean canPlaceResin(World world, BlockPos pos, EnumFacing facing)
	{
		IBlockState state = world.getBlockState(pos.offset(facing));
		return facing.getAxis().isHorizontal()
				&& GenesisBlocks.TREES.isStateOf(state, TreeBlocksAndItems.LOG)
				&& GenesisBlocks.TREES.getVariant(state).hasResin();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
	{
		return canPlaceResin(world, pos, side.getOpposite());
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		for (EnumFacing side : EnumFacing.HORIZONTALS)
		{
			if (canPlaceResin(world, pos, side))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getDefaultState().withProperty(FACING, facing);
	}


	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!canPlaceBlockAt(world, pos) || !canPlaceResin(world, pos, state.getValue(FACING).getOpposite()))
		{
			world.destroyBlock(pos, true);
		}
		super.updateTick(world, pos, state, rand);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		if (!canPlaceBlockAt(world, pos) || !canPlaceResin(world, pos, state.getValue(FACING).getOpposite()))
		{
			world.destroyBlock(pos, true);
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return GenesisItems.MATERIALS.getStack(EnumMaterial.RESIN);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		super.harvestBlock(world, player, pos, state, te, stack);

		int layers = state.getValue(LAYERS);

		if (layers > 0)
		{
			world.setBlockState(pos, state.withProperty(LAYERS, layers - 1), 2);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, FACING, LAYERS);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, FACING, LAYERS);
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return state.withRotation(mirror.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, LAYERS);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return GenesisItems.MATERIALS.getItem(EnumMaterial.RESIN);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return GenesisItems.MATERIALS.getItemMetadata(EnumMaterial.RESIN);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BBS[state.getValue(FACING).getHorizontalIndex()][state.getValue(LAYERS)];
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World world, BlockPos pos)
	{
		return NULL_AABB;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
}
