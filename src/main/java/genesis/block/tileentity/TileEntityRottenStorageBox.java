package genesis.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRottenStorageBox extends TileEntity
{
	public static final int SLOTS_W = 3;
	public static final int SLOTS_H = 2;
	public static final int SLOTS_COUNT = SLOTS_W * SLOTS_H;
	
	protected ItemStack[] inventory = new ItemStack[SLOTS_W * SLOTS_H];
	
	@Override
	public BlockRottenStorageBox getBlockType()
	{
		return (BlockRottenStorageBox) super.getBlockType();
	}
}
