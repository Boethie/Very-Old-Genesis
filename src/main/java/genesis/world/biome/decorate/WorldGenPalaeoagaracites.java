package genesis.world.biome.decorate;

import genesis.block.BlockGenesisMushroom;
import genesis.block.IGenesisMushroomBase;
import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
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
		
		for (int i = 0; i < 100; ++i)
			if(placeMushroom(world, pos, random))
				placedSome = true;
		
		return placedSome;
	}
	
	private boolean placeMushroom(World world, BlockPos pos, Random rand)
	{
		boolean placed = false;
		
		BlockPos mushroomPos = pos.add(rand.nextInt(17) - 8, rand.nextInt(21), rand.nextInt(17) - 8);
		Block block = world.getBlockState(mushroomPos).getBlock();
		
		if (block instanceof IGenesisMushroomBase)
		{
			int location = rand.nextInt(4);
			BlockGenesisMushroom.MushroomEnumFacing mushroomFacing;
			
			switch(location)
			{
			case 1:
				mushroomPos = mushroomPos.south();
				mushroomFacing = BlockGenesisMushroom.MushroomEnumFacing.NORTH;
				break;
			case 2:
				mushroomPos = mushroomPos.east();
				mushroomFacing = BlockGenesisMushroom.MushroomEnumFacing.WEST;
				break;
			case 3:
				mushroomPos = mushroomPos.west();
				mushroomFacing = BlockGenesisMushroom.MushroomEnumFacing.EAST;
				break;
			default:
				mushroomPos = mushroomPos.north();
				mushroomFacing = BlockGenesisMushroom.MushroomEnumFacing.SOUTH;
				break;
			}
			
			if (!world.getBlockState(mushroomPos).getBlock().isAir(world, mushroomPos))
				return false;
			
			setBlockInWorld(world, mushroomPos, GenesisBlocks.palaeoagaracites.getDefaultState().withProperty(BlockGenesisMushroom.FACING, mushroomFacing));
			placed = true;
		}
		
		return placed;
	}
}
