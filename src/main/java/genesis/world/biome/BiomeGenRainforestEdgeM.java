package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;


public class BiomeGenRainforestEdgeM extends BiomeGenRainforestEdge
{
	public BiomeGenRainforestEdgeM(int id)
	{
		super(id);
		setBiomeName("Rainforest Edge M");
		setHeight(0.4F, 1.1F);
	}
}
