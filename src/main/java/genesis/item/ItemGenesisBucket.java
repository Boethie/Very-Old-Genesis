package genesis.item;

import genesis.client.GenesisClient;
import genesis.common.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		
		if (!isEmpty() && containedBlock.getMaterial() == Material.water)
		{
			MinecraftForge.EVENT_BUS.register(this);
		}
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
			
			IBlockState hitState = world.getBlockState(hitPos);
			Block hitBlock = hitState.getBlock();
			
			if (empty)
			{
				if (!player.canPlayerEdit(hitPos.offset(hit.sideHit), hit.sideHit, stack))
				{
					return stack;
				}
				
				if (hitBlock.getMaterial() == Material.water && ((Integer) hitState.getValue(BlockLiquid.LEVEL)).intValue() == 0)
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
	
	protected boolean filledWater = false;
	
	@SubscribeEvent
	public void onBlockInteracted(PlayerInteractEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		
		World world = event.world;
		BlockPos pos = event.pos;
		
		IBlockState state = Blocks.air.getDefaultState();
		if (pos != null)
			state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		ItemStack stack = player.getHeldItem();
		
		switch (event.action)
		{
		case RIGHT_CLICK_BLOCK:
			if (stack != null && stack.getItem() == this && block == Blocks.cauldron)
			{
				event.useBlock = Result.DENY;
				
				int cauldronLevel = (Integer) state.getValue(BlockCauldron.LEVEL);
				int max = 0;
				
				for (Object val : BlockCauldron.LEVEL.getAllowedValues())
				{
					int intVal = (Integer) val;
					
					if (intVal > max)
					{
						max = intVal;
					}
				}
				
				if (cauldronLevel < max)
				{
					world.setBlockState(pos, state.withProperty(BlockCauldron.LEVEL, Math.min(cauldronLevel + 3, max)));
					
					if (!player.capabilities.isCreativeMode)
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(GenesisItems.ceramic_bucket));
					}
					
					event.useItem = Result.DENY;
					
					if (world.isRemote)	// We must send a packet to the server telling it that the player right clicked or else it won't fill the cauldron server side.
					{
						filledWater = true;	// Used to prevent RIGHT_CLICK_AIR going through and placing water in onItemRightClick
						
						Minecraft mc = GenesisClient.getMC();
						EntityPlayerSP spPlayer = mc.thePlayer;
						
						if (spPlayer == player)
						{
							Vec3 hitVec = mc.objectMouseOver.hitVec;
							hitVec = hitVec.subtract(pos.getX(), pos.getY(), pos.getZ());
							Packet packet = new C08PacketPlayerBlockPlacement(pos, event.face.getIndex(), stack, (float) hitVec.xCoord, (float) hitVec.yCoord, (float) hitVec.zCoord);
							spPlayer.sendQueue.addToSendQueue(packet);
							
							spPlayer.swingItem();
							event.setCanceled(true);
						}
					}
				}
			}
			
			break;
		case RIGHT_CLICK_AIR:
			if (stack != null && stack.getItem() == this && filledWater)
			{
				event.setCanceled(true);
				filledWater = false;
			}
			
			break;
		case LEFT_CLICK_BLOCK:
			break;
		}
	}
}
