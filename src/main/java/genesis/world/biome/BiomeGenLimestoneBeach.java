package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenPebbles;

public class BiomeGenLimestoneBeach extends BiomeGenBaseGenesis
{
	public BiomeGenLimestoneBeach (int id)
	{
		super(id);
		this.biomeName = "Limestone Beach";
		this.topBlock = GenesisBlocks.limestone.getDefaultState();
		this.fillerBlock = GenesisBlocks.limestone.getDefaultState();
		this.minHeight = 0.05F;
		this.maxHeight = 0.1F;
		this.setHeight(height_Shores);
		this.setColor(10658436);
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenPebbles().setCountPerChunk(25));
	}
}
