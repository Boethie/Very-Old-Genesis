package genesis.item;

import java.util.List;

import genesis.combo.*;
import genesis.combo.variant.IBowMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.util.BitMask;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemBowMulti<V extends IBowMetadata<V>> extends ItemBow implements IItemMetadataBitMask
{
	public static final ResourceLocation PULL = new ResourceLocation("pull");
	
	protected final VariantsOfTypesCombo<V> owner;
	protected final ObjectType<V, ?, ? extends ItemBowMulti<V>> type;
	
	protected final List<V> variants;
	protected final Class<V> variantClass;
	
	private final BitMask damageMask;
	private final BitMask variantMask;
	
	public ItemBowMulti(VariantsOfTypesCombo<V> owner,
			ObjectType<V, ?, ? extends ItemBowMulti<V>> type,
			List<V> variants, Class<V> variantClass)
	{
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		this.variantClass = variantClass;
		
		int max = 0;
		
		for (V variant : variants)
			max = Math.max(max, variant.getDurability());
		
		setMaxDamage(max);
		damageMask = BitMask.forValueCount(max + 1);
		variantMask = BitMask.forValueCount(variants.size()).shiftTo(Short.SIZE - 1);
		
		addPropertyOverride(PULL,
				(s, w, e) -> e == null ? 0 : (s.getMaxItemUseDuration() - e.getItemInUseCount()) / (float) owner.getVariant(s).getDraw());
		
		setHasSubtypes(true);
		
		setCreativeTab(GenesisCreativeTabs.COMBAT);
	}
	
	@Override
	public BitMask getMetadataBitMask()
	{
		return variantMask;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return owner.getVariant(stack).getDurability();
	}
	
	@Override
	public int getDamage(ItemStack stack)
	{
		return damageMask.decode(stack.getMetadata());
	}
	
	@Override
	public boolean isDamaged(ItemStack stack)
	{
		return stack.getItemDamage() > 0;
	}
	
	@Override
	public void setDamage(ItemStack stack, int value)
	{
		super.setDamage(stack, damageMask.encode(stack.getMetadata(), value));
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
	
	private ItemStack findAmmo(EntityPlayer player)
	{
		if (isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
			return player.getHeldItem(EnumHand.OFF_HAND);
		
		if (isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
			return player.getHeldItem(EnumHand.MAIN_HAND);
		
		for (int i = 0; i < player.inventory.getSizeInventory(); i++)
		{
			ItemStack stack = player.inventory.getStackInSlot(i);
			
			if (isArrow(stack))
				return stack;
		}
		
		return null;
	}
	
	public float getArrowVelocity(ItemStack stack, int useTime)
	{
		float vel = useTime / (float) owner.getVariant(stack).getDraw();
		vel = (vel * vel + vel * 2) / 3;
		return Math.min(vel, 1);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft)
	{
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			V variant = owner.getVariant(stack);
			
			boolean infinite = player.capabilities.isCreativeMode
					|| EnchantmentHelper.getEnchantmentLevel(Enchantments.infinity, stack) > 0;
			ItemStack arrowStack = findAmmo(player);
			
			boolean canFire = arrowStack != null || infinite;
			
			int useTime = getMaxItemUseDuration(stack) - timeLeft;
			useTime = ForgeEventFactory.onArrowLoose(stack, world, player, useTime, canFire);
			
			if (useTime < 0)
				return;
			
			if (canFire)
			{
				if (arrowStack == null)
					arrowStack = new ItemStack(Items.arrow);
				
				float velocity = getArrowVelocity(stack, useTime);
				
				if (velocity >= 0.1F)
				{
					if (!world.isRemote)
					{
						ItemArrow arrowItem = (ItemArrow) (arrowStack.getItem() instanceof ItemArrow ? arrowStack.getItem() : Items.arrow);
						EntityArrow arrowEntity = arrowItem.createArrow(world, arrowStack, player);
						// MCP name: setAim
						arrowEntity.func_184547_a(player,
								player.rotationPitch, player.rotationYaw,
								0, velocity * 3 * variant.getVelocity(), variant.getSpread());
						
						if (velocity >= 1)
							arrowEntity.setIsCritical(true);
						
						int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.power, stack);
						
						if (power > 0)
							arrowEntity.setDamage(arrowEntity.getDamage() * variant.getDamage() + power * 0.5 + 0.5);
						
						int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.punch, stack);
						
						if (punch > 0)
							arrowEntity.setKnockbackStrength(punch);
						
						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.flame, stack) > 0)
							arrowEntity.setFire(100);
						
						stack.damageItem(1, player);
						
						if (infinite)
							arrowEntity.canBePickedUp = EntityArrow.PickupStatus.CREATIVE_ONLY;
						
						world.spawnEntityInWorld(arrowEntity);
					}
					
					world.playSound(null,
							player.posX, player.posY, player.posZ,
							SoundEvents.entity_arrow_shoot, SoundCategory.NEUTRAL,
							1, 1 / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
					
					if (!infinite)
					{
						arrowStack.stackSize--;
						
						if (arrowStack.stackSize == 0)
							player.inventory.deleteStack(arrowStack);
					}
					
					player.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}
}
