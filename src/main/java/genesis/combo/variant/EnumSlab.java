package genesis.combo.variant;

import com.google.common.base.Supplier;
import genesis.common.GenesisBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public enum EnumSlab implements IMetadata<EnumSlab>
{
	LIMESTONE("limestone", Material.rock,
			() -> GenesisBlocks.limestone.getDefaultState());

	final String name;
	final String unlocalizedName;
	final Material material;
	final Supplier<IBlockState> model;

	EnumSlab(String name, String unlocalizedName, Material material, Supplier<IBlockState> model)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.material = material;
		this.model = model;
	}

	EnumSlab(String name, Material material, Supplier<IBlockState> model)
	{
		this(name, name, material, model);
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
