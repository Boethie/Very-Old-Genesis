package genesis.block;

import genesis.combo.ToolItems;
import genesis.combo.ToolItems.ToolObjectType;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.PropertyIMetadata;
import genesis.combo.variant.ToolTypes.ToolType;
import genesis.common.Genesis;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.item.ItemPebble;
import genesis.network.client.MultiPartBlock;
import genesis.network.client.MultiPartBreakMessage;
import genesis.util.BlockStateToMetadata;
import genesis.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockPebble extends Block implements MultiPartBlock
{
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{NW, NE, SE, SW};
	}

	public static final PropertyBool NW = PropertyBool.create("nw");
	public static final PropertyBool NE = PropertyBool.create("ne");
	public static final PropertyBool SE = PropertyBool.create("se");
	public static final PropertyBool SW = PropertyBool.create("sw");

	public final ToolItems owner;
	public final ToolObjectType<BlockPebble, ItemPebble> type;

	public final ToolType variant;
	public final PropertyIMetadata<ToolType> variantProp;
	public PropertyInteger randomProp;

	protected AxisAlignedBB bounds = FULL_BLOCK_AABB;

	public BlockPebble(ToolItems owner, ToolObjectType<BlockPebble, ItemPebble> type, ToolType variant, Class<ToolType> variantClass)
	{
		super(Material.ROCK);
		setSoundType(GenesisSoundTypes.PEBBLE);

		this.owner = owner;
		this.type = type;

		this.variant = variant;
		variantProp = new PropertyIMetadata<>("variant", Collections.singletonList(variant), variantClass);

		final String randomName = "zrandom";
		Genesis.proxy.callClient((c) ->
		{
			// TODO: Random variants
			//Set<String> variants = ModelHelpers.getBlockstatesVariants(new ResourceLocation("genesis:pebble")).keySet();

			randomProp = PropertyInteger.create(randomName, 0, 1);
		});

		Genesis.proxy.callServer((s) -> randomProp = PropertyInteger.create(randomName, 0, 1));

		blockState = new BlockStateContainer(this, variantProp, randomProp, NW, NE, SE, SW);
		setDefaultState(getBlockState().getBaseState()
				.withProperty(randomProp, 0)
				.withProperty(NW, false)
				.withProperty(NE, false)
				.withProperty(SE, false)
				.withProperty(SW, false));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, NW, NE, SE, SW);
	}

	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata, NW, NE, SE, SW);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if (world instanceof World && ((World) world).isRemote)
		{
			// TODO: Random variants here too.
			/*long rand = MathHelper.getPositionRandom(pos);
			Collection<Integer> values = randomProp.getAllowedValues();
			int size = values.size();
			Iterator<Integer> iter = values.iterator();
			int value = 0;

			for (int i = 0; i < (rand / 5.0F) % size; i++)
			{
				value = iter.next();
			}*/

			state = state.withProperty(randomProp, 0);
		}

		return super.getActualState(state, world, pos);
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
	public IBlockState onBlockPlaced(World world, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer)
	{
		IBlockState state = world.getBlockState(pos);

		if (!owner.isStateOf(state, variant, type))
		{
			state = owner.getBlockState(type, variant);
		}

		boolean north = hitZ < 0.5F;
		boolean east = hitX > 0.5F;

		if (north)
			if (east)
				state = state.withProperty(NE, true);
			else
				state = state.withProperty(NW, true);
		else
			if (east)
				state = state.withProperty(SE, true);
			else
				state = state.withProperty(SW, true);

		return state;
	}

	protected enum Part
	{
		NW(BlockPebble.NW, new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 0.25, 0.5)),
		NE(BlockPebble.NE, new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 0.25, 0.5)),
		SE(BlockPebble.SE, new AxisAlignedBB(0.5, 0.0, 0.5, 1.0, 0.25, 1.0)),
		SW(BlockPebble.SW, new AxisAlignedBB(0.0, 0.0, 0.5, 0.5, 0.25, 1.0));

		public final PropertyBool prop;
		public final AxisAlignedBB bounds;

		Part(PropertyBool prop, AxisAlignedBB bounds)
		{
			this.prop = prop;
			this.bounds = bounds;
		}
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end)
	{
		ArrayList<Pair<AxisAlignedBB, RayTraceResult>> hits = new ArrayList<>();

		boolean hasPebble = false;

		for (Part part : Part.values())
		{
			if (state.getValue(part.prop))
			{
				hasPebble = true;
				RayTraceResult hit = rayTrace(pos, start, end, part.bounds);

				if (hit != null)
				{
					hit.subHit = part.ordinal();
					hits.add(Pair.of(part.bounds, hit));
				}
			}
		}

		double lastDistSqr = -1;
		Pair<AxisAlignedBB, RayTraceResult> hit = null;

		for (Pair<AxisAlignedBB, RayTraceResult> checkHit : hits)
		{
			double distSqr = checkHit.getRight().hitVec.squareDistanceTo(start);

			if (lastDistSqr == -1 || distSqr < lastDistSqr)
			{
				lastDistSqr = distSqr;
				hit = checkHit;
			}
		}

		if (hit != null)
		{
			bounds = hit.getLeft();
			return hit.getRight();
		}

		if (!hasPebble)
		{
			bounds = FULL_BLOCK_AABB;
			return super.collisionRayTrace(state, world, pos, start, end);
		}

		return null;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		return bounds.offset(pos);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos,
			AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		for (Part part : Part.values())
			if (state.getValue(part.prop))
				Block.addCollisionBoxToList(pos, mask, list, part.bounds);
	}

	protected boolean dropAll = true;

	public boolean removePart(IBlockState state,
			World world, BlockPos pos, Part part,
			EntityPlayer player, boolean harvest) {
		if (world.isRemote)
			Genesis.network.sendToServer(new MultiPartBreakMessage(pos, part.ordinal()));

		state = state.withProperty(part.prop, false);

		boolean hasPebble = false;

		for (Part checkPart : Part.values()) {
			if (state.getValue(checkPart.prop)) {
				hasPebble = true;
				break;
			}
		}

		world.setBlockState(pos, state);

		if (player == null || !player.capabilities.isCreativeMode) {
			dropAll = false;
			WorldUtils.spawnBlockDrops(world, pos, state);
			dropAll = true;
		}

		return hasPebble || super.removedByPlayer(state, world, pos, player, harvest);
	}

	@Override
	public boolean removePart(IBlockState state,
			World world, BlockPos pos, int part,
			EntityPlayer player, boolean harvest) {
		return !(part < 0 || part >= Part.values().length) &&
						removePart(state, world, pos, Part.values()[part], player, harvest);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean harvest)
	{
		if (world.isRemote)
		{
			RayTraceResult hit = player.rayTrace(15, 1);

			if (hit != null && pos.equals(hit.getBlockPos()))
				return removePart(state, world, pos, hit.subHit, player, harvest);
		}

		return false;
	}

	@Override
	public boolean activate(IBlockState state,
			World world, BlockPos pos, int part,
			EntityPlayer player, ItemStack stack, EnumHand hand)
	{
		return false;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		if (dropAll)
		{
			int count = 0;

			for (Part part : Part.values())
			{
				if (state.getValue(part.prop))
				{
					count++;
				}
			}

			return count;
		}

		return 1;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getStack(type, state.getValue(variantProp)).getItemDamage();
	}

	protected boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		return world.isSideSolid(pos.down(), EnumFacing.UP);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return canBlockStay(world, pos, world.getBlockState(pos));
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!canBlockStay(world, pos, state))
		{
			world.destroyBlock(pos, true);
		}
		super.updateTick(world, pos, state, rand);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		if (!canBlockStay(world, pos, state))
		{
			world.destroyBlock(pos, true);
		}
	}
}
