package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumPlant;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenArchaeopterisForest extends BiomeGenBaseGenesis
{
	public BiomeGenArchaeopterisForest(int id)
	{
		super(id);
		setBiomeName("Archaeopteris Forest");
		setTemperatureRainfall(1.15F, 1.0F);
		setHeight(0.01F, 0.02F);
		theBiomeDecorator.grassPerChunk = 1;
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(3).setCountPerChunk(3));
		
		addTree(new WorldGenTreeArchaeopteris(15, 20, true).setTreeCountPerChunk(10));
		addTree(new WorldGenRottenLog(3, 6, EnumTree.ARCHAEOPTERIS, true).setTreeCountPerChunk(5));
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getBlock(EnumPlant.PSILOPHYTON).getDefaultState()).setVolume(64);
	}
}
