package genesis.item;

import java.util.List;

import genesis.block.BlockDung;
import genesis.combo.DungBlocksAndItems;
import genesis.combo.IItemMetadataBitMask;
import genesis.combo.ObjectType;
import genesis.combo.variant.EnumDung;
import genesis.util.BitMask;
import genesis.util.WorldUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockDung extends ItemBlock implements IItemMetadataBitMask
{
	public final DungBlocksAndItems owner;
	
	protected final List<EnumDung> variants;
	protected final ObjectType<EnumDung, ? extends BlockDung, ? extends ItemBlockDung> type;
	
	public ItemBlockDung(BlockDung block,
			DungBlocksAndItems owner,
			ObjectType<EnumDung, ? extends BlockDung, ? extends ItemBlockDung> type,
			List<EnumDung> variants, Class<EnumDung> variantClass)
	{
		super(block);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
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
		
		boolean replacing = side == EnumFacing.UP
				&& owner.isStateOf(state, owner.getVariant(stack), type)
				&& state.getValue(BlockDung.HEIGHT) < BlockDung.MAX_HEIGHT;
		
		if (!replacing && !state.getBlock().isReplaceable(world, pos))
		{
			pos = pos.offset(side);
			state = world.getBlockState(pos);
		}
		
		IBlockState placing = block.onBlockPlaced(world, pos, side, hitX, hitY, hitZ, stack.getMetadata(), player);
		
		if ((replacing && state != placing && !WorldUtils.isAnyEntityInBlock(world, placing, pos, player))
				|| WorldUtils.canBlockBePlaced(world, placing, pos, side, player, stack))
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
	
	@Override
	public BitMask getMetadataBitMask()
	{
		return BitMask.forValueCount(variants.size());
	}
}

