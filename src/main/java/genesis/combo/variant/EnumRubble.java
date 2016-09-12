package genesis.combo.variant;

import genesis.common.GenesisBlocks;
import java.util.function.Supplier;
import net.minecraft.item.ItemStack;

public enum EnumRubble implements IMetadata<EnumRubble>
{
	GRANITE("granite", 2.6F, 10.0F,
			() -> new ItemStack(GenesisBlocks.GRANITE)),
	MOSSY_GRANITE("mossy_granite", "mossyGranite", 2.6F, 10.0F,
			() -> new ItemStack(GenesisBlocks.MOSSY_GRANITE)),
	RHYOLITE("rhyolite", 2.15F, 10.0F,
			() -> new ItemStack(GenesisBlocks.RHYOLITE)),
	DOLERITE("dolerite", 1.7F, 10.0F,
			() -> new ItemStack(GenesisBlocks.DOLERITE));

	final String name;
	final String unlocalizedName;
	final float hardness;
	final float resistance;
	final Supplier<ItemStack> modelStack;

	EnumRubble(String name, String unlocalizedName, float hardness, float resistance, Supplier<ItemStack> modelStack)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.hardness = hardness;
		this.resistance = resistance * 3.0F;
		this.modelStack = modelStack;
	}

	EnumRubble(String name, float hardness, float resistance, Supplier<ItemStack> modelStack)
	{
		this(name, name, hardness, resistance, modelStack);
	}

	public float getHardness()
	{
		return hardness;
	}

	public float getResistance()
	{
		return resistance;
	}
	
	public ItemStack getModelStack()
	{
		return modelStack.get();
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
}
