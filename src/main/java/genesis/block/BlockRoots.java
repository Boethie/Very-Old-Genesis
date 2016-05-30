package genesis.block;

import com.google.common.collect.Lists;

import genesis.combo.TreeBlocksAndItems;
import genesis.common.GenesisCreativeTabs;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.util.AABBUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.List;

import static genesis.common.GenesisBlocks.*;
import static net.minecraft.init.Blocks.*;

public class BlockRoots extends BlockGenesis
{
	public static final PropertyBool END = PropertyBool.create("end");
	
	private boolean unInit = true;
	private List<IBlockState> blockStateSupportList = Lists.newArrayList();
	
	protected static final double RADIUS = 0.375;
	protected static final AxisAlignedBB BB = AABBUtils.create(0.5, 1, 0.5).expand(RADIUS, 0, RADIUS);
	
	public BlockRoots()
	{
		super(Material.vine, GenesisSoundTypes.ROOTS);
		
		setDefaultState(blockState.getBaseState().withProperty(END, true));
		
		setHardness(0.5F);
		fire.setFireInfo(this, 30, 100);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	public void init()
	{
		blockStateSupportList.addAll(grass.getBlockState().getValidStates());
		blockStateSupportList.addAll(dirt.getBlockState().getValidStates());
		blockStateSupportList.addAll(moss.getBlockState().getValidStates());
		blockStateSupportList.addAll(mycelium.getBlockState().getValidStates());
		
		blockStateSupportList.addAll(log.getBlockState().getValidStates());
		blockStateSupportList.addAll(log2.getBlockState().getValidStates());
		
		for (Block log : trees.getBlocks(TreeBlocksAndItems.LOG))
			blockStateSupportList.addAll(log.getBlockState().getValidStates());
		
		for (Block log : trees.getBlocks(TreeBlocksAndItems.DEAD_LOG))
			blockStateSupportList.addAll(log.getBlockState().getValidStates());
		
		blockStateSupportList.addAll(prototaxites_mycelium.getBlockState().getValidStates());
		
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
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block otherBlock)
	{
		if (!canBlockStay(world, pos))
		{
			world.destroyBlock(pos, true);
		}
	}
}
