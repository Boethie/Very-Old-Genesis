package genesis.world.biome;

public class BiomeGenShallowOcean extends BiomeGenBaseGenesis
{

	public BiomeGenShallowOcean(int id)
	{
		super(id);
		this.biomeName = "Shallow Ocean";
		this.minHeight = -.8F;
		this.maxHeight = 0.1F;
		this.waterColorMultiplier = 0x008d49;
	}

}
