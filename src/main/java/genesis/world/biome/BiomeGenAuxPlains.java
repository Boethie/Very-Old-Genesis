package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenArchaeomarasmius;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenGrowingPlant.GrowingPlantType;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

public class BiomeGenAuxPlains extends BiomeGenBaseGenesis
{
	public int totalTreesPerChunk = 1;
	
	public BiomeGenAuxPlains(int id)
	{
		super(id);
		this.biomeName = "Araucarioxylon Plains";
		this.rainfall = 1.0F;
		this.temperature = 0.9F;
		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 5;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).generateDefaultTrees = false;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenArchaeomarasmius().setPatchSize(3).setCountPerChunk(5));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenPalaeoagaracites().setPatchSize(5).setCountPerChunk(10));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPlantType(GrowingPlantType.NORMAL).setPatchSize(5).setCountPerChunk(5));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenRockBoulders().setCountPerChunk(5));
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreeAraucarioxylon(25, 30, true).setTreeCountPerChunk(this.totalTreesPerChunk));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenRottenLog(3, 7, EnumTree.ARAUCARIOXYLON, true).setTreeCountPerChunk(1));
	}
}
