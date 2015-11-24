package genesis.block;

import com.google.common.collect.Lists;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.TreeBlocksAndItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
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
	
	public BlockRoots()
	{
		super(Material.vine);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setDefaultState(blockState.getBaseState().withProperty(END, true));
		setHardness(0.5F);
		setStepSound(Block.soundTypeWood);
		setBlockBounds(2 / 16F, 0, 2 / 16F, 14 / 16F, 1, 14 / 16F);
		fire.setFireInfo(this, 30, 100);
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
		{
			blockStateSupportList.addAll(log.getBlockState().getValidStates());
		}
		for (Block log : trees.getBlocks(TreeBlocksAndItems.DEAD_LOG))
		{
			blockStateSupportList.addAll(log.getBlockState().getValidStates());
		}
		
		blockStateSupportList.addAll(prototaxites_mycelium.getBlockState().getValidStates());
		
		unInit = false;
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, END);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(END).equals(true) ? 1 : 0;
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
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	@Override
	public boolean isReplaceable(World worldIn, BlockPos pos)
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
	
	private IBlockState getCorrectState(World world, BlockPos posBelow)
	{
		IBlockState stateBelow = world.getBlockState(posBelow);
		Block blockBelow = stateBelow.getBlock();
		AxisAlignedBB aabb = blockBelow.getCollisionBoundingBox(world, posBelow, stateBelow);
		boolean end = blockBelow != this && aabb == null;
		return blockState.getBaseState().withProperty(END, end);
	}
	
	private boolean canSupport(IBlockState state)
	{
		if (unInit)
		{
			init();
		}
		return blockStateSupportList.contains(state);
	}
	
	@SubscribeEvent
	public void onAnyBlockBreaks(BlockEvent.BreakEvent event)
	{
		Block block = event.world.getBlockState(event.pos.down()).getBlock();
		if (!event.world.isRemote && block == this)
		{
			EntityPlayer player = event.getPlayer();
			boolean drop = player instanceof FakePlayer || !player.capabilities.isCreativeMode;
			killRoot(event.world, event.pos.down(), drop);
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
