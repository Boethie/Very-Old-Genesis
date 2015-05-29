package genesis.block;

import genesis.metadata.EnumTree;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

import java.util.*;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockGenesisRottenLogs extends BlockGenesisLogs
{
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return BlockGenesisLogs.getProperties();
	}
	
	public BlockGenesisRottenLogs(List<EnumTree> variants, VariantsOfTypesCombo owner, ObjectType type)
	{
		super(variants, owner, type);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}
}
