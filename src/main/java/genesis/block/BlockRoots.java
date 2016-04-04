package genesis.block;

import com.google.common.collect.Lists;

import genesis.combo.TreeBlocksAndItems;
import genesis.common.GenesisCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static genesis.common.GenesisBlocks.*;
import static net.minecraft.init.Blocks.*;

public class BlockRoots extends BlockGenesis
{
	public static final PropertyBool END = PropertyBool.create("end");
	
	private boolean unInit = true;
	private List<IBlockState> blockStateSupportList = Lists.newArrayList();
	
	protected static final double RADIUS = 0.375;
	protected static final AxisAlignedBB BB = new AxisAlignedBB(0.5 - RADIUS, 0, 0.5 - RADIUS, 0.5 + RADIUS, 1, 0.5 + RADIUS);
	
	public BlockRoots()
	{
		super(Material.vine, SoundType.WOOD);
		
		setDefaultState(blockState.getBaseState().withProperty(END, true));
		
		setHardness(0.5F);
		fire.setFireInfo(this, 30, 100);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	public void init()
	{
		blockStateSupportList.addAll(getBlockState().getValidStates());
		
		blockStateSupportList.addAll(grass.getBlockState().getValidStates());
		blockStateSupportList.addAll(mycelium.getBlockState().getValidStates());
		blockStateSupportList.addAll(dirt.getBlockState().getValidStates());
		blockStateSupportList.addAll(moss.getBlockState().getValidStates());
		
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
		return state.getValue(END).equals(true) ? 1 : 0;	// TODO: Why are we storing this?? 0.o
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return blockState.getBaseState().withProperty(END, meta == 1);
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return type.equals("axe");
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos,
			AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
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
	@SideOnly(Side.CLIENT)
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
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return super.canPlaceBlockAt(worldIn, pos) && canSupport(worldIn.getBlockState(pos.up()));
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getCorrectState(worldIn, pos.down());
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (worldIn.isRemote)
		{
			return;
		}
		IBlockState newState = getCorrectState(worldIn, pos.down());
		if (newState.getValue(END) != state.getValue(END))
		{
			worldIn.setBlockState(pos, newState);
		}
	}
	
	private IBlockState getCorrectState(World world, BlockPos pos)
	{	// TODO: Needs to be changed to getActualState because we shouldn't be storing any properties for this block.
		return getDefaultState().withProperty(END, !world.isSideSolid(pos, EnumFacing.UP));
	}
	
	private boolean canSupport(IBlockState state)
	{
		if (unInit)
			init();
		
		return blockStateSupportList.contains(state);
	}
	
	@SubscribeEvent
	public void onAnyBlockBreaks(BlockEvent.BreakEvent event)
	{
		Block block = event.getWorld().getBlockState(event.getPos().down()).getBlock();
		if (!event.getWorld().isRemote && block == this)
		{
			EntityPlayer player = event.getPlayer();
			boolean drop = player instanceof FakePlayer || !player.capabilities.isCreativeMode;
			killRoot(event.getWorld(), event.getPos().down(), drop);
		}
	}
	
	private void killRoot(World world, BlockPos pos, boolean drop)
	{
		do
		{
			world.setBlockToAir(pos);
			if (drop)
			{
				dropBlockAsItem(world, pos, blockState.getBaseState(), 0);
			}
			pos = pos.down();
		}
		while (world.getBlockState(pos).getBlock() == roots);
	}
}
