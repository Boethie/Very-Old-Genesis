package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenLiquidsGenesis extends WorldGenerator
{
	public final Block liquid;
	public final Block stone;
	
	public WorldGenLiquidsGenesis(Block liquid)
	{
		this(liquid, GenesisBlocks.granite);
	}
	
	public WorldGenLiquidsGenesis(Block liquid, Block stone)
	{
		this.liquid = liquid;
		this.stone = stone;
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		if (worldIn.getBlockState(position.up()).getBlock() != stone)
		{
			return false;
		}
		else if (worldIn.getBlockState(position.down()).getBlock() != stone)
		{
			return false;
		}
		else if (worldIn.getBlockState(position).getBlock().getMaterial() != Material.air && worldIn.getBlockState(position).getBlock() != stone)
		{
			return false;
		}
		else
		{
			int i = 0;

			if (worldIn.getBlockState(position.west()).getBlock() == stone)
			{
				++i;
			}

			if (worldIn.getBlockState(position.east()).getBlock() == stone)
			{
				++i;
			}

			if (worldIn.getBlockState(position.north()).getBlock() == stone)
			{
				++i;
			}

			if (worldIn.getBlockState(position.south()).getBlock() == stone)
			{
				++i;
			}

			int j = 0;

			if (worldIn.isAirBlock(position.west()))
			{
				++j;
			}

			if (worldIn.isAirBlock(position.east()))
			{
				++j;
			}

			if (worldIn.isAirBlock(position.north()))
			{
				++j;
			}

			if (worldIn.isAirBlock(position.south()))
			{
				++j;
			}

			if (i == 3 && j == 1)
			{
				worldIn.setBlockState(position, liquid.getDefaultState(), 2);
				worldIn.forceBlockUpdateTick(liquid, position, rand);
			}

			return true;
		}
	}
}
