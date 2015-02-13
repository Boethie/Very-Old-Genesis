package genesis.world;

import genesis.block.BlockGenesisRock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenCaves;

public class MapGenCavesGenesis extends MapGenCaves 
{
	/**
	 * Digs out the current block, default implementation removes stone, filler, and top block
	 * Sets the block to lava if y is less then 10, and air other wise.
	 * If setting to air, it also checks to see if we've broken the surface and if so
	 * tries to make the floor the biome's top block
	 *
	 * @param data Block data array
	 * @param index Pre-calculated index into block data
	 * @param x local X position
	 * @param y local Y position
	 * @param z local Z position
	 * @param chunkX Chunk X position
	 * @param chunkZ Chunk Y position
	 * @param foundTop True if we've encountered the biome's top block. Ideally if we've broken the surface.
	 */
	@Override
	protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop, IBlockState state, IBlockState up)
	{
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
		IBlockState top = biome.topBlock;
		IBlockState filler = biome.fillerBlock;

		if (state.getBlock() instanceof BlockGenesisRock || state.getBlock() == filler.getBlock() || state.getBlock() == top.getBlock()) 
		{
			if (y < 9) 
			{
				data.setBlockState(x, y, z, Blocks.flowing_lava.getDefaultState()); //TODO: Change this to komatiitic lava
			} 
			else 
			{
				data.setBlockState(x, y, z, Blocks.air.getDefaultState());

				if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler.getBlock()) 
				{
					data.setBlockState(x, y - 1, z, top.getBlock().getDefaultState());
				}
			}
		}
	}
}
