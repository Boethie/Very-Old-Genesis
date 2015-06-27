package genesis.world.biome;

public class BiomeGenAuxForest extends BiomeGenBaseGenesis
{

	public BiomeGenAuxForest(int id)
	{
		super(id);
		this.theBiomeDecorator.treesPerChunk = 5;
		this.theBiomeDecorator.grassPerChunk = 0;
		this.biomeName = "Araucarioxylon Forest";
	}

}
