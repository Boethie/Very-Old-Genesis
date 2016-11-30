package genesis.block.tileentity;

import genesis.api.registry.ItemRackItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRack extends TileEntityInventoryLootBase
{
	public TileEntityRack() {
		super(EnumFacing.HORIZONTALS.length);
	}

	public static boolean isItemValid(ItemStack stack)
	{
		if (stack != null)
		{
			Item item = stack.getItem();
			
			return (item instanceof ItemBlock)
					|| (item instanceof ItemPotion)
					|| (item instanceof ItemBucketMilk)
					|| (item instanceof ItemSoup)
					|| item == Items.LAVA_BUCKET
					|| item == Items.WATER_BUCKET
					|| item == Items.CAKE
					|| ItemRackItems.isItemAllowed(item);
		}
		
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 65536;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(pos.getX() - 0.5, pos.getY(), pos.getZ() - 0.5,
				pos.getX() + 1.5, pos.getY() + 1.5, pos.getZ() + 1.5);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	public boolean hasRack(EnumFacing facing)
	{
		return worldObj.getBlockState(pos).getValue(BlockRack.RACKS.get(facing));
	}

	public ItemStack getStackInSide(EnumFacing facing)
	{
		return this.inventory[facing.getHorizontalIndex()];
	}
	
	public void setStackInSide(EnumFacing facing, ItemStack stack)
	{
		if (stack != null)
		{
			stack.stackSize = Math.min(stack.stackSize, getInventoryStackLimit());

			// Check whether there's a rack where the item is being placed.
			if (!hasRack(EnumFacing.getHorizontal(facing.getHorizontalIndex())))
				stack = null;
		}
		inventory[facing.getHorizontalIndex()] = stack;

		this.markDirty();
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return isItemValid(stack);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}
}
