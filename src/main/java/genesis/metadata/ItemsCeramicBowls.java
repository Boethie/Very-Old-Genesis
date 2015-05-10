package genesis.metadata;

import java.util.*;

import net.minecraft.item.ItemStack;
import genesis.item.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType.ObjectNamePosition;

public class ItemsCeramicBowls extends VariantsOfTypesCombo
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
	
	public static final ObjectType<ItemMulti> main = new ObjectType("ceramic_bowl", "ceramicBowl", null, null, EnumDye.valueList())
			.setNamePosition(ObjectNamePosition.PREFIX);
	public static final ObjectType<ItemMulti> dyes = new ObjectType("dye", null, null)
			.setValidVariants(EnumDye.valueList())
			.setNamePosition(ObjectNamePosition.PREFIX);
	
	public static final List<ObjectType> allObjectTypes = new ArrayList<ObjectType>()
	{{
		add(main);
		add(dyes);
	}};
	public static final List<IMetadata> allVariants = new ArrayList()
	{{
		addAll(Arrays.asList(EnumCeramicBowls.values()));
		addAll(EnumDye.valueList());
	}};
	
	public ItemsCeramicBowls()
	{
		super(allObjectTypes, allVariants);
	}
	
	public ItemStack getStack(EnumCeramicBowls bowlVariant)
	{
		return super.getStack(main, bowlVariant);
	}
	
	public ItemStack getStack(EnumDye dyeVariant)
	{
		return super.getStack(dyes, dyeVariant);
	}
}
