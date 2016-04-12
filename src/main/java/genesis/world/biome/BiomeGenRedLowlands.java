package genesis.world.biome;

import genesis.combo.*;
import genesis.combo.variant.*;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.*;
import genesis.world.gen.feature.*;

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
		
		theBiomeDecorator.sandPerChunk = 0;
		theBiomeDecorator.sandPerChunk2 = 0;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		getDecorator().setGrassCount(8);
		addGrass(WorldGenPlant.create(EnumPlant.WACHTLERIA).setPatchCount(4), 1);
		
		getDecorator().setFlowerCount(5);
		addFlower(WorldGenPlant.create(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.AETHOPHYLLUM), 3);
		addFlower(WorldGenPlant.create(EnumPlant.APOLDIA), 2);
		
		addDecoration(new WorldGenRoots(), 5);
		addDecoration(new WorldGenPebbles().setWaterRequired(false), 1);
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT)).setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1F)), 0.25F);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(10.3F);
		addTree(new WorldGenTreeBjuvia(4, 6, true), 2);
		addTree(new WorldGenTreeVoltzia(5, 10, true), 100);
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
