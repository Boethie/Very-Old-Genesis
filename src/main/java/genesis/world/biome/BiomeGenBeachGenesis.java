package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumPlant;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenPlant;

public class BiomeGenBeachGenesis extends BiomeGenBaseGenesis
{
	public BiomeGenBeachGenesis (int id)
	{
		super(id);
		setBiomeName("Beach");
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		
		theBiomeDecorator.grassPerChunk = 0;
		
		addDecorations();
	}
		
		protected void addDecorations()
		{
			addDecoration(new WorldGenPebbles().setCountPerChunk(25));
			
			addDecoration(new WorldGenPlant(EnumPlant.LEPACYCLOTES).setNextToWater(true).addAllowedBlocks(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT), GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT)).setCountPerChunk(12).setPatchSize(4));
		}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
