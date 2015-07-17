package genesis.world.biome;

import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenPhlebopteris;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenAuxForest extends BiomeGenBaseGenesis
{
	public BiomeGenAuxForest(int id)
	{
		super(id);
		this.biomeName = "Araucarioxylon Forest";
		
		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 5;
		((BiomeDecoratorGenesis) this.theBiomeDecorator).generateDefaultTrees = false;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreeAraucarioxylon(25, 30, true).setTreeCountPerChunk(400));
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenPhlebopteris();
	}
}
