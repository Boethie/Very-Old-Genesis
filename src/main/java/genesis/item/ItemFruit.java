package genesis.item;

import java.util.List;

import genesis.combo.ObjectType;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.variant.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFruit extends ItemGenesisFood<EnumTree>
{
	public ItemFruit(VariantsOfTypesCombo<EnumTree> owner,
			ObjectType<Block, ? extends ItemGenesisEdible<EnumTree>> type,
			List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super(owner, type, variants, variantClass);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize == 0)
			return false;
		
		if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos))
			pos = pos.offset(side);
		
		if (!player.canPlayerEdit(pos, side, stack))
			return false;
		
		EnumTree variant = owner.getVariant(stack);
		
		if (!owner.containsVariant(TreeBlocksAndItems.HANGING_FRUIT, variant))
			return false;
		
		VariantsOfTypesCombo<EnumTree>.VariantData data = owner.getVariantData(TreeBlocksAndItems.HANGING_FRUIT, variant);
		
		IBlockState state = data.block.getDefaultState().withProperty(owner.getVariantProperty(data.block), variant);
		Block block = state.getBlock();
		
		if (world.canBlockBePlaced(block, pos, false, side, null, stack) && world.setBlockState(pos, state))
		{
			SoundType sound = block.stepSound;
			world.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					sound.getPlaceSound(), (sound.getVolume() + 1F) / 2F, sound.getFrequency() * 0.8F);
			stack.stackSize--;
			ItemBlock.setTileEntityNBT(world, player, pos, stack);
			state.getBlock().onBlockPlacedBy(world, pos, state, player, stack);
			
			return true;
		}
		
		return false;
	}
}
