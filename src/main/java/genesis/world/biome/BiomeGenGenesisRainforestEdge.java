package genesis.world.biome;

import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenGenesisRainforestEdge extends BiomeGenGenesisRainforest 
{
	public BiomeGenGenesisRainforestEdge(int biomeID) 
	{
		super(biomeID);
		this.theBiomeDecorator.treesPerChunk = 5;
	}

	@Override
	public WorldGenAbstractTree genBigTreeChance(Random rand) 
	{
		int treeType = rand.nextInt(20);
		return treeType < 14 ? new WorldGenTreeLepidodendron(8, 4, true) : treeType < 17 ? new WorldGenTreeSigillaria(7, 3, true) : treeType < 19 ? new WorldGenTreeCordaites(12, 4, true) : new WorldGenTreePsaronius(4, 3, true);
	}
}
