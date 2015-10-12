package genesis.item;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import genesis.common.GenesisCreativeTabs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemGenesisFood extends ItemFood
{
	private final List<Pair<Float, PotionEffect>> effects = new ArrayList<Pair<Float, PotionEffect>>();
	
	public ItemGenesisFood(int hunger, float saturation)
	{
		this(hunger, saturation, false);
	}
	
	public ItemGenesisFood(int hunger, float saturation, boolean isWolfsFavoriteMeat)
	{
		super(hunger, saturation, isWolfsFavoriteMeat);
		
		setCreativeTab(GenesisCreativeTabs.FOOD);
	}
	
	/**
	 * Adds a potion effect to the food. Can have multiple potion effects per food
	 *
	 * @param id
	 *            The potion id
	 * @param duration
	 *            Duration in seconds (will be multiplied by 20)
	 * @param amplifier
	 *            Amplifier of the potion
	 * @param chance
	 *            Chance of the potion effect happening ranging from 0.0 to 1.0
	 */
	@Override
	public ItemGenesisFood setPotionEffect(int id, int duration, int amplifier, float chance)
	{
		effects.add(Pair.of(chance, new PotionEffect(id, duration * 20, amplifier)));
		
		return this;
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			for (Pair<Float, PotionEffect> entry : effects)
			{
				if ((entry.getValue().getPotionID() > 0) && (world.rand.nextFloat() < entry.getKey()))
				{
					// Defensive copying
					PotionEffect effect = entry.getValue();
					player.addPotionEffect(new PotionEffect(effect.getPotionID(), effect.getDuration(), effect.getAmplifier()));
				}
			}
		}
	}
}
