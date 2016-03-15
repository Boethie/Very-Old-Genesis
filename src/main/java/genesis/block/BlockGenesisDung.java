package genesis.block;

import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumDung;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockGenesisDung extends BlockGenesisVariants<EnumDung>
{
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public BlockGenesisDung(VariantsOfTypesCombo<EnumDung> owner, VariantsOfTypesCombo.ObjectType<? extends BlockGenesisVariants<EnumDung>, ? extends Item> type, List<EnumDung> variants, Class<EnumDung> variantClass, Material material)
	{
		super(owner, type, variants, variantClass, material);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY - 1/8D, (double)pos.getZ() + this.maxZ);
	}
}
