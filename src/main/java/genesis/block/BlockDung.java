package genesis.block;

import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumDung;
import genesis.common.GenesisSounds;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
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
			BBS[i] = new AxisAlignedBB(0, 0, 0, 0, (i + 1) / 8D, 0);
		}
	}
	
	public BlockDung(VariantsOfTypesCombo<EnumDung> owner, ObjectType<? extends BlockGenesisVariants<EnumDung>, ? extends Item> type, List<EnumDung> variants, Class<EnumDung> variantClass)
	{
		super(owner, type, variants, variantClass, Material.ground, GenesisSounds.DUNG);
		
		blockState = new BlockStateContainer(this, variantProp, HEIGHT);
		setDefaultState(blockState.getBaseState().withProperty(HEIGHT, 8));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BBS[state.getValue(HEIGHT) - MIN_HEIGHT];
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos,
			AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		int index = state.getValue(HEIGHT) - MIN_HEIGHT - 1;
		
		if (index >= 0)
			addCollisionBoxToList(pos, mask, list, BBS[index]);
	}
}
