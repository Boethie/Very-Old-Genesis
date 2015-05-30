package genesis.world.biome;

public class BiomeGenAuxForestEdge extends BiomeGenAuxForest
{

	public BiomeGenAuxForestEdge(int id)
	{
		super(id);
		this.theBiomeDecorator.treesPerChunk = 5;
		this.biomeName = "Araucarioxylon Forest Edge";
	}

}
