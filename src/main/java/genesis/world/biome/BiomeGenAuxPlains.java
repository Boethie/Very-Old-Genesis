package genesis.world.biome;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenerator;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenArchaeomarasmius;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPhlebopteris;
import genesis.world.biome.decorate.WorldGenGrowingPlant.GrowingPlantType;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenRockBoulders;
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
		this.theBiomeDecorator.grassPerChunk = 1;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).generateDefaultTrees = false;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenArchaeomarasmius().setPatchSize(3).setCountPerChunk(5));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenPalaeoagaracites().setPatchSize(5).setCountPerChunk(10));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPlantType(GrowingPlantType.NORMAL).setPatchSize(5).setCountPerChunk(7));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenRockBoulders().setCountPerChunk(5));
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreeAraucarioxylon(25, 30, true).setTreeCountPerChunk(this.totalTreesPerChunk));
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenPhlebopteris();
	}
}
