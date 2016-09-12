package genesis.block.tileentity;

import genesis.common.GenesisBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityGenesisFlowerPot extends TileEntityBase
{
	private ItemStack contents;

	public TileEntityGenesisFlowerPot() {}

	public TileEntityGenesisFlowerPot(ItemStack contents)
	{
		setContents(contents);
	}
	
	public void setContents(ItemStack contents)
	{
		this.contents = contents;
	}
	
	public ItemStack getContents()
	{
		return contents;
	}
	
	@Override
	protected void writeVisualData(NBTTagCompound compound, boolean save)
	{
		NBTTagCompound contentsNBT = new NBTTagCompound();
		if (contents != null)
			contents.writeToNBT(contentsNBT);
		compound.setTag("contents", contentsNBT);
	}
	
	@Override
	protected void readVisualData(NBTTagCompound compound, boolean save)
	{
		if (compound.hasKey("contents", NBT.TAG_COMPOUND))
			contents = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("contents"));
		else
			contents = null;
		markDirty();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return newState.getBlock() != GenesisBlocks.FLOWER_POT;
	}
}