package genesis.world.biome;

import java.util.Random;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenZygopteris;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class BiomeGenBaseGenesis extends BiomeGenBase
{
	public IBlockState oceanFloor = GenesisBlocks.ooze.getDefaultState();
	
	public BiomeGenBaseGenesis(int id)
	{
		super(id);
		theBiomeDecorator.clayPerChunk = 1;
		topBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST).withProperty(BlockGrass.SNOWY, false);
		spawnableCaveCreatureList.clear();
		spawnableCreatureList.clear();
		spawnableMonsterList.clear();
		spawnableWaterCreatureList.clear();
		waterColorMultiplier = 0xaa791e;
	}
	
	public BiomeDecoratorGenesis getGenesisDecorator()
	{
		return (BiomeDecoratorGenesis) theBiomeDecorator;
	}
	
	@Override
	public BiomeDecoratorGenesis createBiomeDecorator()
    {
        //return getModdedBiomeDecorator(new BiomeDecoratorGenesis());
		return new BiomeDecoratorGenesis();
    }
	
	@Override
	public BiomeGenBaseGenesis setColor(int color)
	{
		super.setColor(color);
		return this;
	}
	
	@Override
	public BiomeGenBaseGenesis setBiomeName(String name)
	{
		super.setBiomeName(name);
		return this;
	}
	
	//TODO: @Override
	public BiomeGenBaseGenesis setBiomeHeight(BiomeGenBase.Height height)
	{
		super.setHeight(height);
		return this;
	}
	
	public Vec3 getSkyColor()
	{
		return new Vec3(0.29411764705882352941176470588235D, 0.47450980392156862745098039215686D, 0.1960784313725490196078431372549D);
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenZygopteris();
	}
	
	@Override
	public final void generateBiomeTerrain(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
    {
        IBlockState top = topBlock;
        IBlockState filler = fillerBlock;
        int k = -1;
        int l = (int)(d / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int chunkX = blockX & 15;
        int chunkZ = blockZ & 15;

        for (int y = 255; y >= 0; --y)
        {
            if (y <= rand.nextInt(5))
            {
                primer.setBlockState(chunkZ, y, chunkX, Blocks.bedrock.getDefaultState());
            }
            else
            {
                IBlockState state = primer.getBlockState(chunkZ, y, chunkX);

                if (state.getBlock().getMaterial() == Material.air)
                {
                    k = -1;
                }
                else if (state.getBlock() == Blocks.stone)
                {
                    if (k == -1)
                    {
                        if (l <= 0)
                        {
                            top = null;
                            filler = Blocks.stone.getDefaultState();
                        }
                        else if (y >= 59 && y <= 64)
                        {
                            top = topBlock;
                            filler = fillerBlock;
                        }

                        if (y < 63 && (top == null || top.getBlock().getMaterial() == Material.air))
                        {
                            if (getFloatTemperature(new BlockPos(blockX, y, blockZ)) < 0.15F)
                            {
                                top = Blocks.ice.getDefaultState();
                            }
                            else
                            {
                                top = Blocks.water.getDefaultState();
                            }
                        }

                        k = l;

                        if (y >= 62)
                        {
                            primer.setBlockState(chunkZ, y, chunkX, top);
                        }
                        else if (y < 56 - l)
                        {
                            top = null;
                            filler = Blocks.stone.getDefaultState();
                            primer.setBlockState(chunkZ, y, chunkX, oceanFloor);
                        }
                        else
                        {
                            primer.setBlockState(chunkZ, y, chunkX, filler);
                        }
                    }
                    else if (k > 0)
                    {
                        --k;
                        primer.setBlockState(chunkZ, y, chunkX, filler);

                        if (k == 0 && filler.getBlock() == Blocks.sand)
                        {
                            k = rand.nextInt(4) + Math.max(0, y - 63);
                            filler = filler.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState();
                        }
                    }
                }
            }
        }
    }
}
