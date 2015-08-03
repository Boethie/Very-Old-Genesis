package genesis.metadata;

import genesis.metadata.VariantsOfTypesCombo.*;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import genesis.item.*;

public class ItemsCeramicBowls extends VariantsOfTypesCombo<ObjectType, IMetadata>
{
	public static enum EnumCeramicBowls implements IMetadata
	{
		BOWL(""),
		WATER_BOWL("water");
		
		protected String name;
		protected String unlocalizedName;
		
		private EnumCeramicBowls(String name, String unlocalizedName)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		private EnumCeramicBowls(String name)
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
	}
	
	public static final ObjectType<Block, ItemCeramicBowl> MAIN = new ObjectType("ceramic_bowl", "ceramicBowl", null, ItemCeramicBowl.class, EnumDye.valueList())
			.setNamePosition(ObjectNamePosition.PREFIX);
	public static final ObjectType<Block, ItemMulti> DYES = new ObjectType("dye", null, null)
			.setValidVariants(EnumDye.valueList())
			.setNamePosition(ObjectNamePosition.PREFIX);
	
	public static final List<ObjectType> ALL_OBJECT_TYPES = new ArrayList<ObjectType>()
	{{
		add(MAIN);
		add(DYES);
	}};
	public static final List<IMetadata> ALL_VARIANTS = new ArrayList()
	{{
		addAll(Arrays.asList(EnumCeramicBowls.values()));
		addAll(EnumDye.valueList());
	}};
	
	public ItemsCeramicBowls()
	{
		super(ALL_OBJECT_TYPES, ALL_VARIANTS);
	}
	
	public ItemStack getStack(EnumCeramicBowls bowlVariant, int size)
	{
		return super.getStack(MAIN, bowlVariant, size);
	}
	
	public ItemStack getStack(EnumCeramicBowls bowlVariant)
	{
		return getStack(bowlVariant, 1);
	}
	
	public ItemStack getStack(EnumDye dyeVariant, int size)
	{
		return super.getStack(DYES, dyeVariant, size);
	}
	
	public ItemStack getStack(EnumDye dyeVariant)
	{
		return getStack(dyeVariant, 1);
	}

	public ItemStack getStack(EnumDyeColor color, int size)
	{
		return getStack(EnumDye.get(color), size);
	}

	public ItemStack getStack(EnumDyeColor color)
	{
		return getStack(color, 1);
	}
}
