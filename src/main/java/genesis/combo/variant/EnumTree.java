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
	GINKGO("ginkgo", props().noDead().fruit(1, 1)),
	BJUVIA("bjuvia", props().noBillet().noDead().noDebris()),
	VOLTZIA("voltzia", props().noDead().noDebris()),
	ARAUCARIOXYLON("araucarioxylon", props().fruit(1, 1)),
	METASEQUOIA("metasequoia", props()),
	ARCHAEANTHUS("archaeanthus", props().noDead()),
	DRYOPHYLLUM("dryophyllum", props()),
	FICUS("ficus", props().noDead().fruit(1, 1));
	
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
			if (tree.noBillet)
				noBillet.add(tree);
			
			if (tree.noDead)
				noDead.add(tree);
			
			if (tree.noDebris)
				noDebris.add(tree);
			
			if (tree.food > 0)
				fruit.add(tree);
		}

		NO_BILLET = noBillet.build();
		NO_DEAD = noDead.build();
		NO_DEBRIS = noDebris.build();
		FRUIT = fruit.build();
	}
	
	final String name;
	final String unlocalizedName;
	
	final boolean noBillet;
	final boolean noDebris;
	final boolean noDead;
	
	final int food;
	final float saturation;
	final List<PotionEffect> effects;
	
	EnumTree(String name, String unlocalizedName, Props props)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		
		noBillet = props.noBillet;
		noDebris = props.noDebris;
		noDead = props.noDead;
		
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
		boolean noBillet = false;
		boolean noDebris = false;
		boolean noDead = false;
		
		int food = 0;
		float saturation = 0;
		List<PotionEffect> effects = Collections.emptyList();
		
		private Props()
		{
		}
		
		private Props noBillet()
		{
			noBillet = true;
			return this;
		}
		
		private Props noDebris()
		{
			noDebris = true;
			return this;
		}
		
		private Props noDead()
		{
			noDead = true;
			return this;
		}
		
		private Props fruit(int food, float saturation, PotionEffect... effects)
		{
			this.food = food;
			this.saturation = saturation;
			this.effects = ImmutableList.copyOf(effects);
			return this;
		}
	}
}
