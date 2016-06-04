package genesis.world.biome;

import genesis.combo.PlantBlocks;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenTreeBjuvia;
import genesis.world.gen.feature.WorldGenTreeVoltzia;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenRedDesert extends BiomeGenBaseGenesis
{
	public BiomeGenRedDesert(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		
		theBiomeDecorator.sandPerChunk = 0;
		theBiomeDecorator.sandPerChunk2 = 0;
		
		getDecorator().setGrassCount(0.15F);
		addGrass(WorldGenPlant.create(EnumPlant.WACHTLERIA).setPatchCount(4), 1);
		
		getDecorator().setFlowerCount(0.5F);
		addFlower(WorldGenPlant.create(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.AETHOPHYLLUM), 1);
		addFlower(WorldGenPlant.create(EnumPlant.APOLDIA), 4);
		
		addDecoration(new WorldGenRoots(), 5);
		addDecoration(new WorldGenPebbles().setWaterRequired(false), 5);
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT)).setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1F)), 0.4F);

		getDecorator().setTreeCount(0.4F);
		addTree(new WorldGenTreeBjuvia(4, 6, true), 16);
		addTree(new WorldGenTreeVoltzia(4, 8, true), 52);
	}
	
	@Override
	public float getFogDensity()
	{
		return 0.75F;
	}
	
	/*@Override
	public Vec3d getFogColor()
	{
		float red = 0.917647059F;
		float green = 0.650980392F;
		float blue = 0.309803922F;
		
		return new Vec3d(red, green, blue);
	}*/
	
	@Override
	public float getNightFogModifier()
	{
		return 0.25F;
	}
}
