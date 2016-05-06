package genesis.item;

import genesis.block.tileentity.BlockRack;
import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumTree;
import genesis.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

@ItemVariantCount(1)
public class ItemRack extends ItemBlock
{
	public final TreeBlocksAndItems owner;
	
	protected final EnumTree variant;
	protected final ObjectType<EnumTree, ? extends BlockRack, ? extends ItemRack> type;
	
	public ItemRack(BlockRack block,
			TreeBlocksAndItems owner,
			ObjectType<EnumTree, ? extends BlockRack, ? extends ItemRack> type,
			EnumTree variant, Class<EnumTree> variantClass)
	{
		super(block);
		
		this.owner = owner;
		this.type = type;
		
		this.variant = variant;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world,
			BlockPos pos, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (!state.getBlock().isReplaceable(world, pos))
		{
			pos = pos.offset(side);
			state = world.getBlockState(pos);
		}
		
		boolean replacing = owner.isStateOf(state, owner.getVariant(stack), type);
		
		IBlockState placing = block.onBlockPlaced(world, pos, side, hitX, hitY, hitZ, stack.getMetadata(), player);
		
		if (replacing || WorldUtils.canBlockBePlaced(world, placing, pos, side, player, stack))
		{
			if (world.setBlockState(pos, placing))
			{
				world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						block.getSoundType().getPlaceSound(), SoundCategory.BLOCKS,
						(block.getSoundType().getVolume() + 1F) / 2F, block.getSoundType().getPitch() * 0.8F, false);
				
				if (!player.capabilities.isCreativeMode && --stack.stackSize <= 0)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
			
			return EnumActionResult.SUCCESS;
		}
		
		return EnumActionResult.FAIL;
	}
}
