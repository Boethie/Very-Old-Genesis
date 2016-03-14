package genesis.combo.variant;

import com.google.common.base.Supplier;

import genesis.common.GenesisItems;
import net.minecraft.item.ItemStack;

public enum EnumDebrisOther implements IMetadata<EnumDebrisOther>
{
	CALAMITES("calamites", null),
	COELOPHYSIS_FEATHER("coelophysis_feather", "coelophysisFeather",
			() -> GenesisItems.materials.getStack(EnumMaterial.COELOPHYSIS_FEATHER)),
	EPIDEXIPTERYX_FEATHER("epidexipteryx_feather", "epidexipteryxFeather",
			() -> GenesisItems.materials.getStack(EnumMaterial.EPIDEXIPTERYX_FEATHER)),
	TYRANNOSAURUS_FEATHER("tyrannosaurus_feather", "tyrannosaurusFeather",
			() -> GenesisItems.materials.getStack(EnumMaterial.TYRANNOSAURUS_FEATHER));
	
	final String name;
	final String unlocalizedName;
	final Supplier<ItemStack> drop;
	
	EnumDebrisOther(String name, String unlocalizedName, Supplier<ItemStack> drop)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.drop = drop;
	}
	
	EnumDebrisOther(String name, Supplier<ItemStack> drop)
	{
		this(name, name, drop);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	public ItemStack getDrop(ItemStack original)
	{
		if (drop == null)
			return original;
		
		return drop.get();
	}
}
