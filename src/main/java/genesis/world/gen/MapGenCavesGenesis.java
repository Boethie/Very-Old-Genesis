package genesis.world.gen;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenCaves;

public class MapGenCavesGenesis extends MapGenCaves
{
	@Override
	protected boolean func_175793_a(IBlockState state, IBlockState up)
	{
		return state.getBlock() == GenesisBlocks.granite || state.getBlock() == Blocks.dirt || state.getBlock() == GenesisBlocks.moss || state.getBlock() == Blocks.hardened_clay || state.getBlock() == Blocks.stained_hardened_clay || state.getBlock() == Blocks.sandstone || state.getBlock() == Blocks.red_sandstone || state.getBlock() == Blocks.mycelium || state.getBlock() == Blocks.snow_layer || (state.getBlock() == Blocks.sand || state.getBlock() == Blocks.gravel) && up.getBlock().getMaterial() != Material.water;
	}
	
	@Override
	protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop, IBlockState state, IBlockState up)
	{
		net.minecraft.world.biome.BiomeGenBase biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
		IBlockState top = biome.topBlock;
		IBlockState filler = biome.fillerBlock;
		
		if (func_175793_a(state, up) || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock())
		{
			if (y < 7)
			{
				data.setBlockState(x, y, z, GenesisBlocks.komatiitic_lava.getDefaultState());
			}
			else
			{
				data.setBlockState(x, y, z, Blocks.air.getDefaultState());
				
				if (y > 55)
				{
					if (GenesisBlocks.silt.isStateOf(filler, SiltBlocks.SILT, EnumSilt.SILT))
					{
						data.setBlockState(x, y + 1, z, GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.SILT));
					} else if (GenesisBlocks.silt.isStateOf(filler, SiltBlocks.SILT, EnumSilt.RED_SILT))
					{
						data.setBlockState(x, y + 1, z, GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT));
					}
				}
				
				if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler.getBlock())
				{
					data.setBlockState(x, y - 1, z, top);
				}
			}
		}
	}
}
