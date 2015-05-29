package genesis.item;

import genesis.common.*;
import genesis.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemGenesisBucket extends ItemBucket
{
	protected Block containedBlock;
	
	public ItemGenesisBucket(Block block)
	{
		super(block);
		
		containedBlock = block;
		
		if (isEmpty())
		{
			setMaxStackSize(16);
		}
		
		setCreativeTab(GenesisCreativeTabs.MISC);
	}
	
	public boolean isEmpty()
	{
		return containedBlock == Blocks.air;
	}

    protected ItemStack fillBucket(ItemStack emptyBucket, EntityPlayer player, Item fullItem)
    {
        if (player.capabilities.isCreativeMode)
        {
            return emptyBucket;
        }
        else
        {
        	ItemStack full = new ItemStack(fullItem);
        	
	        if (--emptyBucket.stackSize <= 0)
	        {
	            return full;
	        }
	        else
	        {
	            if (!player.inventory.addItemStackToInventory(full))
	            {
	                player.dropPlayerItemWithRandomChoice(full, false);
	            }
	
	            return emptyBucket;
	        }
        }
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		boolean empty = isEmpty();
		MovingObjectPosition hit = getMovingObjectPositionFromPlayer(world, player, empty);
		
		if (hit == null)
		{
			return stack;
		}
		
		ItemStack eventOutput = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, world, stack, hit);
		
		if (eventOutput != null)
		{
			return eventOutput;
		}
		
		if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			BlockPos hitPos = hit.getBlockPos();
			
			if (!world.isBlockModifiable(player, hitPos))
			{
				return stack;
			}
			
			if (empty)
			{
				if (!player.canPlayerEdit(hitPos.offset(hit.sideHit), hit.sideHit, stack))
				{
					return stack;
				}
				
				IBlockState hitState = world.getBlockState(hitPos);
				Material hitMaterial = hitState.getBlock().getMaterial();
				
				if (hitMaterial == Material.water && ((Integer) hitState.getValue(BlockLiquid.LEVEL)).intValue() == 0)
				{
					world.setBlockToAir(hitPos);
					player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
					return fillBucket(stack, player, GenesisItems.ceramic_bucket_water);
				}
			}
			else
			{
				BlockPos placePos = hitPos.offset(hit.sideHit);
				
				if (!player.canPlayerEdit(placePos, hit.sideHit, stack))
				{
					return stack;
				}
				
				if (tryPlaceContainedLiquid(world, placePos) && !player.capabilities.isCreativeMode)
				{
					player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
					return new ItemStack(GenesisItems.ceramic_bucket);
				}
			}
		}
		
		return stack;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
	{
		if (isEmpty() && target instanceof EntityCow)
		{
			if (stack.stackSize == 1)
			{
				stack.setItem(GenesisItems.ceramic_bucket_milk);
			}
			else
			{
				stack.stackSize--;
				playerIn.inventory.addItemStackToInventory(new ItemStack(GenesisItems.ceramic_bucket_milk));
			}
		}
		
		return true;
	}

	@Override
	public ItemGenesisBucket setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(Constants.PREFIX + "misc." + unlocalizedName);

		return this;
	}
	
	@Override
	public ItemGenesisBucket setContainerItem(Item containerItem)
	{
		super.setContainerItem(containerItem);
		
		return this;
	}
}
