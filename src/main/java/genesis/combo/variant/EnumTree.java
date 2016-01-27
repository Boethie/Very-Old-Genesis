package genesis.combo.variant;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.potion.PotionEffect;

public enum EnumTree implements IMetadata<EnumTree>, IFood
{
	ARCHAEOPTERIS("archaeopteris", props()),
	SIGILLARIA("sigillaria", props()),
	LEPIDODENDRON("lepidodendron", props()),
	CORDAITES("cordaites", props().noDebris()),
	PSARONIUS("psaronius", props().noBillet().noDead().noDebris()),
	GINKGO("ginkgo", props().noDead().fruit(1, 0.4F)),
	BJUVIA("bjuvia", props().noBillet().noDead().noDebris()),
	VOLTZIA("voltzia", props().noDead().noDebris()),
	ARAUCARIOXYLON("araucarioxylon", props().hangingFruit()),
	METASEQUOIA("metasequoia", props()),
	ARCHAEANTHUS("archaeanthus", props().noDead()),
	DRYOPHYLLUM("dryophyllum", props()),
	FICUS("ficus", props().noDead().fruit(1, 1.2F));
	
	public static final Set<EnumTree> NO_BILLET;
	public static final Set<EnumTree> NO_DEAD;
	public static final Set<EnumTree> NO_DEBRIS;
	public static final Set<EnumTree> FRUIT;
	
	static
	{
		ImmutableSet.Builder<EnumTree> noBillet = ImmutableSet.builder();
		ImmutableSet.Builder<EnumTree> noDead = ImmutableSet.builder();
		ImmutableSet.Builder<EnumTree> noDebris = ImmutableSet.builder();
		ImmutableSet.Builder<EnumTree> fruit = ImmutableSet.builder();
		
		for (EnumTree tree : values())
		{
			if (!tree.hasBillet())
				noBillet.add(tree);
			
			if (!tree.hasDead())
				noDead.add(tree);
			
			if (!tree.hasDebris())
				noDebris.add(tree);
			
			if (tree.getFruitType() != FruitType.NONE)
				fruit.add(tree);
		}

		NO_BILLET = noBillet.build();
		NO_DEAD = noDead.build();
		NO_DEBRIS = noDebris.build();
		FRUIT = fruit.build();
	}
	
	public static enum FruitType
	{
		NONE, LEAVES, HANGING
	}
	
	final String name;
	final String unlocalizedName;
	
	final boolean billet;
	final boolean debris;
	final boolean dead;
	
	final FruitType fruit;
	final int food;
	final float saturation;
	final List<PotionEffect> effects;
	
	EnumTree(String name, String unlocalizedName, Props props)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		
		billet = props.billet;
		debris = props.debris;
		dead = props.dead;
		
		fruit = props.fruit;
		food = props.food;
		saturation = props.saturation;
		effects = props.effects;
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
	
	private static Props props()
	{
		return new Props();
	}
	
	private static final class Props
	{
		boolean billet = true;
		boolean debris = true;
		boolean dead = true;
		
		FruitType fruit = FruitType.NONE;
		int food = 0;
		float saturation = 0;
		List<PotionEffect> effects = Collections.emptyList();
		
		private Props()
		{
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
	}
}
