package genesis.item;

import genesis.block.BlockGenesisPebble;
import genesis.metadata.*;
import genesis.metadata.ToolItems.ToolObjectType;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.Constants;
import genesis.util.WorldUtils;
import genesis.util.Constants.Unlocalized;

import java.util.*;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

@ItemVariantCount(1)
public class ItemPebble extends ItemGenesis
{
	public final BlockGenesisPebble block;
	public final ToolItems owner;
	
	protected final ToolType toolType;
	protected final ToolObjectType<BlockGenesisPebble, ItemPebble> objType;
	
	public ItemPebble(BlockGenesisPebble block, ToolType toolType, ToolItems owner, ToolObjectType<BlockGenesisPebble, ItemPebble> type)
	{
		super();
		
		this.block = block;
		this.owner = owner;
		
		this.toolType = toolType;
		this.objType = type;
		
		setMaxDamage(toolType.toolMaterial.getMaxUses());
		setMaxStackSize(1);
	}
	
	@Override
	public int getMetadata(ItemStack stack)
	{
		return 0;
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
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (!player.isSneaking() && state.getBlock().getMaterial() == Material.rock && state.getBlock().getBlockHardness(world, pos) >= 1)
		{
			player.swingItem();
			stack.setItemDamage(stack.getItemDamage() + 1);
			player.playSound(Constants.ASSETS_PREFIX + "crafting.rock_hit", 2, 0.9F + world.rand.nextFloat() * 0.2F);
			
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
			
			return true;
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
			
			if (offset && !block.isReplaceable(world, pos))
			{
				pos = pos.offset(side);
			}
			
			if (world.getBlockState(pos).getBlock() == block || world.canBlockBePlaced(block, pos, false, side, player, stack))
			{
				state = block.onBlockPlaced(world, pos, side, hitX, hitY, hitZ, stack.getMetadata(), player);
				boolean changed = world.setBlockState(pos, state);
				
				if (changed && !player.capabilities.isCreativeMode && --stack.stackSize <= 0)
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
				
				return true;
			}
		}
		
		return false;
	}
}
