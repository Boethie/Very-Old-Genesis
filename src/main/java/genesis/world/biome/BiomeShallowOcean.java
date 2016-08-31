package genesis.world.biome;

import java.util.Random;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumAquaticPlant;
import genesis.combo.variant.EnumCoral;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenCorals;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPebbles;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeShallowOcean extends BiomeGenesis
{
	public BiomeShallowOcean(Biome.BiomeProperties properties)
	{
		super(properties);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		
		addDecoration(new WorldGenMossStages(), 30);
		
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HALYSITES), 0.35F);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HELIOLITES), 0.35F);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.FAVOSITES), 0.35F);
		
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.BANGIOMORPHA).setPatchCount(9), 2.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARPOLIA).setPatchCount(9), 1.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARGERETIA).setPatchCount(9), 1.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.GRYPANIA).setPatchCount(9), 0.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.HAZELLA), 4.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DIAGONIELLA), 3.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHANCELLORIA), 3.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VETULOCYSTIS), 3.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PIRANIA), 2.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VAUXIA), 2.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PTERIDINIUM), 2.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHARNIA, EnumAquaticPlant.CHARNIA_TOP), 2.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ERNIETTA), 2.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.WAPKIA), 1.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DINOMISCHUS), 0.2F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ECHMATOCRINUS), 0.2F);
		
		addDecoration(new WorldGenPebbles(), 40);
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.genTerrainBlocks(world, rand, primer, blockX, blockZ, d);
	}
}
