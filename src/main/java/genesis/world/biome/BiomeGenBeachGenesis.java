package genesis.world.biome;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenPlant;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenBeachGenesis extends BiomeGenBaseGenesis
{
	public BiomeGenBeachGenesis (BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		
		theBiomeDecorator.grassPerChunk = 0;
		
		addDecorations();
	}
		
		protected void addDecorations()
		{
			addDecoration(new WorldGenPebbles().setCountPerChunk(25));
			
			addDecoration(WorldGenPlant.create(EnumPlant.LEPACYCLOTES).setNextToWater(true).addAllowedBlocks(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT), GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT)).setCountPerChunk(10).setPatchSize(4));
			addGrassFlowers();
		}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
