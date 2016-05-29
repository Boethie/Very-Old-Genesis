package genesis.item;

import genesis.block.BlockPebble;
import genesis.combo.*;
import genesis.combo.ToolItems.ToolObjectType;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.ToolTypes.ToolType;
import genesis.common.sounds.GenesisSoundEvents;
import genesis.util.WorldUtils;
import genesis.util.math.Vec3f;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.world.World;

@ItemVariantCount(1)
public class ItemPebble extends ItemBlock
{
	public final ToolItems owner;
	
	protected final ToolType toolType;
	protected final ToolObjectType<BlockPebble, ItemPebble> objType;
	
	public ItemPebble(BlockPebble block, ToolItems owner, ToolObjectType<BlockPebble, ItemPebble> type, ToolType toolType, Class<ToolType> variantClass)
	{
		super(block);
		
		this.owner = owner;
		
		this.toolType = toolType;
		this.objType = type;
		
		setMaxDamage(toolType.toolMaterial.getMaxUses());
		setHasSubtypes(true);
		setMaxStackSize(1);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}
	
	public ToolType getToolType()
	{
		return toolType;
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
		
		if (!player.isSneaking() && state.getMaterial() == Material.rock && state.getBlockHardness(world, pos) >= 1)
		{
			//player.swingItem();
			
			ItemStack undamaged = null;
			
			if (stack.stackSize > 1)
			{
				undamaged = stack.splitStack(stack.stackSize - 1);
			}
			
			stack.setItemDamage(stack.getItemDamage() + 10);
			
			if (undamaged != null && !player.inventory.addItemStackToInventory(undamaged))
			{
				player.dropItem(undamaged, false, true);
			}
			
			player.playSound(GenesisSoundEvents.item_pebble_hit, 2, 0.9F + world.rand.nextFloat() * 0.2F);
			
			// If the pebble was destroyed
			if (stack.getItemDamage() > stack.getMaxDamage() || player.capabilities.isCreativeMode)
			{
				ItemStack choppingTool = owner.getStack(ToolItems.CHOPPING_TOOL, toolType.material);
				
				if (--stack.stackSize <= 0)	// Remove an item from the stack
				{	// Remove the stack from the inventory if it's an empty stack.
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
				
				stack.setItemDamage(0);	// Set the stack damage to 0
				
				if (!player.inventory.addItemStackToInventory(choppingTool.copy()))
				{	// Add to inventory or drop as EntityItem.
					player.dropPlayerItemWithRandomChoice(choppingTool, false);
				}
			}
			
			return EnumActionResult.SUCCESS;
		}
		else if (stack.getItemDamage() <= 0)
		{
			boolean replacing = owner.isStateOf(state, toolType, objType) && side != EnumFacing.UP;
			
			if (replacing)
			{
				final float offset = 0.25F;
				hitX += side.getFrontOffsetX() * offset;
				hitY += side.getFrontOffsetY() * offset;
				hitZ += side.getFrontOffsetZ() * offset;
				
				Vec3i blockOffset = new Vec3i(Math.round(hitX - 0.5F), Math.round(hitY - 0.5F), Math.round(hitZ - 0.5F));
				hitX -= blockOffset.getX();
				hitY -= blockOffset.getY();
				hitZ -= blockOffset.getZ();
				
				if (!blockOffset.equals(new Vec3i(0, 0, 0)))
				{
					pos = pos.add(blockOffset);
					replacing = false;
				}
			}
			else if (!state.getBlock().isReplaceable(world, pos))
			{
				pos = pos.offset(side);
				hitX -= side.getFrontOffsetX();
				hitY -= side.getFrontOffsetY();
				hitZ -= side.getFrontOffsetZ();
			}
			
			IBlockState placing = block.onBlockPlaced(world, pos, side, hitX, hitY, hitZ, stack.getMetadata(), player);
			
			if ((replacing && owner.isStateOf(world.getBlockState(pos), toolType, objType))
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
		}
		
		return EnumActionResult.FAIL;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return stack.isItemDamaged() ? 1 : 64;
	}
}
