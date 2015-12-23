package genesis.item;

import java.util.*;

import genesis.common.GenesisCreativeTabs;
import genesis.metadata.IFood;
import genesis.metadata.IMetadata;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.RandomReflection;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemGenesisFood<V extends IMetadata<V>> extends ItemFood
{
	public final VariantsOfTypesCombo<V> owner;
	
	protected final List<V> variants;
	protected final ObjectType<Block, ? extends ItemGenesisFood<V>> type;
	
	public ItemGenesisFood(VariantsOfTypesCombo<V> owner, ObjectType<Block, ? extends ItemGenesisFood<V>> type, List<V> variants, Class<V> variantClass)
	{
		super(0, 0, false);
		
		this.owner = owner;
		this.type = type;
		this.variants = variants;
		
		setCreativeTab(GenesisCreativeTabs.FOOD);
		setHasSubtypes(true);
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
	
	protected IFood getFoodType(ItemStack stack)
	{
		return (IFood) owner.getVariant(stack);
	}
	
	@Override
	public int getHealAmount(ItemStack stack)
	{
		return getFoodType(stack).getFoodAmount();
	}
	
	@Override
	public float getSaturationModifier(ItemStack stack)
	{
		return getFoodType(stack).getSaturationModifier();
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		boolean positive = false;
		
		for (PotionEffect effect : getFoodType(stack).getEffects())
		{
			if (!RandomReflection.isBadPotionEffect(effect))
			{
				positive = true;
				break;
			}
		}
		
		if (player.canEat(positive))
		{
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}
		
		return stack;
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			for (PotionEffect effect : ((IFood) owner.getVariant(stack)).getEffects())
			{
				player.addPotionEffect(new PotionEffect(effect));
			}
		}
	}
}
