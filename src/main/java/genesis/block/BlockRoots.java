package genesis.block;

import genesis.combo.TreeBlocksAndItems;
import genesis.common.GenesisCreativeTabs;
import genesis.common.sounds.GenesisSoundTypes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static genesis.common.GenesisBlocks.*;
import static net.minecraft.init.Blocks.*;

public class BlockRoots extends BlockGenesis
{
	public static final PropertyBool END = PropertyBool.create("end");
	
	private boolean unInit = true;
	private List<IBlockState> blockStateSupportList = new ArrayList<>();
	
	protected static final double RADIUS = 0.375;
	protected static final AxisAlignedBB BB = new AxisAlignedBB(0.5 - RADIUS, 0, 0.5 - RADIUS, 0.5 + RADIUS, 1, 0.5 + RADIUS);
	
	public BlockRoots()
	{
		super(Material.VINE, GenesisSoundTypes.ROOTS);
		
		setDefaultState(blockState.getBaseState().withProperty(END, true));
		
		setHarvestLevel("axe", 0);
		setHardness(0.5F);
		FIRE.setFireInfo(this, 30, 100);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	public void init()
	{
		blockStateSupportList.addAll(GRASS.getBlockState().getValidStates());
		blockStateSupportList.addAll(DIRT.getBlockState().getValidStates());
		blockStateSupportList.addAll(MOSS.getBlockState().getValidStates());
		blockStateSupportList.addAll(HUMUS.getBlockState().getValidStates());
		blockStateSupportList.addAll(MYCELIUM.getBlockState().getValidStates());
		
		blockStateSupportList.addAll(LOG.getBlockState().getValidStates());
		blockStateSupportList.addAll(LOG2.getBlockState().getValidStates());
		
		for (Block log : TREES.getBlocks(TreeBlocksAndItems.LOG))
			blockStateSupportList.addAll(log.getBlockState().getValidStates());
		
		for (Block log : TREES.getBlocks(TreeBlocksAndItems.DEAD_LOG))
			blockStateSupportList.addAll(log.getBlockState().getValidStates());

		unInit = false;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, END);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;	// TODO: Why are we storing this?? 0.o
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return type.equals("axe");
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		return NULL_AABB;
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
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.withProperty(END, world.getBlockState(pos.down()).getBlock() != this);
	}
	
	private boolean canSupport(IBlockState state)
	{
		if (state.getBlock() == this)
			return true;
		
		if (unInit)
			init();
		
		return blockStateSupportList.contains(state);
	}
	
	public boolean canBlockStay(IBlockAccess world, BlockPos pos)
	{
		return canSupport(world.getBlockState(pos.up()));
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return super.canPlaceBlockAt(world, pos) && canBlockStay(world, pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		super.neighborChanged(state, world, pos, block);

		if (!canBlockStay(world, pos))
		{
			world.destroyBlock(pos, true);
		}
	}
}
