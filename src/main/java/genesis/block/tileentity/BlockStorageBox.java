package genesis.block.tileentity;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import genesis.common.Genesis;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisGuiHandler;
import genesis.util.FacingHelpers;
import genesis.util.BlockStateToMetadata;
import genesis.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.world.*;

public class BlockStorageBox extends Block
{
	public static final int MAX_WIDTH = 2;
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool LEFT = PropertyBool.create("left");
	public static final PropertyBool RIGHT = PropertyBool.create("right");
	
	public BlockStorageBox()
	{
		super(Material.wood);
		
		setStepSound(soundTypeWood);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		setHardness(2.5F);
	}
	
	@Override
	public BlockState createBlockState()
	{
		return new BlockState(this, FACING, LEFT, RIGHT);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntityStorageBox box = getTileEntity(world, pos);
		
		if (box != null)
		{
			Axis axis = box.getAxis();
			
			if (axis != null)
			{
				Random endRand = new Random(MathHelper.getPositionRandom(box.getMainBox().getPos()));
				EnumFacing facing = FacingHelpers.getRandomFacing(axis, endRand).rotateY();
				
				state = state.withProperty(FACING, facing);
				state = state.withProperty(LEFT, box.isConnected(facing.rotateYCCW()));
				state = state.withProperty(RIGHT, box.isConnected(facing.rotateY()));
			}
		}
		
		return state;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityStorageBox box = getTileEntity(world, pos);
		
		if (box != null)
		{
			InventoryHelper.dropInventoryItems(world, pos, box);
		}
	}
	
	protected void notifyNeighborsClient(World world, BlockPos pos)
	{
		if (world.isRemote)
		{
			for (EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				BlockPos notifyPos = pos.offset(facing);
				IBlockState notifyState = world.getBlockState(notifyPos);
				notifyState.getBlock().onNeighborBlockChange(world, notifyPos, notifyState, this);
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
	{
		if (world.isRemote)
		{	// Make the box connect to neighboring boxes ASAP.
			notifyNeighborsClient(world, pos);
		}
		
		if (stack.hasDisplayName())
		{
			TileEntityStorageBox box = getTileEntity(world, pos);
			
			if (box != null)
			{
				box.setCustomInventoryName(stack.getDisplayName());
			}
		}
	}
	
	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		boolean out = super.removedByPlayer(world, pos, player, willHarvest);
		notifyNeighborsClient(world, pos);
		return out;
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block otherBlock)
	{	// TODO: Investigate bug with two boxes on either side connecting to one in the middle wrong.
		TileEntityStorageBox box = getTileEntity(world, pos);
		Axis boxAxis = box.getAxis();
		
		int curWidth = box.getWidth();
		
		for (EnumFacing checkSide : EnumFacing.HORIZONTALS)
		{
			if (boxAxis == null ||
				checkSide.getAxis() == boxAxis)
			{
				BlockPos checkPos = pos.offset(checkSide);
				TileEntityStorageBox checkBox = getTileEntity(world, checkPos);
				
				if (box.isConnected(checkSide))
				{
					box.setConnected(checkSide, checkBox != null);
				}
				else if (checkBox != null)
				{
					int checkWidth = checkBox.getWidth();
					
					if (curWidth + checkWidth <= MAX_WIDTH)
					{
						curWidth += checkWidth;
						box.setConnected(checkSide, true);
					}
					else
					{
						checkBox.sendOpenDirectionUpdate();
						checkBox.sendUsersUpdate();
					}
				}
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isAirBlock(pos.up()))
		{
			return false;
		}
		
		TileEntityStorageBox box = getTileEntity(world, pos);

		if (box != null)
		{
			for (TileEntityStorageBox checkBox : box.iterableFromMainToEnd())
			{
				if (!pos.equals(checkBox.getPos()) && !world.isAirBlock(checkBox.getPos().up()))
				{
					return false;
				}
			}
			
			player.openGui(Genesis.instance, GenesisGuiHandler.STORAGE_BOX_ID, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onBlockEventReceived(World world, BlockPos pos, IBlockState state, int id, int value)
	{
		TileEntityStorageBox box = getTileEntity(world, pos);
		
		if (box != null)
		{
			return box.receiveClientEvent(id, value);
		}
		
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntityStorageBox createTileEntity(World world, IBlockState state)
	{
		return new TileEntityStorageBox();
	}
	
	public TileEntityStorageBox getTileEntity(IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEnt = world.getTileEntity(pos);
		
		if (tileEnt instanceof TileEntityStorageBox)
		{
			return (TileEntityStorageBox) tileEnt;
		}
		
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
}
