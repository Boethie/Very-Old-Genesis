package genesis.world.biome;

import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenOdontopteris;
import genesis.world.biome.decorate.WorldGenSphenophyllum;
import genesis.world.biome.decorate.WorldGenZygopteris;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenRainforest extends BiomeGenBaseGenesis
{
	public int totalTreesPerChunk = 1100;
	
	public BiomeGenRainforest(int id)
	{
		super(id);
		this.biomeName = "Rainforest";
		this.rainfall = 1.0F;
		this.temperature = 0.9F;
		this.minHeight = 0.05F;
		this.maxHeight = 0.1F;
		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 3;
		((BiomeDecoratorGenesis) this.theBiomeDecorator).generateDefaultTrees = false;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenOdontopteris().setCountPerChunk(60));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenSphenophyllum().setCountPerChunk(150));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreeLepidodendron(14, 18, true).setTreeCountPerChunk(MathHelper.ceiling_float_int((float)totalTreesPerChunk * 0.6F)));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreeSigillaria(10, 15, true).setTreeCountPerChunk(MathHelper.ceiling_float_int((float)totalTreesPerChunk * 0.3F)));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenTreePsaronius(5, 8, true).setTreeCountPerChunk(MathHelper.ceiling_float_int((float)totalTreesPerChunk * 0.1F)));
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenRottenLog(3, 6, EnumTree.LEPIDODENDRON, true).setTreeCountPerChunk(80));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenRottenLog(3, 6, EnumTree.SIGILLARIA, true).setTreeCountPerChunk(80));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).trees.add(new WorldGenRottenLog(3, 6, EnumTree.CORDAITES, true).setTreeCountPerChunk(80));
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenZygopteris();
	}
}
