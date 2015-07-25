package genesis.world.biome;

import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;

public class BiomeGenArchaeopterisForest extends BiomeGenBaseGenesis
{
	public int totalTreesPerChunk = 20;
	
	public BiomeGenArchaeopterisForest(int id)
	{
		super(id);
		
		this.biomeName = "Archaeopteris Rainforest";
		this.rainfall = 1.0F;
		this.temperature = 0.8F;
		this.minHeight = 0.03F;
		this.maxHeight = 0.04F;
		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 3;
		((BiomeDecoratorGenesis) this.theBiomeDecorator).generateDefaultTrees = false;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreeArchaeopteris(15, 20, true).setTreeCountPerChunk(totalTreesPerChunk));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenRottenLog(3, 6, EnumTree.ARCHAEOPTERIS, true).setTreeCountPerChunk(30));
	}
}
