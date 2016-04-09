package genesis.item;

import genesis.block.BlockPebble;
import genesis.combo.*;
import genesis.combo.ToolItems.ToolObjectType;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.ToolTypes.ToolType;
import genesis.sounds.GenesisSoundEvents;
import genesis.stats.GenesisAchievements;

import net.minecraft.block.material.Material;
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
public class ItemPebble extends ItemGenesis
{
	public final BlockPebble block;
	public final ToolItems owner;
	
	protected final ToolType toolType;
	protected final ToolObjectType<BlockPebble, ItemPebble> objType;
	
	public ItemPebble(BlockPebble block, ToolItems owner, ToolObjectType<BlockPebble, ItemPebble> type, ToolType toolType, Class<ToolType> variantClass)
	{
		super();
		
		this.block = block;
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
				
				player.addStat(GenesisAchievements.gettingChoppingTool, 1);
			}
			
			return EnumActionResult.SUCCESS;
		}
		else if (stack.getItemDamage() <= 0)
		{
			boolean offset = true;
			
			if (state.getBlock() == block && side != EnumFacing.UP)
			{
				final float epsilon = 0.0005F;
				
				offset = false;
				hitX += side.getFrontOffsetX() * epsilon;
				hitY += side.getFrontOffsetY() * epsilon;
				hitZ += side.getFrontOffsetZ() * epsilon;
				
				if (hitX < 0)
				{
					hitX += 1;
					pos = pos.add(-1, 0, 0);
				}
				else if (hitX > 1)
				{
					hitX -= 1;
					pos = pos.add(1, 0, 0);
				}
				
				if (hitY < 0)
				{
					hitY += 1;
					pos = pos.add(0, -1, 0);
				}
				else if (hitY > 1)
				{
					hitY -= 1;
					pos = pos.add(0, 1, 0);
				}
	
				if (hitZ < 0)
				{
					hitZ += 1;
					pos = pos.add(0, 0, -1);
				}
				else if (hitZ > 1)
				{
					hitZ -= 1;
					pos = pos.add(0, 0, 1);
				}
			}
			
			if (offset && !state.getBlock().isReplaceable(world, pos))
			{
				pos = pos.offset(side);
			}
			
			if (world.getBlockState(pos).getBlock() == block || world.canBlockBePlaced(block, pos, false, side, player, stack))
			{
				state = block.onBlockPlaced(world, pos, side, hitX, hitY, hitZ, stack.getMetadata(), player);
				
				if (world.setBlockState(pos, state))
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
