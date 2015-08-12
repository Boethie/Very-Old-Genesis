package genesis.world.biome.decorate;

import genesis.block.BlockGenesisMushroom;
import genesis.block.IGenesisMushroomBase;
import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenPalaeoagaracites extends WorldGenDecorationBase
{
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isAir(world, pos) && !block.isLeaves(world, pos))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		boolean placedSome = false;
		
		for (int i = 0; i < this.getPatchSize(); ++i)
			if(placeMushroom(world, pos, random))
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
			
			if (!world.getBlockState(mushroomPos).getBlock().isAir(world, mushroomPos))
				return false;
			
			setBlockInWorld(world, mushroomPos, GenesisBlocks.palaeoagaracites.getDefaultState().withProperty(BlockGenesisMushroom.FACING, facing.getOpposite()));
			return true;
		}
		
		return false;
	}

	@Override
	public IBlockState getSpawnablePlant(Random rand)
	{
		return GenesisBlocks.palaeoagaracites.getDefaultState();
	}
}
