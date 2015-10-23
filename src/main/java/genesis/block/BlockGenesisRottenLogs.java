package genesis.block;

import genesis.common.GenesisSounds;
import genesis.metadata.EnumTree;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

import java.util.*;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGenesisRottenLogs extends BlockGenesisLogs implements IGenesisMushroomBase
{
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return BlockGenesisLogs.getProperties();
	}
	
	public BlockGenesisRottenLogs(List<EnumTree> variants, VariantsOfTypesCombo owner, ObjectType type)
	{
		super(variants, owner, type);
		setStepSound(GenesisSounds.ROTTEN_LOG);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}
	
	@Override
	public boolean canSustainMushroom(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return true;
	}
}
