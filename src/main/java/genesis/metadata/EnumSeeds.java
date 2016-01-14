package genesis.metadata;

import java.util.List;

import com.google.common.collect.ImmutableList;

import genesis.common.GenesisBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public enum EnumSeeds implements IMetadata<EnumSeeds>, IFood
{
	GINKGO_NUT("ginkgo_nut", "ginkgoNut", 1, 0.4F)
	{
		@Override
		public IBlockState getPlacedState()
		{
			return null;
		}
	},
	ARAUCARIOXYLON_SEEDS("araucarioxylon_seeds", "araucarioxylon", 1, 0.2F)
	{
		@Override
		public IBlockState getPlacedState()
		{
			return null;
		}
	},
	ODONTOPTERIS_SEEDS("odontopteris_seeds", "odontopteris", 1, 0.4F)
	{
		@Override
		public IBlockState getPlacedState()
		{
			return GenesisBlocks.odontopteris.getDefaultState();
		}
	},
	NEUROPTERIDIUM_RHIZOME("neuropteridium_rhizome", "neuropteridiumRhizome", 2, 1.2F)
	{
		@Override
		public IBlockState getPlacedState()
		{
			return GenesisBlocks.neuropteridium.getDefaultState();
		}
	},
	PROGRAMINIS_SEEDS("programinis_seeds", "programinis")
	{
		@Override
		public IBlockState getPlacedState()
		{
			return GenesisBlocks.programinis.getDefaultState();
		}
	},
	ZINGIBEROPSIS_RHIZOME("zingiberopsis_rhizome", "zingiberopsisRhizome", 2, 1.4F)
	{
		@Override
		public IBlockState getPlacedState()
		{
			return GenesisBlocks.zingiberopsis.getDefaultState();
		}
	},
	PROTOTAXITES_FLESH("prototaxites_flesh", "prototaxitesFlesh", 1, 0.8F,
			new PotionEffect(Potion.hunger.id, 300),
			new PotionEffect(Potion.confusion.id, 300),
			new PotionEffect(Potion.poison.id, 300))
	{
		@Override
		public IBlockState getPlacedState()
		{
			return GenesisBlocks.prototaxites.getDefaultState();
		}
	};
	
	final String name;
	final String unlocalizedName;
	final int food;
	final float saturation;
	final List<PotionEffect> effects;
	
	EnumSeeds(String name, String unlocalizedName, int food, float saturation, PotionEffect... effects)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.food = food;
		this.saturation = saturation;
		this.effects = ImmutableList.copyOf(effects);
	}
	
	EnumSeeds(String name, String unlocalizedName, PotionEffect... effects)
	{
		this(name, unlocalizedName, 0, 0, effects);
	}
	
	EnumSeeds(String name, int food, float saturation, PotionEffect... effects)
	{
		this(name, name, food, saturation, effects);
	}
	
	EnumSeeds(String name, PotionEffect... effects)
	{
		this(name, name, effects);
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
	
	public abstract IBlockState getPlacedState();
	
	@Override
	public String toString()
	{
		return name;
	}
}
