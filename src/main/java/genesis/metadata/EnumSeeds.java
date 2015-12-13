package genesis.metadata;

import genesis.common.GenesisBlocks;
import net.minecraft.block.state.IBlockState;

public enum EnumSeeds implements IMetadata<EnumSeeds>, IFood
{
	PROGRAMINIS_SEEDS("programinis_seeds", "programinis")
	{
		@Override
		public IBlockState getPlacedState()
		{
			return GenesisBlocks.programinis.getDefaultState();
		}
	},
	ARAUCARIOXYLON_SEEDS("araucarioxylon_seeds", "araucarioxylon", 1, 0.6F)
	{
		@Override
		public IBlockState getPlacedState()
		{
			return null;
		}
	},
	ODONTOPTERIS_SEEDS("odontopteris_seeds", "odontopteris", 1, 0.8F)
	{
		@Override
		public IBlockState getPlacedState()
		{
			return GenesisBlocks.odontopteris.getDefaultState();
		}
	},
	NEUROPTERIDIUM_RHIZOME("neuropteridium_rhizome", "neuropteridiumRhizome", 2, 1.4F)
	{
		@Override
		public IBlockState getPlacedState()
		{
			return GenesisBlocks.neuropteridium.getDefaultState();
		}
	},
	ZINGIBEROPSIS_RHIZOME("zingiberopsis_rhizome", "zingiberopsisRhizome", 2, 1.2F)
	{
		@Override
		public IBlockState getPlacedState()
		{
			return GenesisBlocks.zingiberopsis.getDefaultState();
		}
	};
	
	final String name;
	final String unlocalizedName;
	final int food;
	final float saturation;
	
	EnumSeeds(String name, String unlocalizedName, int food, float saturation)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.food = food;
		this.saturation = saturation;
	}
	
	EnumSeeds(String name, String unlocalizedName)
	{
		this(name, unlocalizedName, 0, 0);
	}
	
	private EnumSeeds(String name, int food, float saturation)
	{
		this(name, name, food, saturation);
	}
	
	private EnumSeeds(String name)
	{
		this(name, name);
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
	public String toString()
	{
		return name;
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
	
	public abstract IBlockState getPlacedState();
}
