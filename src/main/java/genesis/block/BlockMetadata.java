package genesis.block;

import genesis.util.Metadata;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class BlockMetadata extends BlockGenesis
{
	public BlockMetadata(Material material)
	{
		super(material);
		setDefaultState(Metadata.getDefaultState(this, getVariant(), getMetaClass()));
	}

	@Override
	public BlockMetadata setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(unlocalizedName);
		
		return this;
	}

	protected abstract IProperty getVariant();

	protected abstract Class getMetaClass();

	@Override
	public int damageDropped(IBlockState state)
	{
		return Metadata.getMetadata(state, getVariant());
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		Metadata.getSubBlocks(getMetaClass(), list);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return Metadata.getState(this, getVariant(), getMetaClass(), meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return Metadata.getMetadata(state, getVariant());
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, getVariant());
	}
}
