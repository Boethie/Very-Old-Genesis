package genesis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldUtils
{
	/**
	 * @param worldIn The world.
	 * @param pos The position to start from.
	 * @param dHoriz The horizontal distance to check from the starting block position.
	 * @param dVert The vertical distance to check from the starting block position.
	 * @return Whether there is water in range.
	 */
	public static boolean waterInRange(World worldIn, BlockPos pos, int dHoriz, int dVert)
	{
		Iterable<BlockPos> checkArea = (Iterable<BlockPos>) BlockPos.getAllInBox(pos.add(-dHoriz, -1 - dVert, -dHoriz), pos.add(dHoriz, -1 + dVert, dHoriz));
		
		for (BlockPos checkPos : checkArea)
		{
			if (worldIn.getBlockState(checkPos).getBlock().getMaterial() == Material.water)
			{
				return true;
			}
		}
		
		return false;
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
	
	public static final RandomIntRange ITEM_DROP_SIZE = new RandomIntRange(10, 30);
	public static final RandomDoubleRange ITEM_OFFSET = new RandomDoubleRange(0.1, 0.9);
	
	public static void spawnItemsAt(World world, double x, double y, double z, ItemStack stack)
	{
		Random rand = world.rand;
		
		if (stack != null)
		{
			double offX = ITEM_OFFSET.getRandom(rand);
			double offY = ITEM_OFFSET.getRandom(rand);
			double offZ = ITEM_OFFSET.getRandom(rand);
			
			while (stack.stackSize > 0)
			{
				int subSize = Math.min(stack.stackSize, ITEM_DROP_SIZE.getRandom(rand));
				
				stack.stackSize -= subSize;
				
				EntityItem dropItem = new EntityItem(world, x + offX, y + offY, z + offZ,
						new ItemStack(stack.getItem(), subSize, stack.getItemDamage()));
				
				if (stack.hasTagCompound())
				{
					dropItem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
				}
				
				float speed = 0.05F;
				dropItem.motionX = rand.nextGaussian() * speed;
				dropItem.motionY = rand.nextGaussian() * speed + 0.2;
				dropItem.motionZ = rand.nextGaussian() * speed;
				
				world.spawnEntityInWorld(dropItem);
			}
		}
	}
	
	public static void spawnItemsAt(World world, double x, double y, double z, Iterable<ItemStack> stacks)
	{
		for (ItemStack stack : stacks)
		{
			spawnItemsAt(world, x, y, z, stack);
		}
	}

	public static void setProperty(World world, BlockPos pos, IProperty property, Comparable value)
	{
		world.setBlockState(pos, world.getBlockState(pos).withProperty(property, value));
	}
}
