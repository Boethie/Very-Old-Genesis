package genesis.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public class BlockMycorrhiza extends BlockPeat
{
	public BlockMycorrhiza()
	{
		setHardness(0.6F);
		setSoundType(SoundType.PLANT);
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		switch (plantable.getPlantType(world, pos.offset(direction)))
		{
			case Plains:
				return true;
		}

		return super.canSustainPlant(state, world, pos, direction, plantable);
	}
}