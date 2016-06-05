package genesis.block;

import genesis.combo.ObjectType;
import genesis.combo.VariantsCombo;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumRubble;
import genesis.combo.variant.PropertyIMetadata;
import genesis.item.ItemBlockMulti;
import java.util.List;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockRubble extends BlockGenesisVariants<EnumRubble>
{
	@BlockProperties
	public static IProperty<?>[] properties = {};
	
	public final VariantsCombo<EnumRubble, BlockRubble, ItemBlockMulti<EnumRubble>> owner;
	public final ObjectType<EnumRubble, BlockRubble, ItemBlockMulti<EnumRubble>> type;
	
	public final List<EnumRubble> variants;
	public final PropertyIMetadata<EnumRubble> variantProp;
	
	public BlockRubble(VariantsCombo<EnumRubble, BlockRubble, ItemBlockMulti<EnumRubble>> owner,
			ObjectType<EnumRubble, BlockRubble, ItemBlockMulti<EnumRubble>> type,
			List<EnumRubble> variants, Class<EnumRubble> variantClass)
	{
		super(owner, type, variants, variantClass, Material.rock, SoundType.STONE);

		this.owner = owner;
		this.type = type;

		this.variants = variants;
		variantProp = new PropertyIMetadata<>("variant", variants, variantClass);

		blockState = new BlockStateContainer(this, variantProp);
		setDefaultState(getBlockState().getBaseState());

		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos)
	{
		return owner.getVariant(state).getHardness();
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return owner.getVariant(world.getBlockState(pos)).getResistance() / 5.0F;
	}
}
