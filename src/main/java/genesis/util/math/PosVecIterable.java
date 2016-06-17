package genesis.util.math;

import java.util.*;

import genesis.util.WorldUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3i;

public class PosVecIterable implements Iterable<MutableBlockPos>
{
	public static class PosVecIterator implements Iterator<MutableBlockPos>
	{
		private MutableBlockPos pos;
		private int countLeft;
		private final Vec3i offset;
		
		/**
		 * An iterator that begins at {@code start} (non-inclusive) and offsets for {@code count} iterations.
		 * 
		 * @param start The position to start from. <i>Not inclusive</i>, the iteration will start with next offset.
		 * @param offset The vector to offset by each iteration.
		 * @param count The number of iterations to do.
		 */
		public PosVecIterator(BlockPos pos, Vec3i offset, int count)
		{
			this.pos = pos instanceof MutableBlockPos ? (MutableBlockPos) pos : new MutableBlockPos(pos);
			this.countLeft = count;
			this.offset = offset;
		}
		
		/**
		 * An iterator that begins at {@code start} (non-inclusive) and offsets for {@code count} iterations.
		 * 
		 * @param start The position to start from. <i>Not inclusive</i>, the iteration will start with next offset.
		 * @param offset The facing to offset by each iteration.
		 * @param count The number of iterations to do.
		 */
		public PosVecIterator(BlockPos pos, EnumFacing offset, int count)
		{
			this(pos, offset.getDirectionVec(), count);
		}
		
		/**
		 * An iterator that begins at {@code start} (non-inclusive) and offsets for {@code count} iterations.<br>
		 * <br>
		 * This variation will iterate infinitely.
		 * 
		 * @param start The position to start from. <i>Not inclusive</i>, the iteration will start with next offset.
		 * @param offset The vector to offset by each iteration.
		 */
		public PosVecIterator(BlockPos pos, Vec3i offset)
		{
			this(pos, offset, -1);
		}
		
		/**
		 * An iterator that begins at {@code start} (non-inclusive) and offsets for {@code count} iterations.<br>
		 * <br>
		 * This variation will iterate infinitely.
		 * 
		 * @param start The position to start from. <i>Not inclusive</i>, the iteration will start with next offset.
		 * @param offset The facing to offset by each iteration.
		 */
		public PosVecIterator(BlockPos pos, EnumFacing offset)
		{
			this(pos, offset, -1);
		}
		
		@Override
		public boolean hasNext()
		{
			return (countLeft == -1 || countLeft > 0) && pos.getY() > 0;
		}
		
		@Override
		public MutableBlockPos next()
		{
			if (!hasNext())
				throw new NoSuchElementException();
			else if (countLeft > 0)
				countLeft--;
			
			return WorldUtils.setOffset(pos, offset);
		}
		
		public int getCountLeft()
		{
			return countLeft;
		}
	}
	
	private final BlockPos start;
	private final int count;
	private final Vec3i offset;
	
	/**
	 * An iterable that provides an iterator that begins at {@code start} (non-inclusive) and offsets for {@code count} iterations.
	 * 
	 * @param start The position to start from. <i>Not inclusive</i>, the iteration will start with next offset.
	 * @param offset The vector to offset by each iteration.
	 * @param count The number of iterations to do.
	 */
	public PosVecIterable(BlockPos start, Vec3i offset, int count)
	{
		// Make defensive copies to keep this iterable immutable.
		this.start = new BlockPos(start);
		this.count = count;
		this.offset = new Vec3i(offset.getX(), offset.getY(), offset.getZ());
	}
	
	/**
	 * An iterable that provides an iterator that begins at {@code start} (non-inclusive) and offsets for {@code count} iterations.
	 * 
	 * @param start The position to start from. <i>Not inclusive</i>, the iteration will start with next offset.
	 * @param offset The facing to offset by each iteration.
	 * @param count The number of iterations to do.
	 */
	public PosVecIterable(BlockPos start, EnumFacing offset, int count)
	{
		this(start, offset.getDirectionVec(), count);
	}
	
	/**
	 * An iterable that provides an iterator that begins at {@code start} (non-inclusive) and offsets for {@code count} iterations.<br>
	 * <br>
	 * This variation will iterate infinitely.
	 * 
	 * @param start The position to start from. <i>Not inclusive</i>, the iteration will start with next offset.
	 * @param offset The vector to offset by each iteration.
	 */
	public PosVecIterable(BlockPos start, Vec3i offset)
	{
		this(start, offset, -1);
	}
	
	/**
	 * An iterable that provides an iterator that begins at {@code start} (non-inclusive) and offsets for {@code count} iterations.<br>
	 * <br>
	 * This variation will iterate infinitely.
	 * 
	 * @param start The position to start from. <i>Not inclusive</i>, the iteration will start with next offset.
	 * @param offset The facing to offset by each iteration.
	 */
	public PosVecIterable(BlockPos start, EnumFacing offset)
	{
		this(start, offset, -1);
	}
	
	@Override
	public Iterator<MutableBlockPos> iterator()
	{
		return new PosVecIterator(start, offset, count);
	}
}
