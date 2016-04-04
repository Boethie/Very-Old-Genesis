package genesis.world.biome;

import genesis.combo.PlantBlocks;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenTreeVoltzia;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenRedLowlandsM extends BiomeGenRedLowlands
{
	public BiomeGenRedLowlandsM(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		//setBiomeName("Red Lowlands M");
		//setHeight(2.2F, 0.4F);
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(WorldGenPlant.create(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.AETHOPHYLLUM).setCountPerChunk(1));
		addDecoration(WorldGenPlant.create(EnumPlant.APOLDIA).setCountPerChunk(1));
		addGrassFlowers();
		
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT)).setWaterRequired(false).setMaxHeight(3).setRarity(3).setCountPerChunk(1));
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeVoltzia(5, 10, true).setTreeCountPerChunk(10));
	}
}
