package genesis.combo.variant;

import genesis.common.GenesisItems;
import java.util.function.Supplier;
import net.minecraft.item.ItemStack;

public enum EnumDebrisOther implements IMetadata<EnumDebrisOther>
{
	CALAMITES("calamites", null),
	COELOPHYSIS_FEATHER("coelophysis_feather", "coelophysisFeather",
			() -> GenesisItems.MATERIALS.getStack(EnumMaterial.COELOPHYSIS_FEATHER)),
	EPIDEXIPTERYX_FEATHER("epidexipteryx_feather", "epidexipteryxFeather",
			() -> GenesisItems.MATERIALS.getStack(EnumMaterial.EPIDEXIPTERYX_FEATHER)),
	TYRANNOSAURUS_FEATHER("tyrannosaurus_feather", "tyrannosaurusFeather",
			() -> GenesisItems.MATERIALS.getStack(EnumMaterial.TYRANNOSAURUS_FEATHER));
	
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
