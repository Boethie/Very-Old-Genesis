package genesis.block;

import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.EnumTree.FruitType;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.util.*;

import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGenesisLeaves extends BlockLeaves
{
	@BlockProperties
	public static final IProperty<?>[] STORE_PROPERTIES = { CHECK_DECAY, DECAYABLE };

	public final TreeBlocksAndItems owner;
	public final ObjectType<EnumTree, BlockGenesisLeaves, ItemBlockMulti<EnumTree>> type;

	public final List<EnumTree> variants;
	public final PropertyIMetadata<EnumTree> variantProp;

	public BlockGenesisLeaves(TreeBlocksAndItems owner,
			ObjectType<EnumTree, BlockGenesisLeaves, ItemBlockMulti<EnumTree>> type,
			List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super();

		this.owner = owner;
		this.type = type;

		this.variants = variants;
		variantProp = new PropertyIMetadata<>("variant", variants, variantClass);

		blockState = new BlockStateContainer(this, variantProp, CHECK_DECAY, DECAYABLE);
		setDefaultState(getBlockState().getBaseState().withProperty(DECAYABLE, true).withProperty(CHECK_DECAY, false));

		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setSoundType(SoundType.PLANT);

		Blocks.FIRE.setFireInfo(this, 30, 60);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, state.getValue(variantProp));
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return owner.getStack(type, state.getValue(variantProp));
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return Collections.singletonList(owner.getStack(type, world.getBlockState(pos).getValue(variantProp)));
	}

	protected ItemStack getSapling(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return owner.getStack(TreeBlocksAndItems.SAPLING, state.getValue(variantProp));
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<>();

		Random rand = WorldUtils.getWorldRandom(world, RANDOM);

		int chance = getSaplingDropChance(state);

		if (fortune > 0)
		{
			chance = Math.max(chance - (2 << fortune), 10);
		}

		if (rand.nextInt(chance) == 0)
		{
			ret.add(getSapling(world, pos, state));
		}

		return ret;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getDefaultState();
		state = state.withProperty(variantProp, owner.getVariant(this, meta));
		state = state.withProperty(DECAYABLE, false).withProperty(CHECK_DECAY, false);
		return state;
	}

	protected void breakAndDrop(World world, BlockPos pos)
	{
		dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
		world.setBlockToAir(pos);
	}

	protected int leafDistance = 5;
	protected float[] distanceCosts = {leafDistance, 1, 1.4142F, 1.7321F};	// 0 uses leafDistance to make sure there are no stack overflows.

	public boolean isConnectedToLog(EnumTree treeType, World world, BlockPos curPos, float curCost)
	{
		if (curCost < leafDistance)
		{
			IBlockState state = world.getBlockState(curPos);
			TreeBlocksAndItems.VariantData data = owner.getVariantData(state);

			if (data != null && data.variant == treeType)
			{
				if (data.type == TreeBlocksAndItems.LOG || data.type == TreeBlocksAndItems.BRANCH)
				{
					return true;
				}
				else if (data.type == TreeBlocksAndItems.LEAVES || data.type == TreeBlocksAndItems.LEAVES_FRUIT)
				{
					Iterable<BlockPos> blocksAround = WorldUtils.getArea(curPos.add(-1, -1, -1), curPos.add(1, 1, 1));

					for (BlockPos nextPos : blocksAround)
					{
						if (!nextPos.equals(curPos))
						{
							BlockPos diff = curPos.subtract(nextPos);
							int blockDist = Math.abs(diff.getX()) + Math.abs(diff.getY()) + Math.abs(diff.getZ());
							float addCost = distanceCosts[blockDist];

							if (isConnectedToLog(treeType, world, nextPos, curCost + addCost))
							{
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

	public boolean isConnectedToLog(World world, BlockPos pos)
	{
		return isConnectedToLog(world.getBlockState(pos).getValue(variantProp), world, pos, 0);
	}

	protected void checkAndDoDecay(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);

		if (state.getValue(DECAYABLE) && state.getValue(CHECK_DECAY) && world.isAreaLoaded(pos.add(-leafDistance, -leafDistance, -leafDistance), pos.add(leafDistance, leafDistance, leafDistance)))
		{
			if (isConnectedToLog(world, pos))
			{
				world.setBlockState(pos, state.withProperty(CHECK_DECAY, false), 4);
			}
			else
			{
				breakAndDrop(world, pos);
			}
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		checkAndDoDecay(world, pos);

		state = world.getBlockState(pos);

		if (state.getBlock() == this && state.getValue(DECAYABLE)
			&& world.rand.nextInt(8) == 0)
		{
			EnumTree variant = state.getValue(variantProp);

			if (variant.getFruitType() == FruitType.LEAVES)
			{
				int fruitCount = 4;
				final int radius = 8;

				for (BlockPos checkPos : BlockPos.getAllInBoxMutable(pos.add(-radius, -radius, -radius), pos.add(radius, radius, radius)))
				{
					if (owner.isStateOf(world.getBlockState(checkPos), variant, TreeBlocksAndItems.LEAVES_FRUIT))
					{
						fruitCount--;
						if (fruitCount <= 0)
							break;
					}
				}

				if (fruitCount > 0)
				{
					world.setBlockState(pos, owner.getBlockState(TreeBlocksAndItems.LEAVES_FRUIT, variant)
							.withProperty(DECAYABLE, state.getValue(DECAYABLE))
							.withProperty(CHECK_DECAY, state.getValue(CHECK_DECAY)));
				}
			}
		}
	}

	@Override
	public EnumType getWoodType(int meta)
	{
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		if (Minecraft.isFancyGraphicsEnabled())
		{
			return layer == BlockRenderLayer.CUTOUT_MIPPED;
		}

		if (state.getValue(variantProp).hasLayeredLeaves())
		{
			// on fancy graphics both the leaves and flower/fruit layer can be drawn in cutout
			// (this means that MC can't cull leave faces)
			// on fast there is special multilayer model that renders the leaves part in solid
			// and the fruit/flower layer renders in cutout.
			// To make this block render in both layers, this check needs to be performed.
			return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.SOLID;
		}

		return layer == BlockRenderLayer.SOLID;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube(IBlockState state)
	{
		return !Minecraft.isFancyGraphicsEnabled();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		if (Minecraft.isFancyGraphicsEnabled())
		{
			return true;
		}

		BlockPos sidePos = pos.offset(side);
		if (!world.getBlockState(sidePos).doesSideBlockRendering(world, sidePos, side.getOpposite()))
		{
			return true;
		}

		return super.shouldSideBeRendered(state, world, pos, side);
	}
}
