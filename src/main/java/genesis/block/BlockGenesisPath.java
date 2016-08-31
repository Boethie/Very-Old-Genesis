package genesis.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGenesisPath extends Block
{
	private static final AxisAlignedBB BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
	private final IBlockState modelState;
	
	public BlockGenesisPath(IBlockState modelState)
	{
		super(modelState.getMaterial());
		this.modelState = modelState;
		setLightOpacity(255);
		setSoundType(SoundType.GROUND);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		switch (side)
		{
		case UP:
			return true;
		case NORTH:
		case SOUTH:
		case WEST:
		case EAST:
			IBlockState sideState = world.getBlockState(pos.offset(side));
			Block sideBlock = sideState.getBlock();
			return !sideState.isOpaqueCube()
					&& sideBlock != Blocks.FARMLAND
					&& sideBlock != Blocks.GRASS_PATH
					&& sideBlock != this;
		default:
			return super.shouldSideBeRendered(state, world, pos, side);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BB;
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
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return modelState.getBlock().getItemDropped(modelState, rand, fortune);
	}
	
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this);
	}

	@Override
	public void onNeighborChange(IBlockAccess blockAccess, BlockPos pos, BlockPos neighbor)
	{
		if (blockAccess instanceof World)
		{
			World world = (World) blockAccess;

			if (world.getBlockState(pos.up()).getMaterial().isSolid())
			{
				world.setBlockState(pos, modelState);
			}
		}
	}
}
