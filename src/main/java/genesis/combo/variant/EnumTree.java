package genesis.combo.variant;

import java.util.*;

import com.google.common.collect.ImmutableList;

import net.minecraft.potion.PotionEffect;

public enum EnumTree implements IMetadata<EnumTree>, IFood
{
	ARCHAEOPTERIS("archaeopteris", tree()),
	SIGILLARIA("sigillaria", tree()),
	LEPIDODENDRON("lepidodendron", tree()),
	CORDAITES("cordaites", tree()),
	PSARONIUS("psaronius", tree().noBillet().noDead()),
	GINKGO("ginkgo", tree().noDead().noDebris().fruit(1, 1)),
	BJUVIA("bjuvia", tree().noBillet().noDead().noDebris()),
	VOLTZIA("voltzia", tree().noDead().noDebris()),
	ARAUCARIOXYLON("araucarioxylon", tree().hangingFruit().fruitSize(6, 7)),
	METASEQUOIA("metasequoia", tree().bow(0.6F, 1)),
	ARCHAEANTHUS("archaeanthus", tree().noDead().bow(0.8F, 1.3F)),
	DRYOPHYLLUM("dryophyllum", tree().bow(1.2F, 1.8F)),
	FICUS("ficus", tree().noDead().noDebris().fruit(1, 1.2F)),
	LAUROPHYLLUM("laurophyllum", bush().noDead().noDebris().fruit(1, 0.4F).bow(0.7F, 1.1F));
	
	public static enum FruitType
	{
		NONE, LEAVES, HANGING
	}
	
	final String name;
	final String unlocalizedName;
	
	final boolean bush;
	final boolean billet;
	final boolean debris;
	final boolean dead;
	
	final FruitType fruit;
	
	final int food;
	final float saturation;
	final List<PotionEffect> effects;
	
	final float fruitWidth;
	final float fruitHeight;
	
	final float bowDurability;
	final float bowDraw;
	final float bowVelocity;
	final float bowDamage;
	final float bowSpread;
	
	EnumTree(String name, String unlocalizedName, Props props)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		
		bush = props.bush;
		billet = props.billet;
		debris = props.debris;
		dead = props.dead;
		
		fruit = props.fruit;
		food = props.food;
		saturation = props.saturation;
		effects = props.effects;
		
		fruitWidth = props.fruitWidth;
		fruitHeight = props.fruitHeight;
		
		bowDurability = props.bowDurability;
		bowDraw = props.bowDraw;
		bowVelocity = props.bowVelocity;
		bowDamage = props.bowDamage;
		bowSpread = props.bowSpread;
	}
	
	EnumTree(String name, Props props)
	{
		this(name, name, props);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public boolean isBush()
	{
		return bush;
	}
	
	public boolean hasBillet()
	{
		return billet;
	}
	
	public boolean hasDebris()
	{
		return debris;
	}
	
	public boolean hasDead()
	{
		return dead;
	}
	
	public FruitType getFruitType()
	{
		return fruit;
	}
	
	@Override
	public int getFoodAmount()
	{
		return food;
	}
	
	@Override
	public float getSaturationModifier()
	{
		return saturation;
	}
	
	@Override
	public Iterable<PotionEffect> getEffects()
	{
		return effects;
	}
	
	public float getFruitWidth()
	{
		return fruitWidth;
	}
	
	public float getFruitHeight()
	{
		return fruitHeight;
	}
	
	public float getBowDurability()
	{
		return bowDurability;
	}
	
	public boolean hasBow()
	{
		return getBowDurability() > 0;
	}
	
	public float getBowDraw()
	{
		return bowDraw;
	}
	
	public float getBowVelocity()
	{
		return bowVelocity;
	}
	
	public float getBowDamage()
	{
		return bowDamage;
	}
	
	public float getBowSpread()
	{
		return bowSpread;
	}
	
	private static Props tree()
	{
		return new Props();
	}
	
	private static Props bush()
	{
		return new Props().bush();
	}
	
	private static final class Props
	{
		boolean bush = false;
		boolean billet = true;
		boolean debris = true;
		boolean dead = true;
		
		FruitType fruit = FruitType.NONE;
		
		int food = 0;
		float saturation = 0;
		List<PotionEffect> effects = Collections.emptyList();
		
		float fruitWidth = 0.25F;
		float fruitHeight = 0.5F;
		
		float bowDurability;
		float bowDraw;
		float bowVelocity;
		float bowDamage;
		float bowSpread;
		
		private Props()
		{
		}
		
		private Props bush()
		{
			bush = true;
			return this;
		}
		
		private Props noBillet()
		{
			billet = false;
			return this;
		}
		
		private Props noDebris()
		{
			debris = false;
			return this;
		}
		
		private Props noDead()
		{
			dead = false;
			return this;
		}
		
		private Props fruit(FruitType fruit)
		{
			this.fruit = fruit;
			return this;
		}
		
		private Props fruitSize(float width, float height)
		{
			fruitWidth = width;
			fruitHeight = height;
			return this;
		}
		
		private Props fruitSize(int width, int height)
		{
			return fruitSize(width / 16F, height / 16F);
		}
		
		private Props hangingFruit()
		{
			return fruit(FruitType.HANGING);
		}
		
		private Props fruit(FruitType fruit, int food, float saturation, PotionEffect... effects)
		{
			fruit(fruit);
			this.food = food;
			this.saturation = saturation;
			this.effects = ImmutableList.copyOf(effects);
			return this;
		}
		
		private Props fruit(int food, float saturation, PotionEffect... effects)
		{
			return fruit(FruitType.LEAVES, food, saturation, effects);
		}
		
		private Props bow(float durability, float draw, float velocity, float damage, float spread)
		{
			this.bowDurability = durability;
			this.bowDraw = draw;
			this.bowVelocity = velocity;
			this.bowDamage = damage;
			this.bowSpread = spread;
			return this;
		}
		
		private Props bow(float durability, float draw)
		{
			return bow(durability, draw, 1, 1, 1);
		}
	}
}
