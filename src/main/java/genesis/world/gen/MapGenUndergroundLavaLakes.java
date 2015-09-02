package genesis.world.gen;

import java.util.Random;

import genesis.common.GenesisBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
	
	private float[] column = new float[1024];
	
    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop, boolean column)
    {
    	BlockPos pos = new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16);
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(pos);
        IBlockState state = data.getBlockState(x, y, z);
        IBlockState top = biome.topBlock;
        IBlockState filler = biome.fillerBlock;
        
        if (y > lakeBottom + lakeHeight || y < lakeBottom)
        	return;
        
        if (column)
    	{
    		data.setBlockState(x, y, z, GenesisBlocks.komatiite.getDefaultState());
    	}
        else if (
        		state.getBlock() == GenesisBlocks.granite 
        		|| state.getBlock() == top.getBlock() 
        		|| state.getBlock() == filler.getBlock())
        {
        	if (y < lavaLevel)
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
    
    public void func_175792_a(IChunkProvider chunkProvider, World world, int chunkX, int chunkZ, ChunkPrimer chunkPrimer)
    {
        int k = this.range;
        this.worldObj = world;
        this.rand.setSeed(world.getSeed());
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();
        
        for (int blockX = chunkX - k; blockX <= chunkX + k; ++blockX)
        {
            for (int blockZ = chunkZ - k; blockZ <= chunkZ + k; ++blockZ)
            {
                long l1 = (long)blockX * l;
                long i2 = (long)blockZ * i1;
                this.rand.setSeed(l1 ^ i2 ^ world.getSeed());
                this.generateInChunk(world, blockX, blockZ, chunkX, chunkZ, chunkPrimer);
            }
        }
    }
    
    protected void generateInChunk(World worldIn, int blockX, int blockZ, int chunkX, int chunkZ, ChunkPrimer chunkPrimer)
    {
        if (this.rand.nextInt(30) == 0)
        {
        	lakeBottom = 1 + rand.nextInt(3);
    		lakeHeight = 11 + rand.nextInt(3);
    		
    		lavaLevel = 7;
    		
            double x1 = (double)(blockX * 16 + this.rand.nextInt(16));
            double y1 = (double)lakeBottom;
            double z1 = (double)(blockZ * 16 + this.rand.nextInt(16));
            byte b0 = 1;
            
            for (int i1 = 0; i1 < b0; ++i1)
            {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
                this.generateArea(this.rand.nextLong(), chunkX, chunkZ, chunkPrimer, x1, y1, z1, f2, f, f1, 0, 0, 3.0D);
            }
        }
    }
    
    protected void generateArea(long seed, int chunkX, int chunkZ, ChunkPrimer chunkPrimer, double x1, double y1, double z1, float p_180707_12_, float p_180707_13_, float p_180707_14_, int p_180707_15_, int p_180707_16_, double p_180707_17_)
    {
        Random random = new Random(seed);
        double d4 = (double)(chunkX * 16 + 8);
        double d5 = (double)(chunkZ * 16 + 8);
        float f3 = 0.0F;
        float f4 = 0.0F;
        
        if (p_180707_16_ <= 0)
        {
            int j1 = this.range * 16 - 16;
            p_180707_16_ = j1 - random.nextInt(j1 / 4);
        }
        
        boolean flag1 = false;
        
        if (p_180707_15_ == -1)
        {
            p_180707_15_ = p_180707_16_ / 2;
            flag1 = true;
        }
        
        float f5 = 1.0F;
        
        for (int k1 = 0; k1 < 256; ++k1)
        {
            if (k1 == 0 || random.nextInt(3) == 0)
            {
                f5 = 1.0F + random.nextFloat() * random.nextFloat() * 1.0F;
            }
            
            this.column[k1] = f5 * f5;
        }
        
        for (; p_180707_15_ < p_180707_16_; ++p_180707_15_)
        {
            double d13 = 2.5D + (double)(MathHelper.sin((float)p_180707_15_ * (float)Math.PI / (float)p_180707_16_) * p_180707_12_ * 6.0F);
            double d6 = d13 * p_180707_17_;
            d13 *= (double)random.nextFloat() * 0.25D + 0.75D;
            d6 *= (double)random.nextFloat() * 0.25D + 0.75D;
            float f6 = MathHelper.cos(p_180707_14_);
            float f7 = MathHelper.sin(p_180707_14_);
            x1 += (double)(MathHelper.cos(p_180707_13_) * f6);
            y1 += (double)f7;
            z1 += (double)(MathHelper.sin(p_180707_13_) * f6);
            p_180707_14_ *= 0.7F;
            p_180707_14_ += f4 * 0.05F;
            p_180707_13_ += f3 * 0.05F;
            f4 *= 0.8F;
            f3 *= 0.5F;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
            
            if (flag1 || random.nextInt(4) != 0)
            {
                double d7 = x1 - d4;
                double d8 = z1 - d5;
                double d9 = (double)(p_180707_16_ - p_180707_15_);
                double d10 = (double)(p_180707_12_ + 2.0F + 16.0F);
                
                if (d7 * d7 + d8 * d8 - d9 * d9 > d10 * d10)
                {
                    return;
                }
                
                if (x1 >= d4 - 16.0D - d13 * 2.0D && z1 >= d5 - 16.0D - d13 * 2.0D && x1 <= d4 + 16.0D + d13 * 2.0D && z1 <= d5 + 16.0D + d13 * 2.0D)
                {
                    int k3 = MathHelper.floor_double(x1 - d13) - chunkX * 16 - 1;
                    int l1 = MathHelper.floor_double(x1 + d13) - chunkX * 16 + 1;
                    int l3 = MathHelper.floor_double(y1 - d6) - 1;
                    int i2 = MathHelper.floor_double(y1 + d6) + 1;
                    int i4 = MathHelper.floor_double(z1 - d13) - chunkZ * 16 - 1;
                    int j2 = MathHelper.floor_double(z1 + d13) - chunkZ * 16 + 1;
                    
                    if (k3 < 0)
                    {
                        k3 = 0;
                    }
                    
                    if (l1 > 16)
                    {
                        l1 = 16;
                    }
                    
                    if (l3 < 1)
                    {
                        l3 = 1;
                    }
                    
                    if (i2 > lakeBottom + lakeHeight)
                    	i2 = lakeBottom + lakeHeight;
                    
                    if (i4 < 0)
                    {
                        i4 = 0;
                    }
                    
                    if (j2 > 16)
                    {
                        j2 = 16;
                    }
                    
                    int k2;
                    
                    {
                        for (k2 = k3; k2 < l1; ++k2)
                        {
                            double d14 = ((double)(k2 + chunkX * 16) + 0.5D - x1) / d13;

                            for (int j4 = i4; j4 < j2; ++j4)
                            {
                                double d11 = ((double)(j4 + chunkZ * 16) + 0.8D - z1) / d13;
                                boolean flag = false;
                                
                                if (d14 * d14 + d11 * d11 < 1.0D)
                                {
                                    for (int j3 = i2; j3 > l3; --j3)
                                    {
                                        double d12 = ((double)(j3 - 1) + 0.5D - y1) / d6;

                                        if ((d14 * d14 + d11 * d11) * (double)this.column[j3 - 1] + d12 * d12 / 6.0D < 1.0D)
                                        {
                                            if (isTopBlock(chunkPrimer, k2, j3, j4, chunkX, chunkZ))
                                            {
                                                flag = true;
                                            }
                                            
                                            digBlock(chunkPrimer, k2, j3, j4, chunkX, chunkZ, flag, false);
                                        }
                                    }
                                }
                                /*
                                if (this.rand.nextInt(200) == 0 && this.rand.nextInt(180) == 0)
                                {
                                	for (int c = 2; c <= 18; c++)
                                		digBlock(chunkPrimer, k2, c, j4, chunkX, chunkZ, flag, true);
                                }*/
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected boolean isOceanBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ)
    {
        Block block = data.getBlockState(x, y, z).getBlock();
        return block == Blocks.flowing_water || block == Blocks.water;
    }
    
    private boolean isTopBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ)
    {
        net.minecraft.world.biome.BiomeGenBase biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        IBlockState state = data.getBlockState(x, y, z);
        return state.getBlock() == biome.topBlock;
    }
}
