package genesis.world.biome.decorate;

import genesis.block.BlockAsplenium;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WorldGenAsplenium extends WorldGenDecorationBase
{
	private static final WorldBlockMatcher MATCHER = (s, w, p) -> BlockAsplenium.canSustainAsplenum(s);

	@Override
	protected BlockPos findGround(IBlockAccess world, BlockPos pos, int distance)
	{
		do
		{
			if (MATCHER.apply(world, pos))
				return pos;
		} while ((pos = pos.down()).getY() >= 0);

		return null;
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = findGround(world, pos, -1);

		if (pos == null || random.nextInt(getRarity()) != 0)
			return false;

		boolean success = false;
		int count = getPatchCountProvider().get(random);

		for (int i = 0; i < count; i++)
		{
			BlockPos genPos = pos;

			if (i != 0)
			{
				Vec3d offset = new Vec3d(random.nextDouble() - 0.5, 0, random.nextDouble() - 0.5)
						.normalize()
						.scale(random.nextDouble() * getPatchMaxRadius());
				genPos = findGround(world,
						pos.add(offset.xCoord, offset.yCoord + getPatchStartHeight(), offset.zCoord),
						getPatchStartHeight() * 2 + 1);
			}

			if (genPos != null && place(world, random, genPos))
				success = true;
		}

		return success;
	}

	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		EnumFacing side = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)];
		IBlockState placedState = GenesisBlocks.asplenium.getDefaultState()
				.withProperty(BlockAsplenium.FACING, side.getOpposite());

		return setAirBlock(world, pos.offset(side), placedState);
	}
}
