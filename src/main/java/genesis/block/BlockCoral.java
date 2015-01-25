package genesis.block;

import genesis.client.GenesisSounds;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.EnumCoral;
import genesis.util.Constants;
import genesis.util.Metadata;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockCoral extends BlockGenesis
{
	public BlockCoral()
	{
		super(Material.coral);
		setHardness(0.75F);
		setResistance(8.5F);
		setStepSound(GenesisSounds.CORAL);
		setDefaultState(getBlockState().getBaseState().withProperty(Constants.CORAL_VARIANT, EnumCoral.FAVOSITES));
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return Metadata.getMetadata(state, Constants.CORAL_VARIANT);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		Metadata.getSubBlocks(EnumCoral.class, list);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return Metadata.getState(this, Constants.CORAL_VARIANT, EnumCoral.class, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return Metadata.getMetadata(state, Constants.CORAL_VARIANT);
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, Constants.CORAL_VARIANT);
	}
}
