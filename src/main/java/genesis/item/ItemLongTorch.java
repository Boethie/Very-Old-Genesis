package genesis.item;

import genesis.block.BlockLongTorch;
import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Simeon on 9/15/2015.
 */
public class ItemLongTorch extends ItemBlock
{
		public ItemLongTorch(Block longTorch)
		{
				super(longTorch);
				this.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		}

		/**
		 * Called when a Block is right-clicked with this Item
		 *
		 * @param pos  The block being right-clicked
		 * @param side The side being right-clicked
		 */
		public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
		{
				if (side != EnumFacing.UP)
				{
						return false;
				}
				else
				{
						IBlockState iblockstate = worldIn.getBlockState(pos);
						Block block = iblockstate.getBlock();

						if (!block.isReplaceable(worldIn, pos))
						{
								pos = pos.offset(side);
						}

						if (!playerIn.canPlayerEdit(pos, side, stack))
						{
								return false;
						}
						else if (!this.block.canPlaceBlockAt(worldIn, pos))
						{
								return false;
						}
						else
						{
								placeTorch(worldIn, pos, this.block);
								--stack.stackSize;
								return true;
						}
				}
		}

		public static void placeTorch(World worldIn, BlockPos pos, Block torch)
		{
				worldIn.setBlockState(pos, torch.getDefaultState().withProperty(BlockLongTorch.PART_PROPERTY, BlockLongTorch.PART.BOTTOM));
				worldIn.setBlockState(pos.up(), torch.getDefaultState().withProperty(BlockLongTorch.PART_PROPERTY, BlockLongTorch.PART.TOP));
		}
}
