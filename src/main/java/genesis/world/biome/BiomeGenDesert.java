package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;

public class BiomeGenDesert extends BiomeGenBaseGenesis
{
	public BiomeGenDesert(int id)
	{
		super(id);
		setBiomeName("Desert");
		this.temperature = 2.0F;
		setDisableRain();
		setHeight(0.125F, 0.05F);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
	}
}
