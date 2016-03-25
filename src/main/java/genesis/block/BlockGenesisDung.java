package genesis.block;

import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumDung;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockGenesisDung extends BlockGenesisVariants<EnumDung>
{
	public static final PropertyInteger HEIGHT = PropertyInteger.create("height", 1, 8);
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{ HEIGHT };
	}
	
	public BlockGenesisDung(VariantsOfTypesCombo<EnumDung> owner, ObjectType<? extends BlockGenesisVariants<EnumDung>, ? extends Item> type, List<EnumDung> variants, Class<EnumDung> variantClass, Material material)
	{
		super(owner, type, variants, variantClass, material);
		blockState = new BlockState(this, variantProp, HEIGHT);
		setDefaultState(blockState.getBaseState().withProperty(HEIGHT, 8));
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return new AxisAlignedBB(
				pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
				pos.getX() + this.maxX, pos.getY() + (state.getValue(HEIGHT) - 1)/8D, pos.getZ() + this.maxZ);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos);
		return new AxisAlignedBB(
				pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
				pos.getX() + this.maxX, pos.getY() + state.getValue(HEIGHT)/8D, pos.getZ() + this.maxZ);
	}
}
