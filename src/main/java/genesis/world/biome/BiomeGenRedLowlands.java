package genesis.world.biome;

import java.util.Random;

import genesis.combo.PlantBlocks;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenTreeBjuvia;
import genesis.world.gen.feature.WorldGenTreeVoltzia;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenRedLowlands extends BiomeGenBaseGenesis
{
	public BiomeGenRedLowlands(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		//setBiomeName("Red Lowlands");
		//setTemperatureRainfall(2.0F, 0.0F);
		//setDisableRain();
		//setHeight(0.001F, 0.125F);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		
		theBiomeDecorator.grassPerChunk = 8;
		theBiomeDecorator.sandPerChunk = 0;
		theBiomeDecorator.sandPerChunk2 = 0;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(WorldGenPlant.create(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.AETHOPHYLLUM).setCountPerChunk(3));
		addDecoration(WorldGenPlant.create(EnumPlant.APOLDIA).addAllowedBlocks(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT), GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.RED_SILT)).setCountPerChunk(2));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
		addGrassFlowers();
		
		addDecoration(new WorldGenPebbles().setWaterRequired(false).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).addBlocks(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT)).setRarity(4).setCountPerChunk(1));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeBjuvia(4, 6, true).setTreeCountPerChunk(2).setRarity(10));
		addTree(new WorldGenTreeVoltzia(5, 10, true).setTreeCountPerChunk(10));
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getPlantBlockState(EnumPlant.WACHTLERIA)).setVolume(24);
	}
	
	@Override
	public float getFogDensity()
	{
		return 0.75F;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		float red = 0.917647059F;
		float green = 0.650980392F;
		float blue = 0.309803922F;
		
		return new Vec3d(red, green, blue);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.25F;
	}
}
