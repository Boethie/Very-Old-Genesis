package genesis.block;

import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumTree;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.item.ItemBlockMulti;

import java.util.*;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGenesisDeadLogs extends BlockGenesisLogs implements IGenesisMushroomBase
{
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return BlockGenesisLogs.getProperties();
	}
	
	public BlockGenesisDeadLogs(VariantsOfTypesCombo<EnumTree> owner, ObjectType<? extends BlockGenesisDeadLogs, ? extends ItemBlockMulti<EnumTree>> type, List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super(owner, type, variants, variantClass);
		
		setSoundType(GenesisSoundTypes.DEAD_LOG);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}
	
	@Override
	public boolean canSustainMushroom(IBlockAccess world, BlockPos pos, EnumFacing side, IBlockState mushroomState)
	{
		return true;
	}
}
