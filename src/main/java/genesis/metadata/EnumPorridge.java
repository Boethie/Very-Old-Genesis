package genesis.metadata;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import net.minecraft.item.ItemStack;

public enum EnumPorridge implements IMetadata
{
	BASE("base", "", 4, 4.8F),
	ARAUCARIOXYLON("araucarioxylon", 5, 5.7F)
	{
		@Override public ItemStack getIngredient()
		{
			return new ItemStack(GenesisItems.araucarioxylon_seeds);
		}
	},
	ZINGIBEROPSIS("zingiberopsis", 6, 6.6F)
	{
		@Override public ItemStack getIngredient()
		{
			return new ItemStack(GenesisItems.zingiberopsis_rhizome);
		}
	},
	ODONTOPTERIS("odontopteris", 5, 6F)
	{
		@Override public ItemStack getIngredient()
		{
			return new ItemStack(GenesisItems.odontopteris_seeds);
		}
	},
	ARCHAEOMARASMIUS("archaeomarasmius", 5, 6.2F)
	{
		@Override public ItemStack getIngredient()
		{
			return new ItemStack(GenesisBlocks.archaeomarasmius);
		}
	};
	
	final String name;
	final String unlocalizedName;
	final int food;
	final float saturation;
	
	EnumPorridge(String name, String unlocalizedName, int food, float saturation)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.food = food;
		this.saturation = saturation;
	}
	
	EnumPorridge(String name, int food, float saturation)
	{
		this(name, name, food, saturation);
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
	
	public ItemStack getIngredient()
	{
		return null;
	}
	
	public int getFoodAmount()
	{
		return food;
	}
	
	public float getSaturationModifier()
	{
		return saturation;
	}
}
