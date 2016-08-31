package genesis.item;

import genesis.common.*;
import genesis.util.Actions;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemGenesisBucket extends ItemBucket
{
	protected IBlockState containedState;
	
	public ItemGenesisBucket(IBlockState state)
	{
		super(state.getBlock());
		
		containedState = state;
		
		if (isEmpty())
		{
			setMaxStackSize(16);
		}
		
		setCreativeTab(GenesisCreativeTabs.MISC);
		
		if (!isEmpty() && containedState.getMaterial() == Material.WATER)
		{
			MinecraftForge.EVENT_BUS.register(this);
		}
	}
	
	public boolean isEmpty()
	{
		return containedState.getBlock() == Blocks.AIR;
	}

	protected ItemStack fillBucket(ItemStack emptyBucket, EntityPlayer player, Item fullItem)
	{
		if (!player.capabilities.isCreativeMode)
		{
			ItemStack full = new ItemStack(fullItem);
			
			if (--emptyBucket.stackSize <= 0)
			{
				return full;
			}
			else if (!player.inventory.addItemStackToInventory(full))
			{
				player.dropItem(full, false);
			}
		}
		
		return emptyBucket;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		boolean empty = isEmpty();
		RayTraceResult hit = rayTrace(world, player, empty);
		
		if (hit == null)
			return Actions.fail(stack);
		
		ActionResult<ItemStack> event = ForgeEventFactory.onBucketUse(player, world, stack, hit);
		
		if (event != null)
			return event;
		
		if (hit.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			BlockPos hitPos = hit.getBlockPos();
			
			if (!world.isBlockModifiable(player, hitPos))
				return Actions.fail(stack);
			
			IBlockState hitState = world.getBlockState(hitPos);
			
			if (empty)
			{
				if (!player.canPlayerEdit(hitPos.offset(hit.sideHit), hit.sideHit, stack))
					return Actions.fail(stack);
				
				if (hitState.getMaterial() == Material.WATER && hitState.getValue(BlockLiquid.LEVEL).intValue() == 0)
				{
					world.setBlockToAir(hitPos);
					player.addStat(StatList.getObjectUseStats(this));
					return Actions.success(fillBucket(stack, player, GenesisItems.ceramic_bucket_water));
				}
			}
			else
			{
				BlockPos placePos = hitPos.offset(hit.sideHit);
				
				if (!player.canPlayerEdit(placePos, hit.sideHit, stack))
				{
					return Actions.fail(stack);
				}
				
				if (tryPlaceContainedLiquid(player, world, placePos) && !player.capabilities.isCreativeMode)
				{
					player.addStat(StatList.getObjectUseStats(this));
					return Actions.success(new ItemStack(GenesisItems.ceramic_bucket));
				}
			}
		}
		
		return Actions.fail(stack);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
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
				player.inventory.addItemStackToInventory(new ItemStack(GenesisItems.ceramic_bucket_milk));
			}
		}
		
		return true;
	}
	
	protected boolean filledWater = false;
	
	@SubscribeEvent
	public void onBlockInteracted(PlayerInteractEvent event)
	{
		/* TODO: Adapt to dual wielding.
		EntityPlayer player = event.getEntityPlayer();
		
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		
		ItemStack stack = player.getHeldItem();
		
		switch (event.getAction())
		{
		case RIGHT_CLICK_BLOCK:
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			
			if (stack != null && stack.getItem() == this && block == Blocks.cauldron)
			{
				event.useBlock = Result.DENY;
				
				int cauldronLevel = state.getValue(BlockCauldron.LEVEL);
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
							Vec3d hitVec = mc.objectMouseOver.hitVec;
							hitVec = hitVec.subtract(pos.getX(), pos.getY(), pos.getZ());
							Packet<?> packet = new C08PacketPlayerBlockPlacement(pos, event.face.getIndex(), stack, (float) hitVec.xCoord, (float) hitVec.yCoord, (float) hitVec.zCoord);
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
		}*/
	}
}
