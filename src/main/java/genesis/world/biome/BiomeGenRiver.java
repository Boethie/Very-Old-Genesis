package genesis.world.biome;

import java.util.Random;

import genesis.entity.living.IEntityPreferredBiome;
import genesis.metadata.EnumAquaticPlant;
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
	public BiomeGenRiver(int id)
	{
		super(id);
		setBiomeName("River");
		setHeight(BiomeGenBase.height_ShallowWaters);
		
		theBiomeDecorator.grassPerChunk = 0;
		
		int[] rarityScale = {30, 20, 10, 5, 2};
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(40));
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(10));
		addDecoration(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.BANGIOMORPHA).setCountPerChunk(8));
		addDecoration(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.MARPOLIA).setCountPerChunk(rarityScale[3]));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
	}
	
	@Override
	public void generateBiomeTerrain(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.generateBiomeTerrain(world, rand, primer, blockX, blockZ, d);
	}

	@Override
	public boolean shouldEntityPreferBiome(EntityLivingBase entity)
	{
		return true;
	}
}
