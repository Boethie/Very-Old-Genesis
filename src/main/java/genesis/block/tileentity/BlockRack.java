package genesis.block.tileentity;

import genesis.combo.ObjectType;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.Genesis;
import genesis.item.ItemRack;
import genesis.network.client.MultiPartActivateMessage;
import genesis.network.client.MultiPartBlock;
import genesis.network.client.MultiPartBreakMessage;
import genesis.util.AABBUtils;
import genesis.util.BlockStateToMetadata;
import genesis.util.StreamUtils;
import genesis.util.WorldUtils;
import genesis.util.WorldUtils.DropType;
import genesis.util.blocks.FacingProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class BlockRack extends BlockContainer implements MultiPartBlock
{
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return RACKS.toArray();
	}

	public static final FacingProperties<Boolean> RACKS =
			FacingProperties.createHorizontal((f) -> PropertyBool.create(f.getName()));

	private static final Map<EnumFacing, AxisAlignedBB> BAKED_BOUNDS;

	static
	{
		AxisAlignedBB base = new AxisAlignedBB(0, 0.75, 0,
												1, 1, 0.125);

		BAKED_BOUNDS = Arrays.stream(EnumFacing.VALUES)
				.filter(RACKS::has)
				.collect(StreamUtils.toImmMap((f) -> f, (f) -> AABBUtils.rotateTo(base, f)));
	}

	public final TreeBlocksAndItems owner;
	public final ObjectType<EnumTree, ? extends BlockRack, ? extends ItemRack> type;

	public final PropertyIMetadata<EnumTree> variantProp;
	public final EnumTree variant;

	private AxisAlignedBB bounds;

	public BlockRack(TreeBlocksAndItems owner,
			ObjectType<EnumTree, BlockRack, ItemRack> type,
			EnumTree variant, Class<EnumTree> variantClass)
	{
		super(Material.WOOD);

		setHardness(0);
		setResistance(5);
		setSoundType(SoundType.WOOD);

		this.owner = owner;
		this.type = type;

		variantProp = new PropertyIMetadata<>("variant", Collections.singletonList(variant), variantClass);
		this.variant = variant;

		blockState = new BlockStateContainer(this, RACKS.toArrayWith(variantProp));
		setDefaultState(RACKS.stateWith(getBlockState().getBaseState(), false));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityRack();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityRack te = getTileEntity(world, pos);

		if (te != null)
		{
			InventoryHelper.dropInventoryItems(world, pos, te);
			world.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(world, pos, state);
	}

	public static TileEntityRack getTileEntity(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileEntityRack)
			return (TileEntityRack) te;

		return null;
	}

	@Override
	public boolean activate(IBlockState state,
			World world, BlockPos pos, int part,
			EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		if (world.isRemote)
			Genesis.network.sendToServer(new MultiPartActivateMessage(pos, part, hand));

		TileEntityRack te = getTileEntity(world, pos);

		if (te != null)
		{
			EnumFacing side = EnumFacing.getHorizontal(part);

			if (state.getValue(RACKS.get(side)))
			{
				ItemStack displayStack = te.getStackInSide(side);

				if (displayStack == null)
				{
					if (heldStack != null)
					{
						te.setStackInSide(side, heldStack.splitStack(1));

						if (player.capabilities.isCreativeMode)
							heldStack.stackSize++;

						if (heldStack.stackSize <= 0)
							player.setHeldItem(hand, null);

						Genesis.logger.debug("add " + world.isRemote + " " + te.getStackInSide(side));

						return true;
					}
				}
				else
				{
					Genesis.logger.debug("remove " + world.isRemote + " " + displayStack);
					WorldUtils.spawnItemsAt(world, pos, DropType.CONTAINER, displayStack);
					te.setStackInSide(side, null);
					return true;
				}
			}

			te.markDirty();
		}

		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (owner.isStackOf(stack, type))
			return false;

		if (!world.isRemote)
			return TileEntityRack.isItemValid(stack);

		RayTraceResult hit = player.rayTrace(15, 1);

		return hit.getBlockPos().equals(pos) && activate(state, world, pos, hit.subHit, player, stack, hand);
	}

	public boolean canBlockStay(World world, BlockPos pos, EnumFacing side)
	{
		return world.isSideSolid(pos.offset(side), side.getOpposite());
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return RACKS.has(side) && canBlockStay(world, pos, side.getOpposite()) && super.canPlaceBlockOnSide(world, pos, side);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		IBlockState blockState = world.getBlockState(pos);

		for (FacingProperties.Entry<Boolean> entry : RACKS) {
			if (blockState.getValue(entry.property) && !canBlockStay(world, pos, entry.facing)) {
				removePart(blockState, world, pos, entry.facing, null, true);
			}
		}
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer)
	{
		IBlockState state = world.getBlockState(pos);
		EnumTree variant = owner.getVariant(this, meta);

		if (!owner.isStateOf(state, variant, type))
			state = owner.getBlockState(type, variant);

		if (RACKS.has(facing))
			state = state.withProperty(RACKS.get(facing.getOpposite()), true);

		return state;
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end)
	{
		ArrayList<Pair<AxisAlignedBB, RayTraceResult>> hits = new ArrayList<>();

		boolean hasRack = false;

		for (FacingProperties.Entry<Boolean> entry : RACKS)
		{
			if (state.getValue(entry.property))
			{
				AxisAlignedBB bounds = BAKED_BOUNDS.get(entry.facing);
				hasRack = true;
				RayTraceResult hit = rayTrace(pos, start, end, bounds);

				if (hit != null)
				{
					hit.subHit = entry.facing.getHorizontalIndex();
					hits.add(Pair.of(bounds, hit));
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

		if (!hasRack)
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
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		AxisAlignedBB bb = new AxisAlignedBB(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);

		for (FacingProperties.Entry<Boolean> entry : RACKS)
			if (state.getValue(entry.property))
				bb = bb.union(BAKED_BOUNDS.get(entry.facing));

		return bb;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos,
			AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		for (FacingProperties.Entry<Boolean> entry : RACKS)
			if (state.getValue(entry.property))
				Block.addCollisionBoxToList(pos, mask, list, BAKED_BOUNDS.get(entry.facing));
	}

	private boolean dropAll;

	public boolean removePart(IBlockState state,
			World world, BlockPos pos, EnumFacing side,
			EntityPlayer player, boolean harvest) {
		if (world.isRemote)
			Genesis.network.sendToServer(new MultiPartBreakMessage(pos, side.getHorizontalIndex()));

		state = state.withProperty(RACKS.get(side), false);

		boolean hasRack = false;

		for (FacingProperties.Entry<Boolean> check : RACKS) {
			if (state.getValue(check.property)) {
				hasRack = true;
				break;
			}
		}

		TileEntityRack te = getTileEntity(world, pos);

		if (player == null || !player.capabilities.isCreativeMode) {
			dropAll = false;
			dropBlockAsItem(world, pos, state, 0);
			dropAll = true;

			if (te != null) {
				ItemStack displayStack = te.getStackInSide(side);

				if (displayStack != null) {
					WorldUtils.spawnItemsAt(world, pos, DropType.CONTAINER, displayStack);
				}
			}
		}

		if (te != null)
			te.setStackInSide(side, null);

		world.setBlockState(pos, state);

		return hasRack || super.removedByPlayer(state, world, pos, player, harvest);
	}

	@Override
	public boolean removePart(IBlockState state,
			World world, BlockPos pos, int part,
			EntityPlayer player, boolean harvest)
	{
		return removePart(state, world, pos, EnumFacing.getHorizontal(part), player, harvest);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		if (dropAll)
		{
			int count = 0;

			for (FacingProperties.Entry<Boolean> check : RACKS)
			{
				if (state.getValue(check.property))
				{
					count++;
					break;
				}
			}

			return count;
		}

		return 1;
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
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.SOLID;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		/*float pixel = 1.0F / 16;

		switch (state.getValue(FACING))
		{
			case NORTH:
				return new AxisAlignedBB(0, pixel * 12, pixel * 14, 1,
						1, 1);
			case SOUTH:
				return new AxisAlignedBB(0, pixel * 12, 0, 1,
						1, pixel * 2);
			case WEST:
				return new AxisAlignedBB(pixel * 14, pixel * 12, 0, 1,
						1, 1);
			case EAST:
				return new AxisAlignedBB(0, pixel * 12, 0, pixel * 2,
						1, 1);
		}*/

		return FULL_BLOCK_AABB;
	}
}
