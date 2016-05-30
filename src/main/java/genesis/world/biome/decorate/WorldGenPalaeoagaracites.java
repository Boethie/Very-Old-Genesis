package genesis.world.biome.decorate;

import genesis.block.BlockGenesisMushroom;
import genesis.block.IMushroomBase;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenPalaeoagaracites extends WorldGenDecorationBase
{
	public WorldGenPalaeoagaracites()
	{
		super(WorldBlockMatcher.STANDARD_AIR, WorldBlockMatcher.TRUE);
		
		setPatchRadius(8);
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		BlockPos logPos = pos.up(rand.nextInt(3) - 1);
		Block block = world.getBlockState(logPos).getBlock();
		
		if (block instanceof IMushroomBase)
		{
			IMushroomBase base = (IMushroomBase) block;
			EnumFacing side = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)];
			IBlockState placedState = GenesisBlocks.palaeoagaracites.getDefaultState()
					.withProperty(BlockGenesisMushroom.FACING, side.getOpposite());
			
			if (!base.canSustainMushroom(world, logPos, side, placedState))
				return false;
			
			return setAirBlock(world, logPos.offset(side), placedState);
		}
		
		return false;
	}
}
