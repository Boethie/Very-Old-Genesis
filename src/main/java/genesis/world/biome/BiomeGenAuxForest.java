package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenArchaeomarasmius;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenGrowingPlant.GrowingPlantType;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenPhlebopteris;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenAuxForest extends BiomeGenBaseGenesis
{
	public int totalTreesPerChunk = 5;
	
	public BiomeGenAuxForest(int id)
	{
		super(id);
		this.biomeName = "Araucarioxylon Forest";
		this.rainfall = 1.0F;
		this.temperature = 0.9F;
		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 5;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).generateDefaultTrees = false;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenArchaeomarasmius().setPatchSize(3).setCountPerChunk(5));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenPalaeoagaracites().setPatchSize(10).setCountPerChunk(16));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPlantType(GrowingPlantType.NORMAL).setPatchSize(5).setCountPerChunk(5));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenRockBoulders().setCountPerChunk(5));
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreeAraucarioxylon(25, 30, true).setTreeCountPerChunk(totalTreesPerChunk));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenRottenLog(3, 7, EnumTree.ARAUCARIOXYLON, true).addTopDecoration(GenesisBlocks.archaeomarasmius.getDefaultState()).setTreeCountPerChunk(3));
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenPhlebopteris();
	}
}
