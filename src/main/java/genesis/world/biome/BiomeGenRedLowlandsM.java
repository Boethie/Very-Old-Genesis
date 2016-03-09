package genesis.world.biome;

import genesis.combo.PlantBlocks;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenTreeVoltzia;

public class BiomeGenRedLowlandsM extends BiomeGenRedLowlands
{
	public BiomeGenRedLowlandsM(int id)
	{
		super(id);
		setBiomeName("Red Lowlands M");
		setHeight(2.2F, 0.4F);
	}
	
	protected void addDecorations()
	{
		addDecoration(WorldGenPlant.create(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.AETHOPHYLLUM).setCountPerChunk(1));
		addDecoration(WorldGenPlant.create(EnumPlant.APOLDIA).addAllowedBlocks(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT), GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.RED_SILT)).setCountPerChunk(1));
		addGrassFlowers();
		
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(3).addBlocks(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT)).setCountPerChunk(1));
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeVoltzia(5, 10, true).setTreeCountPerChunk(10));
	}
}
