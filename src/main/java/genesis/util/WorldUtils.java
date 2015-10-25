package genesis.util;

import genesis.util.random.DoubleRange;
import genesis.util.random.IntRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SuppressWarnings("unchecked")
public class WorldUtils
{
	public static Iterable<BlockPos> getAreaFullHeight(BlockPos pos, int area, int height)
	{
		BlockPos start = new BlockPos(pos.getX() - area, 0, pos.getZ() - area);
		BlockPos end = new BlockPos(pos.getX() + area, height, pos.getZ() + area);
		return (Iterable<BlockPos>) BlockPos.getAllInBox(start, end);
	}
	
	public static Iterable<BlockPos> getArea(BlockPos pos, int area)
	{
		BlockPos start = pos.add(-area, -area, -area);
		BlockPos end = pos.add(area, area, area);
		return (Iterable<BlockPos>) BlockPos.getAllInBox(start, end);
	}
	
	/**
	 * @param worldIn The world.
	 * @param pos The position to start from.
	 * @param dNegX The distance along the X axis in the negative direction.
	 * @param dPosX The distance along the X axis in the positive direction.
	 * @param dNegZ The distance along the Z axis in the negative direction.
	 * @param dPosZ The distance along the Z axis in the positive direction.
	 * @param dNegY The distance along the Y axis in the negative direction.
	 * @param dPosY The distance along the Y axis in the positive direction.
	 * @return Whether there is water in range.
	 */
	public static boolean waterInRange(World world, BlockPos pos, int dNegX, int dPosX, int dNegZ, int dPosZ, int dNegY, int dPosY)
	{
		Iterable<BlockPos> checkArea = (Iterable<BlockPos>) BlockPos.getAllInBox(pos.add(-dNegX, -dNegY, -dNegZ), pos.add(dPosX, dPosY, dPosZ));
		
		for (BlockPos checkPos : checkArea)
		{
			if (world.getBlockState(checkPos).getBlock().getMaterial() == Material.water)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @param worldIn The world.
	 * @param pos The position to start from.
	 * @param dX The distance along the X axis to check from the starting block position.
	 * @param dZ The distance along the Z axis to check from the starting block position.
	 * @param dY The vertical distance to check from the starting block position.
	 * @return Whether there is water in range.
	 */
	public static boolean waterInRange(World worldIn, BlockPos pos, int dX, int dZ, int dY)
	{
		return waterInRange(worldIn, pos, dX, dX, dZ, dZ, dY, dY);
	}
	
	/**
	 * @param worldIn The world.
	 * @param pos The position to start from.
	 * @param dXZ The horizontal distance to check from the starting block position.
	 * @param dY The vertical distance to check from the starting block position.
	 * @return Whether there is water in range.
	 */
	public static boolean waterInRange(World worldIn, BlockPos pos, int dXZ, int dY)
	{
		return waterInRange(worldIn, pos, dXZ, dXZ, dY);
	}
	
	public static List<IBlockState> getBlocksAround(final World world, final BlockPos pos)
	{
		final List<IBlockState> blocks = new ArrayList<IBlockState>();

		BlockPos shift = pos.north();
		blocks.add(world.getBlockState(shift));

		shift = pos.north();
		shift = shift.east();
		blocks.add(world.getBlockState(shift));

		shift = pos.east();
		blocks.add(world.getBlockState(shift));

		shift = pos.east();
		shift = shift.south();
		blocks.add(world.getBlockState(shift));

		shift = pos.south();
		blocks.add(world.getBlockState(shift));

		shift = pos.south();
		shift = pos.west();
		blocks.add(world.getBlockState(shift));

		shift = pos.west();
		blocks.add(world.getBlockState(shift));

		shift = pos.west();
		shift = shift.north();
		blocks.add(world.getBlockState(shift));

		return blocks;
	}
	
	public static enum DropType
	{
		BLOCK(DoubleRange.create(-0.25, 0.25), 0, 0, true),
		CONTAINER(DoubleRange.create(-0.4, 0.4), 0.05, 0.2, false);
		
		public final DoubleRange offsetRange;
		public final double randomSpeed;
		public final double upwardSpeed;
		public final boolean delayPickup;
		
		DropType(DoubleRange offsetRange, double randomSpeed, double upwardSpeed, boolean delayPickup)
		{
			this.offsetRange = offsetRange;
			this.randomSpeed = randomSpeed;
			this.upwardSpeed = upwardSpeed;
			this.delayPickup = delayPickup;
		}
	}
	
	public static final IntRange ITEM_DROP_SIZE = IntRange.create(10, 30);
	
	/**
	 * Spawns the specified ItemStack in the world using the DropType to determine spawn offset and velocity.
	 */
	public static List<EntityItem> spawnItemsAt(World world, double x, double y, double z, DropType dropType, ItemStack stack)
	{
		if (!world.isRemote && stack != null)
		{
			Random rand = world.rand;
			ImmutableList.Builder<EntityItem> builder = ImmutableList.builder();
			
			if (dropType != null && dropType.offsetRange != null)
			{
				x += dropType.offsetRange.get(world.rand);
				y += dropType.offsetRange.get(world.rand);
				z += dropType.offsetRange.get(world.rand);
			}
			
			while (stack.stackSize > 0)
			{
				int subSize = Math.min(stack.stackSize, ITEM_DROP_SIZE.get(rand));
				
				stack.stackSize -= subSize;
				
				EntityItem dropItem = new EntityItem(world, x, y, z,
						new ItemStack(stack.getItem(), subSize, stack.getItemDamage()));
				
				if (stack.hasTagCompound())
				{
					dropItem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
				}
				
				if (dropType != null && (dropType.randomSpeed != 0 || dropType.upwardSpeed != 0))
				{
					dropItem.motionX = rand.nextGaussian() * dropType.randomSpeed;
					dropItem.motionY = rand.nextGaussian() * dropType.randomSpeed + dropType.upwardSpeed;
					dropItem.motionZ = rand.nextGaussian() * dropType.randomSpeed;
				}
				
				if (dropType == null || dropType.delayPickup)
				{
					dropItem.setDefaultPickupDelay();
				}
				
				world.spawnEntityInWorld(dropItem);
				builder.add(dropItem);
			}
			
			return builder.build();
		}
		
		return Collections.emptyList();
	}
	
	public static List<EntityItem> spawnItemsAt(World world, Vec3 pos, DropType dropType, ItemStack stack)
	{
		return spawnItemsAt(world, pos.xCoord, pos.yCoord, pos.zCoord, dropType, stack);
	}
	
	public static final DoubleRange ITEM_OFFSET = DoubleRange.create(-0.4, 0.4);
	
	public static List<EntityItem> spawnItemsAt(World world, BlockPos pos, DropType dropType, ItemStack stack)
	{
		return spawnItemsAt(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropType, stack);
	}
	
	public static void spawnXPOrbs(World world, double x, double y, double z, float fAmount)
	{
		if (!world.isRemote)
		{
			int amount = MathHelper.floor_float(fAmount);
			
			if (amount < fAmount && world.rand.nextFloat() <= fAmount - amount)
			{
				amount++;
			}
			
			while (amount > 0)
			{
				int split = EntityXPOrb.getXPSplit(amount);
				world.spawnEntityInWorld(new EntityXPOrb(world, x, y, z, split));
				amount -= split;
			}
		}
	}
	
	public static void setProperty(World world, BlockPos pos, IProperty property, Comparable<?> value)
	{
		world.setBlockState(pos, world.getBlockState(pos).withProperty(property, value));
	}
	
	public static Random getWorldRandom(IBlockAccess world, Random or)
	{
		return world instanceof World ? ((World) world).rand : or;
	}
	
	public static NBTTagCompound writeBlockPosToNBT(BlockPos pos)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("x", pos.getX());
		compound.setInteger("y", pos.getY());
		compound.setInteger("z", pos.getZ());
		return compound;
	}
	
	public static BlockPos readBlockPosFromNBT(NBTTagCompound compound)
	{
		return new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
	}
}
