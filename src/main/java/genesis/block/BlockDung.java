package genesis.block;

import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumDung;
import genesis.sounds.GenesisSoundTypes;
import genesis.util.BlockStateToMetadata;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockDung extends BlockGenesisVariants<EnumDung>
{
	public static final int MIN_HEIGHT = 1;
	public static final int MAX_HEIGHT = 8;
	public static final PropertyInteger HEIGHT = PropertyInteger.create("height", MIN_HEIGHT, MAX_HEIGHT);
	
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{ HEIGHT };
	}
	
	private static final AxisAlignedBB[] BBS = new AxisAlignedBB[HEIGHT.getAllowedValues().size()];
	
	static
	{
		for (int i = 0; i < BBS.length; i++)
		{
			BBS[i] = new AxisAlignedBB(0, 0, 0, 1, (i + 1) / 8D, 1);
		}
	}
	
	private boolean ready = false;
	
	public BlockDung(VariantsOfTypesCombo<EnumDung> owner, ObjectType<? extends BlockGenesisVariants<EnumDung>, ? extends Item> type, List<EnumDung> variants, Class<EnumDung> variantClass)
	{
		super(owner, type, variants, variantClass, Material.ground, GenesisSoundTypes.DUNG);
		
		blockState = new BlockStateContainer(this, variantProp, HEIGHT);
		setDefaultState(blockState.getBaseState().withProperty(HEIGHT, 8));
		
		ready = true;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BBS[state.getValue(HEIGHT) - MIN_HEIGHT];
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		int index = state.getValue(HEIGHT) - MIN_HEIGHT - 1;
		
		if (index >= 0)
			return BBS[index];
		
		return null;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, HEIGHT, variantProp);
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata, HEIGHT, variantProp);
	}
	
	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(HEIGHT) <= (MIN_HEIGHT + MAX_HEIGHT) / 2;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		if (ready)
			return state.getValue(HEIGHT) >= MAX_HEIGHT;
		
		return false;
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return isFullCube(state);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return isFullCube(state);
	}
}
