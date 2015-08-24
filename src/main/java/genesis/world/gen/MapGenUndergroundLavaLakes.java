package genesis.world.gen;

import genesis.common.GenesisBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;

public class MapGenUndergroundLavaLakes extends MapGenBase
{
	private int lavaLevel;
	private int lakeBottom;
	private int lakeHeight;
	
	public MapGenUndergroundLavaLakes()
	{
		lakeBottom = 10 + rand.nextInt(15);
		lakeHeight = 8 + rand.nextInt(8);
		
		int lev = Math.max((lakeHeight / 2) - 4, 1);
		
		lavaLevel = lakeBottom + 4 + rand.nextInt(lev);
	}
	
    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop)
    {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        IBlockState state = data.getBlockState(x, y, z);
        IBlockState top = biome.topBlock;
        IBlockState filler = biome.fillerBlock;
        
        if (state.getBlock() == GenesisBlocks.granite || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock())
        {
            if (y < lavaLevel)
            {
                data.setBlockState(x, y, z, GenesisBlocks.komatiitic_lava.getDefaultState());
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
    
    @Override
    public void func_175792_a(IChunkProvider chunkProvider, World world, int x, int z, ChunkPrimer chunkPrimer)
    {
        int k = this.range;
        this.worldObj = world;
        this.rand.setSeed(world.getSeed());
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();

        for (int j1 = x - k; j1 <= x + k; ++j1)
        {
            for (int k1 = z - k; k1 <= z + k; ++k1)
            {
                long l1 = (long)j1 * l;
                long i2 = (long)k1 * i1;
                this.rand.setSeed(l1 ^ i2 ^ world.getSeed());
                this.func_180701_a(world, j1, k1, x, z, chunkPrimer);
            }
        }
    }
    
    @Override
    protected void func_180701_a(World world, int p_180701_2_, int p_180701_3_, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimer)
    {
    	;
    }
}
