package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumAquaticPlant;
import genesis.entity.living.IEntityPreferredBiome;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenRiver extends BiomeGenBaseGenesis implements IEntityPreferredBiome
{
	public BiomeGenRiver(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		theBiomeDecorator.grassPerChunk = 0;
		
		int[] rarityScale = {30, 20, 10, 5, 2};
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(40));
		addDecoration(new WorldGenRockBoulders().setCountPerChunk(6));
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.BANGIOMORPHA).setGenerateInGroup(true, 6).setCountPerChunk(rarityScale[2]));
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARPOLIA).setGenerateInGroup(true, 6).setCountPerChunk(rarityScale[3]));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.genTerrainBlocks(world, rand, primer, blockX, blockZ, d);
	}

	@Override
	public boolean shouldEntityPreferBiome(EntityLivingBase entity)
	{
		return true;
	}
}
