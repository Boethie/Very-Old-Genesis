package genesis.block;

import genesis.client.GenesisSounds;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.EnumCoral;
import genesis.util.Constants;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;

public class BlockCoral extends BlockMetadata
{
	public BlockCoral()
	{
		super(Material.coral);
		setHardness(0.75F);
		setResistance(8.5F);
		setStepSound(GenesisSounds.CORAL);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}

	@Override
	protected IProperty getVariant()
	{
		return Constants.CORAL_VARIANT;
	}

	@Override
	protected Class getMetaClass()
	{
		return EnumCoral.class;
	}
}
