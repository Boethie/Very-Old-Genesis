package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumAquaticPlant;
import genesis.entity.living.IEntityPreferredBiome;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenBoulders;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeRiver extends BiomeGenesis implements IEntityPreferredBiome
{
	public BiomeRiver(Biome.BiomeProperties properties)
	{
		super(properties);
		
		addDecoration(new WorldGenBoulders(0, 0.2F, 0.65F).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.25F, 0.75F)), 1.35F);
		addDecoration(new WorldGenPebbles(), 25);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.BANGIOMORPHA).setPatchCount(9), 1.5F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARPOLIA).setPatchCount(9), 0.75F);
		addDecoration(new WorldGenMossStages(), 30);
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
