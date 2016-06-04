package genesis.block;

import genesis.combo.SlabBlocks;
import genesis.combo.SlabBlocks.SlabObjectType;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumSlab;
import genesis.combo.variant.PropertyIMetadata;
import genesis.util.BlockStateToMetadata;
import java.util.List;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static genesis.block.BlockGenesisSlab.EnumHalf.*;

public class BlockGenesisSlab extends BlockGenesis
{
	public static final PropertyEnum<EnumHalf> HALF = PropertyEnum.create("half", EnumHalf.class);

	public enum EnumHalf implements IStringSerializable
	{
		BOTTOM, TOP, BOTH;

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}

	@BlockProperties
	public static IProperty<?>[] properties = { HALF };

	protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
	protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0, 0.5, 0, 1, 1, 1);

	public final SlabBlocks owner;
	public final SlabObjectType type;

	public final PropertyIMetadata<EnumSlab> variantProp;
	public final List<EnumSlab> variants;

	public BlockGenesisSlab(SlabBlocks owner,
							SlabObjectType type,
							List<EnumSlab> variants, Class<EnumSlab> variantClass,
							Material material, SoundType sound)
	{
		super(material, sound);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		this.variantProp = new PropertyIMetadata<>("variant", variants, variantClass);

		blockState = new BlockStateContainer(this, variantProp, HALF);
		setDefaultState(blockState.getBaseState().withProperty(HALF, BOTTOM));
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
		return owner.getVariant(state).getModelState().getMapColor();
	}

	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return state.getValue(HALF) == BOTH;
	}

	@Override
	public boolean getUseNeighborBrightness(IBlockState state)
	{
		EnumHalf half = state.getValue(HALF);
		
		switch (half)
		{
		case BOTH:
			return false;
		case TOP:
		case BOTTOM:
			return true;
		}
		
		throw new IllegalArgumentException("Unknown half " + half);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		EnumHalf half = state.getValue(HALF);
		
		switch (half)
		{
		case BOTH:
			return FULL_BLOCK_AABB;
		case TOP:
			return AABB_TOP_HALF;
		case BOTTOM:
			return AABB_BOTTOM_HALF;
		}
		
		throw new IllegalArgumentException("Unknown half " + half);
	}

	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		EnumHalf half = state.getValue(HALF);
		
		switch (half)
		{
		case BOTH:
		case TOP:
			return true;
		case BOTTOM:
			return false;
		}
		
		throw new IllegalArgumentException("Unknown half " + half);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getValue(HALF) == BOTH;
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		EnumHalf half = state.getValue(HALF);
		
		switch (half)
		{
		case BOTH:
			return true;
		case TOP:
			return face == EnumFacing.UP;
		case BOTTOM:
			return face == EnumFacing.DOWN;
		}
		
		throw new IllegalArgumentException("Unknown half " + half);
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getStateFromMeta(meta);
		EnumSlab variant = owner.getVariant(state);

		if (state.getValue(HALF) == BOTH)
		{
			return owner.getBlockState(type, variant).withProperty(HALF, BOTTOM);
		}
		else if (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double) hitY <= 0.5D))
		{
			return owner.getBlockState(type, variant).withProperty(HALF, BOTTOM);
		}
		else
		{
			return owner.getBlockState(type, variant).withProperty(HALF, TOP);
		}
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		EnumHalf half = state.getValue(HALF);
		
		switch (half)
		{
		case BOTH:
			return 2;
		case TOP:
		case BOTTOM:
			return 1;
		}
		
		throw new IllegalArgumentException("Unknown half " + half);
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return state.getValue(HALF) == BOTH;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		boolean shouldSideBeRendered = super.shouldSideBeRendered(state, world, pos, side);

		if (state.getValue(HALF) == BOTH)
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
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, HALF);
	}
}
