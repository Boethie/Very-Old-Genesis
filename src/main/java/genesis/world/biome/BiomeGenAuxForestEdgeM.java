package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenArchaeomarasmius;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

public class BiomeGenAuxForestEdgeM extends BiomeGenAuxForestEdge
{
	public BiomeGenAuxForestEdgeM(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest Edge M");
		setHeight(0.4F, 0.9F);
	}
}
