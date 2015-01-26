package genesis.block;

import genesis.client.GenesisSounds;
import genesis.common.GenesisItems;
import genesis.metadata.EnumDung;
import genesis.util.Constants;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;

public class BlockDung extends BlockMetadata
{
	public BlockDung()
	{
		super(Material.ground);
		setHardness(0.5F);
		setStepSound(GenesisSounds.DUNG);
		setItemDropped(GenesisItems.dung);
		setQuantityDropped(4);
		Blocks.fire.setFireInfo(this, 5, 5);
	}

	@Override
	protected IProperty getVariant()
	{
		return Constants.DUNG_VARIANT;
	}

	@Override
	protected Class getMetaClass()
	{
		return EnumDung.class;
	}
}
