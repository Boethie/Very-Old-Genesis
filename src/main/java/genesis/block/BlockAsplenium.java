package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.util.BlockStateToMetadata;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAsplenium extends BlockBush implements IShearable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockAsplenium()
	{
		blockState = new BlockStateContainer(this, FACING);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setSoundType(GenesisSoundTypes.FERN);
	}

	public static boolean canSustainAsplenum(IBlockState state)
	{
		return state.getMaterial() == Material.rock;
	}

	@Override
	protected boolean canSustainBush(IBlockState state)
	{
		return canSustainAsplenum(state);
	}

	public boolean canBlockStay(IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		return facing.getAxis().isHorizontal() && canSustainBush(world.getBlockState(pos.offset(facing)));
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		return canBlockStay(world, pos, state.getValue(FACING));
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
	{
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos)
				&& canBlockStay(world, pos, side.getOpposite());
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing.getOpposite());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		EnumFacing facing = state.getValue(FACING);
		double offsetAmount = BlockPlant.BB_INSET;
		return BlockPlant.BB.offset(facing.getFrontOffsetX() * offsetAmount,
				facing.getFrontOffsetY() * offsetAmount,
				facing.getFrontOffsetZ() * offsetAmount);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rotation)
	{
		return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return state.withRotation(mirror.toRotation(state.getValue(FACING)));
	}

	@Override
	public boolean isShearable(ItemStack stack, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack stack, IBlockAccess world, BlockPos pos, int fortune)
	{
		return Collections.singletonList(new ItemStack(this));
	}
}
