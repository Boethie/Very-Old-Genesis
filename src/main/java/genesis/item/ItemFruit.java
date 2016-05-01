package genesis.item;

import java.util.List;

import genesis.combo.ObjectType;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.variant.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFruit extends ItemGenesisFood<EnumTree>
{
	public ItemFruit(TreeBlocksAndItems owner,
			ObjectType<Block, ? extends ItemGenesisEdible<EnumTree>> type,
			List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super(owner, type, variants, variantClass);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize == 0)
			return EnumActionResult.FAIL;
		
		if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos))
			pos = pos.offset(side);
		
		if (!player.canPlayerEdit(pos, side, stack))
			return EnumActionResult.FAIL;
		
		EnumTree variant = owner.getVariant(stack);
		
		if (!owner.containsVariant(TreeBlocksAndItems.HANGING_FRUIT, variant))
			return EnumActionResult.FAIL;
		
		TreeBlocksAndItems.VariantData data = owner.getVariantData(TreeBlocksAndItems.HANGING_FRUIT, variant);
		
		IBlockState state = data.block.getDefaultState().withProperty(owner.getVariantProperty(data.block), variant);
		Block block = state.getBlock();
		
		if (world.canBlockBePlaced(block, pos, false, side, null, stack) && world.setBlockState(pos, state))
		{
			SoundType sound = block.getSoundType();
			world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					sound.getPlaceSound(), SoundCategory.BLOCKS,
					(sound.getVolume() + 1F) / 2F, sound.getPitch() * 0.8F, false);
			
			stack.stackSize--;
			
			ItemBlock.setTileEntityNBT(world, player, pos, stack);
			state.getBlock().onBlockPlacedBy(world, pos, state, player, stack);
			
			return EnumActionResult.SUCCESS;
		}
		
		return EnumActionResult.FAIL;
	}
}
