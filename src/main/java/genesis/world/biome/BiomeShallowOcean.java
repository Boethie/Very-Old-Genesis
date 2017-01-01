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
		topBlock = GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		
		addDecoration(new WorldGenMossStages(), 30);
		
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HALYSITES), 0.275F);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HELIOLITES), 0.275F);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.FAVOSITES), 0.275F);
		
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.BANGIOMORPHA).setPatchCount(12), 1.8F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARPOLIA).setPatchCount(12), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARGERETIA).setPatchCount(12), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.GRYPANIA).setPatchCount(12), 0.15F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.HAZELLA), 3.4F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DIAGONIELLA), 2.6F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHANCELLORIA), 2.6F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VETULOCYSTIS), 2.6F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PIRANIA), 1.8F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VAUXIA), 1.8F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PTERIDINIUM), 1.8F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PRIMOCANDELABRUM), 1.8F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHARNIA, EnumAquaticPlant.CHARNIA_TOP), 1.8F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ERNIETTA), 1.8F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.WAPKIA), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DINOMISCHUS), 0.15F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ECHMATOCRINUS), 0.15F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.THAUMAPTILON), 0.15F);
		
		addDecoration(new WorldGenPebbles(), 40);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
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
