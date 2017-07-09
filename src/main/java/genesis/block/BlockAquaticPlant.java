package genesis.block;

import java.util.*;

import com.google.common.collect.ImmutableSet;

import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumAquaticPlant;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.item.ItemBlockMulti;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;
import genesis.util.FlexibleStateMap;
import genesis.util.WorldUtils;
import genesis.util.blocks.IAquaticBlock;
import genesis.util.blocks.ISitOnBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAquaticPlant extends Block implements IModifyStateMap, IAquaticBlock, ISitOnBlock
{
	/**
	 * Used in VariantsOfTypesCombo.
	 */
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{};
	}

	public final VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>> owner;
	public final ObjectType<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>> type;

	public final List<EnumAquaticPlant> variants;
	public final PropertyIMetadata<EnumAquaticPlant> variantProp;

	protected final Set<Material> validGround = ImmutableSet.of(Material.GROUND, Material.SAND, Material.CLAY, Material.ROCK, Material.WOOD, Material.CORAL);
	protected final Set<EnumAquaticPlant> noDrops = ImmutableSet.of(EnumAquaticPlant.CHARNIA);

	public BlockAquaticPlant(VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>> owner,
			ObjectType<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>> type,
			List<EnumAquaticPlant> variants, Class<EnumAquaticPlant> variantClass)
	{
		super(Material.WATER);

		this.owner = owner;
		this.type = type;

		this.variants = variants;
		variantProp = new PropertyIMetadata<>("variant", variants, variantClass);

		blockState = new BlockStateContainer(this, variantProp, BlockLiquid.LEVEL);
		setDefaultState(getBlockState().getBaseState());

		setCreativeTab(GenesisCreativeTabs.DECORATIONS);

		setHardness(0.0F);
		setSoundType(GenesisSoundTypes.AQUATIC_PLANT);
		setTickRandomly(true);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, variantProp);
	}

	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata, variantProp);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void customizeStateMap(FlexibleStateMap stateMap)
	{
		stateMap.addIgnoredProperties(BlockLiquid.LEVEL);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(variants, list, noDrops);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		if (!noDrops.contains(state.getValue(variantProp)))
		{
			return super.getDrops(world, pos, state, fortune);
		}

		return Collections.emptyList();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(state.getValue(variantProp));
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		return null;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isReplaceable(IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if (state.getValue(variantProp) == EnumAquaticPlant.CHANCELLORIA)
		{
			entity.attackEntityFrom(Constants.CHANCELLORIA_DMG, 0.5F);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		this.checkAndDropBlock(world, pos, state);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		this.checkAndDropBlock(world, pos, state);
	}

	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!this.canBlockStay(world, pos, state))
		{
			WorldUtils.spawnBlockDrops(world, pos, state);
			this.breakPlant(world, pos, state);
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
	{
		this.breakPlant(world, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (state.getValue(variantProp) == EnumAquaticPlant.CHARNIA_TOP)
		{
			world.setBlockState(pos, getDefaultState().withProperty(variantProp, EnumAquaticPlant.CHARNIA), 3);
			world.setBlockState(pos.up(), getDefaultState().withProperty(variantProp, EnumAquaticPlant.CHARNIA_TOP), 3);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return this.canBlockStay(world, pos, this.getDefaultState());
	}

	private void breakPlant(World world, BlockPos pos, IBlockState state)
	{
		world.setBlockState(pos, getReplacement(world, pos, state), 3);

		EnumAquaticPlant variant = state.getValue(variantProp);

		if (variant == EnumAquaticPlant.CHARNIA_TOP)
		{
			BlockPos belowPos = pos.down();
			IBlockState below = world.getBlockState(belowPos);

			if (below.getBlock() == this && below.getValue(variantProp) == EnumAquaticPlant.CHARNIA)
			{
				world.setBlockState(belowPos, getReplacement(world, belowPos, below), 3);
			}
		}
		else if (variant == EnumAquaticPlant.CHARNIA)
		{
			BlockPos abovePos = pos.up();
			IBlockState above = world.getBlockState(abovePos);

			if (above.getBlock() == this && above.getValue(variantProp) == EnumAquaticPlant.CHARNIA_TOP)
			{
				world.setBlockState(abovePos, getReplacement(world, abovePos, above), 3);
			}
		}
	}

	public boolean canBlockStay(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		IBlockState below = world.getBlockState(pos.down());
		Block blockBelow = below.getBlock();
		EnumAquaticPlant variant = state.getValue(variantProp);

		if (!validGround.contains(below.getMaterial())
				&& !(blockBelow instanceof BlockGenesisRock)
				&& (variant != EnumAquaticPlant.CHARNIA_TOP || blockBelow != this || below.getValue(variantProp) != EnumAquaticPlant.CHARNIA))
		{
			return false;
		}

		IBlockState above = world.getBlockState(pos.up());

		if (above.getMaterial() != Material.WATER)
		{
			return false;
		}
		if (variant == EnumAquaticPlant.CHARNIA && world.getBlockState(pos.up(2)).getMaterial() != Material.WATER)
		{
			return false;
		}

		final List<IBlockState> blocks = WorldUtils.getBlocksAround(world, pos);

		for (int i = 0; i < blocks.size();)
		{
			final boolean corner0 = this.isWaterish(blocks.get(i++).getBlock());
			final boolean corner1 = this.isWaterish(blocks.get(i++).getBlock());
			boolean corner2;

			if (i == blocks.size())
			{
				corner2 = this.isWaterish(blocks.get(0).getBlock());
			}
			else
			{
				corner2 = this.isWaterish(blocks.get(i).getBlock());
			}

			if (corner0 && corner1 && corner2)
			{
				return true;
			}
		}

		return false;
	}

	private boolean isWaterish(Block block)
	{
		return (block == Blocks.WATER) || block == Blocks.FLOWING_WATER || (block == this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType()
	{
		return Block.EnumOffsetType.XZ;
	}

	@Override
	public IBlockState getReplacement(World world, BlockPos pos, IBlockState state)
	{
		return Blocks.WATER.getStateFromMeta(0);
	}
}
