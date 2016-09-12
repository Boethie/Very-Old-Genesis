package genesis.world.biome;

import java.util.Random;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeDeepOceanGenesis extends BiomeGenesis
{
	public BiomeDeepOceanGenesis(BiomeProperties properties)
	{
		super(properties);
		
		topBlock = GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		
		theBiomeDecorator.clayPerChunk = 2;
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.genTerrainBlocks(world, rand, primer, blockX, blockZ, d);
	}
}
