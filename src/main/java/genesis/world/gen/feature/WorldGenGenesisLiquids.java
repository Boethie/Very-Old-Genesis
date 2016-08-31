package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenGenesisLiquids extends WorldGenerator
{
	public final Block liquid;
	public final Block stone;
	
	public WorldGenGenesisLiquids(Block liquid)
	{
		this(liquid, GenesisBlocks.granite);
	}
	
	public WorldGenGenesisLiquids(Block liquid, Block stone)
	{
		this.liquid = liquid;
		this.stone = stone;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		if (world.getBlockState(pos.up()).getBlock() != stone)
			return false;
		
		if (world.getBlockState(pos.down()).getBlock() != stone)
			return false;
		
		IBlockState state = world.getBlockState(pos);
		
		if (state.getMaterial() != Material.AIR && state.getBlock() != stone)
			return false;
		
		int stoneCount = 0;
		int airCount = 0;
		
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			BlockPos sidePos = pos.offset(facing);
			IBlockState sideState = world.getBlockState(sidePos);
			
			if (sideState.getBlock() == stone)
				stoneCount++;
			else if (sideState.getBlock().isAir(sideState, world, sidePos))
				airCount++;
		}
		
		if (stoneCount == 3 && airCount == 1)
		{
			world.setBlockState(pos, liquid.getDefaultState(), 2);
			world.scheduleUpdate(pos, liquid, 0);
		}
		
		return true;
	}
}
