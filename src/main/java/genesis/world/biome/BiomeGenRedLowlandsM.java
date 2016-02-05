package genesis.world.biome;

import net.minecraft.init.Blocks;
import genesis.combo.PlantBlocks;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenTreeVoltzia;

public class BiomeGenRedLowlandsM extends BiomeGenRedLowlands
{
	public BiomeGenRedLowlandsM(int id)
	{
		super(id);
		setBiomeName("Red Lowlands M");
		setHeight(0.4F, 0.5F);
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenMossStages().addAllowedBlocks(GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT), GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT), Blocks.dirt.getDefaultState()).setCountPerChunk(53).setPatchSize(27));
		
		addDecoration(new WorldGenPlant(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.AETHOPHYLLUM).setCountPerChunk(1));
		addDecoration(new WorldGenPlant(EnumPlant.APOLDIA).addAllowedBlocks(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT), GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.RED_SILT)).setCountPerChunk(1));
		
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(4).addBlocks(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT)).setCountPerChunk(3));
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeVoltzia(5, 10, true).setTreeCountPerChunk(12).setRarity(2));
	}
}
