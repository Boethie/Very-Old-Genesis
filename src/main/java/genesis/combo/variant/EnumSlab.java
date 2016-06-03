package genesis.combo.variant;

import com.google.common.base.Supplier;
import genesis.common.GenesisBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public enum EnumSlab implements IMetadata<EnumSlab>
{
	LIMESTONE("limestone", () -> GenesisBlocks.limestone.getDefaultState());

	final String name;
	final String unlocalizedName;
	final Supplier<IBlockState> base;

	EnumSlab(String name, String unlocalizedName, Supplier<IBlockState> base)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.base = base;
	}

	EnumSlab(String name, Supplier<IBlockState> base)
	{
		this(name, name, base);
	}

	public IBlockState getBaseState()
	{
		return base.get();
	}

	public Material getMaterial()
	{
		return getBaseState().getMaterial();
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
