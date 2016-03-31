package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenLimestoneBeach extends BiomeGenBaseGenesis
{
	public BiomeGenLimestoneBeach (BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		topBlock = GenesisBlocks.limestone.getDefaultState();
		fillerBlock = GenesisBlocks.limestone.getDefaultState();
		
		theBiomeDecorator.grassPerChunk = 0;
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(25));
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
