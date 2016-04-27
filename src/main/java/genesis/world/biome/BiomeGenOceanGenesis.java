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
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenOceanGenesis extends BiomeGenBaseGenesis
{
	public BiomeGenOceanGenesis(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
	}
	
	public BiomeGenOceanGenesis addElements(int multiplier)
	{
		int[] rarityScale = {1, 5 * multiplier, 1, 5 * multiplier, 1, 5 * multiplier, multiplier, multiplier};
		
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HALYSITES).setRarity(4), multiplier);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HELIOLITES).setRarity(4), multiplier);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.FAVOSITES).setRarity(4), multiplier);
		
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.HAZELLA), rarityScale[0]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DIAGONIELLA), rarityScale[1]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHANCELLORIA), rarityScale[1]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VETULOCYSTIS), rarityScale[1]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PIRANIA), rarityScale[2]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VAUXIA), rarityScale[2]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PTERIDINIUM), rarityScale[2]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.BANGIOMORPHA).setPatchCount(9), rarityScale[2]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHARNIA, EnumAquaticPlant.CHARNIA_TOP), rarityScale[2]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ERNIETTA), rarityScale[2]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.WAPKIA), rarityScale[3]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARPOLIA).setPatchCount(9), rarityScale[3]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARGERETIA).setPatchCount(9), rarityScale[3]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DINOMISCHUS), rarityScale[4]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ECHMATOCRINUS), rarityScale[4]);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.GRYPANIA).setPatchCount(9), rarityScale[4]);
		
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
