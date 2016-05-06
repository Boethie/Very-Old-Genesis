package genesis.item;

import java.util.*;

import genesis.client.GenesisClient;
import genesis.combo.*;
import genesis.combo.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.combo.variant.MultiMetadataList.MultiMetadata;
import genesis.common.GenesisItems;
import genesis.util.Actions;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerBlockPlacement;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ItemCeramicBowl extends ItemGenesis
{
	public final ItemsCeramicBowls owner;
	
	protected final List<MultiMetadata> variants;
	protected final ObjectType<MultiMetadata, Block, ItemCeramicBowl> type;
	
	public ItemCeramicBowl(ItemsCeramicBowls owner,
			ObjectType<MultiMetadata, Block, ItemCeramicBowl> type,
			List<MultiMetadata> variants, Class<MultiMetadata> variantClass)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		this.variants = variants;
		
		setHasSubtypes(true);
		
		if (variants.contains(EnumCeramicBowls.WATER_BOWL))
		{
			MinecraftForge.EVENT_BUS.register(this);
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		owner.fillSubItems(type, variants, subItems);
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		if (owner.getVariant(stack).getOriginal() == EnumCeramicBowls.WATER_BOWL)
			return owner.getStack(EnumCeramicBowls.BOWL);
		
		return null;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		if (owner.getVariant(stack).getOriginal() == EnumCeramicBowls.WATER_BOWL)
			return EnumAction.DRINK;
		
		return EnumAction.NONE;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		EnumCeramicBowls variant = (EnumCeramicBowls) owner.getVariant(stack).getOriginal();
		
		switch (variant)
		{
		case BOWL:
			RayTraceResult hit = getMovingObjectPositionFromPlayer(world, player, true);
	
			if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				BlockPos hitPos = hit.getBlockPos();
				
				if (world.isBlockModifiable(player, hitPos) && player.canPlayerEdit(hitPos.offset(hit.sideHit), hit.sideHit, stack))
				{
					if (world.getBlockState(hitPos).getMaterial() == Material.water)
					{
						player.addStat(StatList.getObjectUseStats(this));
						
						ItemStack newStack = GenesisItems.bowls.getStack(EnumCeramicBowls.WATER_BOWL);
						stack.stackSize--;
						
						if (stack.stackSize <= 0)
						{
							return Actions.success(newStack);
						}
						
						if (!player.inventory.addItemStackToInventory(newStack))
						{
							player.dropPlayerItemWithRandomChoice(newStack, false);
						}
						
						return Actions.success(stack);
					}
				}
			}
			break;
		case WATER_BOWL:
			if (!player.capabilities.isCreativeMode)
			{
				player.setActiveHand(hand);
				return Actions.success(stack);
			}
			break;
		}
		
		return Actions.fail(stack);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
	{
		EnumCeramicBowls variant = (EnumCeramicBowls) owner.getVariant(stack).getOriginal();
		
		switch (variant)
		{
		case WATER_BOWL:
			ItemStack empty = getContainerItem(stack);
			
			if (--stack.stackSize <= 0)
			{
				return empty;
			}
			
			EntityPlayer player = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
			
			if (player != null && !player.inventory.addItemStackToInventory(empty))
			{
				player.dropPlayerItemWithRandomChoice(empty, false);
			}
			
			break;
		default:
			break;
		}
		
		return stack;
	}
	
	@SubscribeEvent
	public void onBlockInteracted(PlayerInteractEvent event)
	{
		/* TODO: Adapt to dual-wielding.
		EntityPlayer player = event.getEntityPlayer();
		
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		
		ItemStack stack = player.getHeldItem();
		
		switch (event.getAction())
		{
		case RIGHT_CLICK_BLOCK:
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			
			if (owner.isStackOf(stack, EnumCeramicBowls.BOWL) && block == Blocks.cauldron)
			{
				event.setUseBlock(Result.DENY);
				
				int cauldronLevel = state.getValue(BlockCauldron.LEVEL);
				
				if (cauldronLevel > 0)
				{
					world.setBlockState(pos, state.withProperty(BlockCauldron.LEVEL, Math.max(cauldronLevel - 1, 0)));
					
					if (!player.capabilities.isCreativeMode)
					{
						stack.stackSize--;
						
						if (stack.stackSize <= 0)
						{
							player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
						}
						
						ItemStack newStack = owner.getStack(EnumCeramicBowls.WATER_BOWL);
						
						if (!player.inventory.addItemStackToInventory(newStack))
						{
							player.dropPlayerItemWithRandomChoice(newStack, false);
						}
					}
					
					event.setUseItem(Result.DENY);
					
					if (world.isRemote)	// We must send a packet to the server telling it that the player right clicked or else it won't fill the cauldron server side.
					{
						Minecraft mc = GenesisClient.getMC();
						EntityPlayerSP spPlayer = mc.thePlayer;
						
						if (spPlayer == player)
						{
							Vec3d hitVec = mc.objectMouseOver.hitVec;
							hitVec = hitVec.subtract(pos.getX(), pos.getY(), pos.getZ());
							Packet<?> packet = 
									new CPacketPlayerBlockPlacement(hand);
							spPlayer.sendQueue.addToSendQueue(packet);
							
							//spPlayer.swingItem();	TODO: ???
							event.setCanceled(true);
						}
					}
				}
			}
			
			break;
		case RIGHT_CLICK_AIR:
			break;
		case LEFT_CLICK_BLOCK:
			break;
		}*/
	}
}
