package genesis.block;

import genesis.util.AABBUtils;
import java.util.Random;

import genesis.combo.variant.EnumMaterial;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockResin extends BlockHorizontal implements IGrowable
{
	public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 0, 3);
	private static final AxisAlignedBB[] BBS = new AxisAlignedBB[4];

	static
	{
		AxisAlignedBB bb = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.125D);
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			BBS[facing.getHorizontalIndex()] = AABBUtils.rotateTo(bb, facing);
		}
	}

	public BlockResin()
	{
		super(Material.wood);
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
		world.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING)).withProperty(LAYERS, state.getValue(LAYERS) + 1), 2);
	}

	protected static boolean canPlaceResin(World world, BlockPos pos, EnumFacing facing)
	{
		IBlockState state = world.getBlockState(pos.offset(facing));
		EnumTree variant = GenesisBlocks.trees.getVariant(state);
		return facing.getHorizontalIndex() != -1 && variant != null && variant.hasResin();
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
		return getStateFromMeta(meta);
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (!canPlaceBlockAt(world, pos) || !canPlaceResin(world, pos, state.getValue(FACING).getOpposite()))
		{
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		super.harvestBlock(world, player, pos, state, te, stack);
		
		int layers = state.getValue(LAYERS);
		
		if (layers > 0)
		{
			world.setBlockState(pos, getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(LAYERS, layers - 1), 2);
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
		return GenesisItems.materials.getItem(EnumMaterial.RESIN);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return GenesisItems.materials.getItemMetadata(EnumMaterial.RESIN);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BBS[state.getValue(FACING).getHorizontalIndex()];
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World world, BlockPos pos)
	{
		return NULL_AABB;
	}
}
