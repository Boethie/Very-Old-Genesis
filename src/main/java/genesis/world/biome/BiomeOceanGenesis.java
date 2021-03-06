package genesis.world.biome;

import java.util.Random;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumAquaticPlant;
import genesis.combo.variant.EnumCoral;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenCorals;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeOceanGenesis extends BiomeGenesis
{
	public BiomeOceanGenesis(Biome.BiomeProperties properties)
	{
		super(properties);
		
		topBlock = GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);

		addDecorations();
	}
	
	protected BiomeOceanGenesis addDecorations()
	{
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HALYSITES), 0.25F);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HELIOLITES), 0.25F);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.FAVOSITES), 0.25F);
		
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.BANGIOMORPHA).setPatchCount(12), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARPOLIA).setPatchCount(12), 0.9F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARGERETIA).setPatchCount(12), 0.9F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.GRYPANIA).setPatchCount(12), 0.1F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.HAZELLA), 2);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DIAGONIELLA), 1.6F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHANCELLORIA), 1.6F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VETULOCYSTIS), 1.6F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PIRANIA), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VAUXIA), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PTERIDINIUM), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PRIMOCANDELABRUM), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHARNIA, EnumAquaticPlant.CHARNIA_TOP), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ERNIETTA), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.WAPKIA), 0.9F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DINOMISCHUS), 0.07F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ECHMATOCRINUS), 0.07F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.THAUMAPTILON), 0.07F);
		
		return this;
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
