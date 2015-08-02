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
	public int totalTreesPerChunk = 10;
	
	public BiomeGenArchaeopterisForest(int id)
	{
		super(id);
		this.biomeName = "Archaeopteris Forest";
		this.rainfall = 1.0F;
		this.temperature = 1.15F;
		this.minHeight = 0.01F;
		this.maxHeight = 0.02F;
		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 1;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).generateDefaultTrees = false;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(3).setCountPerChunk(3));
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreeArchaeopteris(15, 20, true).setTreeCountPerChunk(totalTreesPerChunk));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenRottenLog(3, 6, EnumTree.ARCHAEOPTERIS, true).setTreeCountPerChunk(5));
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getBlockState(EnumPlant.PSILOPHYTON)).setVolume(64);
	}
}
