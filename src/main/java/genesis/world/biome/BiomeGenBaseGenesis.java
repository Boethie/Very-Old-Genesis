package genesis.world.biome;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import net.minecraft.block.BlockGrass;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class BiomeGenBaseGenesis extends BiomeGenBase
{
	public BiomeGenBaseGenesis(int id)
	{
		super(id);
		this.theBiomeDecorator = new BiomeDecoratorGenesis();
		this.theBiomeDecorator.clayPerChunk = 1;
		this.topBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST).withProperty(BlockGrass.SNOWY, false);
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.waterColorMultiplier = 0xaa791e;
	}
}
