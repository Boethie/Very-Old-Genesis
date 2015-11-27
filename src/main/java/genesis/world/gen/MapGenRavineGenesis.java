package genesis.world.gen;

import genesis.block.BlockMoss;
import genesis.common.GenesisBiomes;
import genesis.common.GenesisBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenRavine;

public class MapGenRavineGenesis extends MapGenRavine
{
	protected boolean isExceptionBiome(BiomeGenBase biome)
	{
		if (biome == GenesisBiomes.genesisBeach) return true;
		return false;
	}
	
	@Override
	protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop)
	{
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
		IBlockState state = data.getBlockState(x, y, z);
		IBlockState top = isExceptionBiome(biome) ? GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST) : biome.topBlock;
		IBlockState filler = isExceptionBiome(biome) ? Blocks.dirt.getDefaultState() : biome.fillerBlock;
		
		if (state.getBlock() == GenesisBlocks.granite || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock())
		{
			if (y < 7)
			{
				data.setBlockState(x, y, z, GenesisBlocks.komatiitic_lava.getDefaultState());
			}
			else
			{
				if (state.getBlock() != GenesisBlocks.komatiitic_lava)
					data.setBlockState(x, y, z, Blocks.air.getDefaultState());
				
				if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler.getBlock())
				{
					data.setBlockState(x, y - 1, z, top.getBlock().getDefaultState());
				}
			}
		}
	}
}
