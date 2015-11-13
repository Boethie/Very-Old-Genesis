package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenDesert extends BiomeGenBaseGenesis
{
	public BiomeGenDesert(int id)
	{
		super(id);
		setBiomeName("Desert");
		setTemperatureRainfall(2.0F, 0.0F);
		setDisableRain();
		setHeight(0.125F, 0.05F);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		
		addDecoration(new WorldGenRockBoulders().setRarity(85).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
	}
}
