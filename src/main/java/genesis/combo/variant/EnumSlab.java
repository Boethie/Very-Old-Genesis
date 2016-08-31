package genesis.combo.variant;

import genesis.common.GenesisBlocks;
import java.util.function.Supplier;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public enum EnumSlab implements IMetadata<EnumSlab>
{
	GRANITE_RUBBLE("granite_rubble", "rubble.granite", Material.ROCK,
			() -> GenesisBlocks.rubble.getBlockState(EnumRubble.GRANITE),
			() -> GenesisBlocks.rubble.getStack(EnumRubble.GRANITE)),
	MOSSY_GRANITE_RUBBLE("mossy_granite_rubble", "rubble.mossyGranite", Material.ROCK,
			() -> GenesisBlocks.rubble.getBlockState(EnumRubble.MOSSY_GRANITE),
			() -> GenesisBlocks.rubble.getStack(EnumRubble.MOSSY_GRANITE)),
	RHYOLITE_RUBBLE("rhyolite_rubble", "rubble.rhyolite", Material.ROCK,
			() -> GenesisBlocks.rubble.getBlockState(EnumRubble.RHYOLITE),
			() -> GenesisBlocks.rubble.getStack(EnumRubble.RHYOLITE)),
	DOLERITE_RUBBLE("dolerite_rubble", "rubble.dolerite", Material.ROCK,
			() -> GenesisBlocks.rubble.getBlockState(EnumRubble.DOLERITE),
			() -> GenesisBlocks.rubble.getStack(EnumRubble.DOLERITE)),
	SMOOTH_LIMESTONE("smooth_limestone", "smoothLimestone", Material.ROCK,
			() -> GenesisBlocks.smooth_limestone.getDefaultState(),
			() -> new ItemStack(GenesisBlocks.smooth_limestone));

	final String name;
	final String unlocalizedName;
	final Material material;
	final Supplier<IBlockState> model;
	final Supplier<ItemStack> modelStack;

	EnumSlab(String name, String unlocalizedName, Material material, Supplier<IBlockState> model, Supplier<ItemStack> modelStack)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.material = material;
		this.model = model;
		this.modelStack = modelStack;
	}

	EnumSlab(String name, Material material, Supplier<IBlockState> model, Supplier<ItemStack> modelStack)
	{
		this(name, name, material, model, modelStack);
	}

	public ItemStack getModelStack()
	{
		return modelStack.get();
	}

	public IBlockState getModelState()
	{
		return model.get();
	}

	public Material getMaterial()
	{
		return material;
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
