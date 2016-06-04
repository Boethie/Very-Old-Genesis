package genesis.item;

import genesis.block.BlockGenesisSlab;
import genesis.block.BlockGenesisSlab.EnumHalf;
import genesis.combo.SlabBlocks;
import genesis.combo.SlabBlocks.SlabObjectType;
import genesis.combo.variant.EnumSlab;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static genesis.block.BlockGenesisSlab.EnumHalf.*;

public class ItemGenesisSlab extends ItemBlockMulti<EnumSlab>
{
	public final SlabBlocks owner;
	public final SlabObjectType type;

	public final List<EnumSlab> variants;

	public ItemGenesisSlab(BlockGenesisSlab slabBlock, SlabBlocks owner,
			SlabObjectType type,
			List<EnumSlab> variants, Class<EnumSlab> variantClass)
	{
		super(slabBlock, owner, type, variants, variantClass);

		this.owner = owner;
		this.type = type;

		this.variants = variants;

		setHasSubtypes(true);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize == 0 || !player.canPlayerEdit(pos.offset(facing), facing, stack))
		{
			return EnumActionResult.FAIL;
		}
		
		EnumSlab variant = owner.getVariant(stack);
		IBlockState state = world.getBlockState(pos);
		EnumSlab stateVariant = owner.getVariant(state);
		
		if (variant == stateVariant && canFillEmptyHalf(state.getValue(BlockGenesisSlab.HALF), facing))
		{
			IBlockState doubleSlabState = owner.getBlockState(type, stateVariant).withProperty(BlockGenesisSlab.HALF, BOTH);
			AxisAlignedBB collisionBB = doubleSlabState.getCollisionBoundingBox(world, pos);

			if (collisionBB != Block.NULL_AABB && world.checkNoEntityCollision(collisionBB.offset(pos)) && world.setBlockState(pos, doubleSlabState, 11))
			{
				SoundType sound = doubleSlabState.getBlock().getSoundType();
				world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
				--stack.stackSize;
			}

			return EnumActionResult.SUCCESS;
		}
		
		if (tryPlace(player, stack, world, pos.offset(facing), variant))
		{
			return EnumActionResult.SUCCESS;
		}
		
		return onItemBlockUse(stack, variant, player, state, world, pos, facing, hitX, hitY, hitZ);
	}

	private EnumActionResult onItemBlockUse(ItemStack stack, EnumSlab material, EntityPlayer player, IBlockState oldState, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!oldState.getBlock().isReplaceable(world, pos))
		{
			pos = pos.offset(facing);
		}

		if (!world.canBlockBePlaced(block, pos, false, facing, null, stack))
		{
			return EnumActionResult.FAIL;
		}

		IBlockState state = owner.getBlockState(type, material);
		int meta = block.getMetaFromState(state);
		state = block.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, player);

		if (placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, state))
		{
			SoundType sound = block.getSoundType();
			world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
			--stack.stackSize;
		}

		return EnumActionResult.SUCCESS;
	}

	private boolean tryPlace(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumSlab variant)
	{
		IBlockState state = world.getBlockState(pos);
		EnumSlab stateVariant = owner.getVariant(state);

		if (variant == stateVariant && state.getValue(BlockGenesisSlab.HALF) != BOTH)
		{
			IBlockState doubleSlabState = owner.getBlockState(type, variant).withProperty(BlockGenesisSlab.HALF, BOTH);
			AxisAlignedBB collisionBB = doubleSlabState.getCollisionBoundingBox(world, pos);

			if (collisionBB != Block.NULL_AABB && world.checkNoEntityCollision(collisionBB.offset(pos)) && world.setBlockState(pos, doubleSlabState, 11))
			{
				SoundType sound = doubleSlabState.getBlock().getSoundType();
				world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
				--stack.stackSize;
			}

			return true;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack stack)
	{
		EnumSlab variant = owner.getVariant(stack);
		IBlockState state = world.getBlockState(pos);
		EnumSlab stateVariant = owner.getVariant(state);

		if (variant == stateVariant && canFillEmptyHalf(state.getValue(BlockGenesisSlab.HALF), facing))
		{
			return true;
		}

		BlockPos sidePos = pos.offset(facing);
		IBlockState sideState = world.getBlockState(sidePos);
		EnumSlab sideVariant = owner.getVariant(sideState);

		if (variant == sideVariant && sideState.getValue(BlockGenesisSlab.HALF) != BOTH)
		{
			return true;
		}

		return canPlaceItemBlockOnSide(world, pos, facing, player, stack);
	}

	@SideOnly(Side.CLIENT)
	private boolean canPlaceItemBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
	{
		Block block = world.getBlockState(pos).getBlock();

		if (!block.isReplaceable(world, pos))
		{
			pos = pos.offset(side);
		}

		return world.canBlockBePlaced(this.block, pos, false, side, null, stack);
	}

	private boolean canFillEmptyHalf(EnumHalf blockHalf, EnumFacing facing)
	{
		switch (blockHalf)
		{
		case BOTTOM:
			return facing == EnumFacing.UP;
		case TOP:
			return facing == EnumFacing.DOWN;
		default:
			return false;
		}
	}
}
