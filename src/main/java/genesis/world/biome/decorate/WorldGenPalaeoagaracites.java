package genesis.world.biome.decorate;

import genesis.block.BlockGenesisMushroom;
import genesis.block.IGenesisMushroomBase;
import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenPalaeoagaracites extends WorldGenDecorationBase
{
	@Override
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		do
		{
			IBlockState state = world.getBlockState(pos);
			
			if (!state.getBlock().isAir(state, world, pos) && !state.getBlock().isLeaves(state, world, pos))
				break;
			
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() != GenesisBlocks.moss && state.getBlock() != Blocks.dirt)
			return false;
		
		boolean placedSome = false;
		
		for (int i = 0; i < this.getPatchSize(); ++i)
			if (placeMushroom(world, pos, random))
				placedSome = true;
		
		return placedSome;
	}
	
	protected boolean placeMushroom(World world, BlockPos pos, Random rand)
	{
		BlockPos mushroomPos = pos.add(rand.nextInt(17) - 8, rand.nextInt(21), rand.nextInt(17) - 8);
		Block block = world.getBlockState(mushroomPos).getBlock();
		
		if (block instanceof IGenesisMushroomBase)
		{
			EnumFacing facing = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)];
			mushroomPos = mushroomPos.offset(facing);
			
			if (!world.isAirBlock(mushroomPos))
				return false;
			
			setBlockInWorld(world, mushroomPos, GenesisBlocks.palaeoagaracites.getDefaultState().withProperty(BlockGenesisMushroom.FACING, facing.getOpposite()));
			return true;
		}
		
		return false;
	}
}
