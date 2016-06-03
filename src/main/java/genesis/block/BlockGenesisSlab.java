package genesis.block;

import genesis.combo.SlabBlocks;
import genesis.combo.SlabBlocks.SlabObjectType;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumSlab;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.util.BlockStateToMetadata;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGenesisSlab extends BlockSlab
{
	public static final PropertyEnum<EnumHalf> HALF = PropertyEnum.create("half", EnumHalf.class);

	public enum EnumHalf implements IStringSerializable
	{
		BOTH, TOP, BOTTOM;

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}

		public boolean isDouble()
		{
			return this == BOTH;
		}

		public boolean isSingle()
		{
			return isTop() || isBottom();
		}

		public boolean isTop()
		{
			return this == TOP;
		}

		public boolean isBottom()
		{
			return this == BOTTOM;
		}
	}

	@BlockProperties
	public static IProperty<?>[] properties = { HALF };

	public final SlabBlocks owner;
	public final SlabObjectType type;

	public final PropertyIMetadata<EnumSlab> variantProp;
	public final List<EnumSlab> variants;

	public BlockGenesisSlab(SlabBlocks owner,
							SlabObjectType type,
							List<EnumSlab> variants, Class<EnumSlab> variantClass,
							Material material, SoundType sound)
	{
		super(material);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		this.variantProp = new PropertyIMetadata<EnumSlab>("variant", variants, variantClass);

		blockState = new BlockStateContainer(this, variantProp, HALF);
		setDefaultState(blockState.getBaseState().withProperty(HALF, EnumHalf.BOTTOM));

		setSoundType(sound);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, variantProp, HALF);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, variantProp, HALF);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, owner.getVariant(state));
	}

	@Override
	public MapColor getMapColor(IBlockState state)
	{
		return owner.getVariant(state).getBaseState().getMapColor();
	}

	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return state.getValue(HALF).isDouble();
	}

	@Override
	public boolean getUseNeighborBrightness(IBlockState state)
	{
		return state.getValue(HALF).isSingle();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		EnumHalf half = state.getValue(HALF);
		return half.isDouble() ? FULL_BLOCK_AABB : half.isTop() ? AABB_TOP_HALF : AABB_BOTTOM_HALF;
	}

	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		EnumHalf half = state.getValue(HALF);
		return half.isDouble() || half.isTop();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getValue(HALF).isDouble();
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		EnumHalf half = state.getValue(HALF);
		return state.isOpaqueCube()
				|| (half.isTop() && face == EnumFacing.UP)
				|| (half.isBottom() && face == EnumFacing.DOWN);
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getStateFromMeta(meta);
		EnumSlab variant = owner.getVariant(state);

		if (state.getValue(HALF).isDouble())
		{
			return owner.getBottomSlabState(type, variant);
		}
		else if (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double) hitY <= 0.5D))
		{
			return owner.getBottomSlabState(type, variant);
		}
		else
		{
			return owner.getTopSlabState(type, variant);
		}
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return state.getValue(HALF).isDouble() ? 2 : 1;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return state.getValue(HALF).isDouble();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		boolean shouldSideBeRendered = super.shouldSideBeRendered(state, world, pos, side);

		if (state.getValue(HALF).isDouble())
		{
			return shouldSideBeRendered;
		}
		else if (side != EnumFacing.UP && side != EnumFacing.DOWN && !shouldSideBeRendered)
		{
			return false;
		}
		else
		{
			return shouldSideBeRendered;
		}
	}

	@Override
	public boolean isDouble()
	{
		// must be true to call super.super.shouldSideBeRendered(...)
		return true;
	}

	@Override
	public String getUnlocalizedName(int meta)
	{
		return owner.getUnlocalizedName(owner.getStack(type, owner.getVariant(this, meta)), getUnlocalizedName());
	}

	@Override
	public PropertyIMetadata<EnumSlab> getVariantProperty()
	{
		return variantProp;
	}

	@Override
	public EnumSlab getTypeForItem(ItemStack stack)
	{
		return owner.getVariant(stack);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, HALF);
	}
}
