package genesis.world.biome.decorate;

import genesis.block.BlockAsplenium;
import genesis.common.GenesisBlocks;
import genesis.util.math.PosVecIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class WorldGenAsplenium extends WorldGenDecorationBase
{
	public WorldGenAsplenium()
	{
		super();
		
		setPatchCount(20);
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		List<Pair<BlockPos, EnumFacing>> positions = new ArrayList<>();
		
		for (MutableBlockPos mutPos : new PosVecIterable(pos, EnumFacing.DOWN, -1))
		{
			if (world.isAirBlock(mutPos) && world.getLightFor(EnumSkyBlock.SKY, mutPos) >= 7)
			{
				for (EnumFacing side : EnumFacing.HORIZONTALS)
				{
					if (GenesisBlocks.asplenium.canPlaceBlockOnSide(world, mutPos, side))
					{
						positions.add(Pair.of(mutPos.toImmutable(), side));
					}
				}
			}
		}
		
		if (positions.isEmpty())
			return false;
		
		Pair<BlockPos, EnumFacing> position = positions.get(rand.nextInt(positions.size()));
		pos = position.getLeft();
		
		IBlockState placedState = GenesisBlocks.asplenium.getDefaultState()
				.withProperty(BlockAsplenium.FACING, position.getRight().getOpposite());
		
		return setAirBlock(world, pos, placedState);
	}
}
