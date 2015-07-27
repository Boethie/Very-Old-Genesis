package genesis.world.biome;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenPebbles;

public class BiomeGenBeachGenesis extends BiomeGenBaseGenesis
{
	public BiomeGenBeachGenesis (int id)
	{
		super(id);
		this.biomeName = "Beach";
		this.fillerBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST);
		this.minHeight = 0.05F;
		this.maxHeight = 0.1F;
		this.setHeight(height_Shores);
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenPebbles().setCountPerChunk(25));
	}
}
