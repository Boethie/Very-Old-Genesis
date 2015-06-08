package genesis.world.biome;

import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenZygopteris;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenRainforest extends BiomeGenBaseGensis
{

	public BiomeGenRainforest(int id)
	{
		super(id);
		this.biomeName = "Rainforest";
		this.rainfall = 1.0F;
		this.minHeight = 0.05F;
		this.maxHeight = .1F;
		this.theBiomeDecorator.treesPerChunk = 10;
		this.theBiomeDecorator.grassPerChunk = 2;
		((BiomeDecoratorGenesis) this.theBiomeDecorator).odontopterisPerChunk = 10;
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenZygopteris();
	}

}
