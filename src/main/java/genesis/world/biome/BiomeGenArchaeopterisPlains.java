package genesis.world.biome;

import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;

public class BiomeGenArchaeopterisPlains extends BiomeGenBaseGenesis
{
	public int totalTreesPerChunk = 1;
	
	public BiomeGenArchaeopterisPlains (int id)
	{
		super(id);
		this.biomeName = "Archaeopteris Plains";
		this.rainfall = 1.0F;
		this.temperature = 1.15F;
		this.minHeight = 0.01F;
		this.maxHeight = 0.02F;
		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 0;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).generateDefaultTrees = false;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreeArchaeopteris(15, 25, true).setTreeCountPerChunk(this.totalTreesPerChunk));
	}
}
