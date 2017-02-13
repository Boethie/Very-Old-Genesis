package genesis.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;

/**
 * Specifies a list of bounding boxes that represent space volume.
 * Currently used to specify space shape required to grow tree.
 * <p>
 * The code currently doesn't check for overlapping bounding boxes so for best performance they shouldn't overlap
 */
public class BlockVolumeShape
{
	private static final BlockVolumeShape EMPTY = new BlockVolumeShape();
	
	private final List<StructureBoundingBox> boxes = new ArrayList<>();
	
	private BlockVolumeShape()
	{
	}
	
	public BlockVolumeShape and(int x1, int y1, int z1, int x2, int y2, int z2)
	{
		this.boxes.add(new StructureBoundingBox(x1, y1, z1, x2, y2, z2));
		return this;
	}
	
	public BlockVolumeShape and(int x, int y, int z)
	{
		this.boxes.add(new StructureBoundingBox(x, y, z, x, y, z));
		return this;
	}
	
	public boolean hasSpace(BlockPos origin, Predicate<BlockPos> isAllowed)
	{
		for (StructureBoundingBox box : boxes)
		{
			BlockPos start = origin.add(box.minX, box.minY, box.minZ);
			BlockPos end = origin.add(box.maxX, box.maxY, box.maxZ);
			
			for (BlockPos pos : BlockPos.getAllInBoxMutable(start, end))
			{
				// if this turns out to cause performance issues, only some random blocks can be checked
				if (!isAllowed.apply(pos))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public static BlockVolumeShape region(int x1, int y1, int z1, int x2, int y2, int z2)
	{
		return empty().and(x1, y1, z1, x2, y2, z2);
	}
	
	public static BlockVolumeShape block(int x, int y, int z)
	{
		return region(x, y, z, x, y, z);
	}
	
	public static BlockVolumeShape empty()
	{
		return new BlockVolumeShape();
	}
}
