package genesis.world.biome;

import java.util.Random;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumAquaticPlant;
import genesis.metadata.EnumCoral;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenCorals;
import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenOceanGenesis extends BiomeGenBaseGenesis
{
	public BiomeGenOceanGenesis(int id)
	{
		super(id);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		setWaterColor(0x007d39);
		
		theBiomeDecorator.grassPerChunk = 0;
	}
	
	public BiomeGenOceanGenesis addElements(int multiplier)
	{
		int[] rarityScale = {2 * multiplier, 2 * multiplier, 2 * multiplier, 1 * multiplier, 1 * multiplier};
		
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HALYSITES).setRarity(4).setCountPerChunk(1 * multiplier));
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HELIOLITES).setRarity(4).setCountPerChunk(1 * multiplier));
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.FAVOSITES).setRarity(4).setCountPerChunk(1 * multiplier));
		
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.HAZELLA).setCountPerChunk(rarityScale[0]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.DIAGONIELLA).setCountPerChunk(rarityScale[1]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.CHANCELLORIA).setCountPerChunk(rarityScale[1]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.VETULOCYSTIS).setCountPerChunk(rarityScale[1]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.PIRANIA).setCountPerChunk(rarityScale[2]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.VAUXIA).setCountPerChunk(rarityScale[2]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.PTERIDINIUM).setCountPerChunk(rarityScale[2]));
		addDecoration(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.BANGIOMORPHA).setCountPerChunk(rarityScale[2]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.CHARNIA, EnumAquaticPlant.CHARNIA_TOP).setCountPerChunk(rarityScale[2]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.ERNIETTA).setCountPerChunk(rarityScale[2]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.WAPKIA).setCountPerChunk(rarityScale[3]));
		addDecoration(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.MARPOLIA).setCountPerChunk(rarityScale[3]));
		addDecoration(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.MARGERETIA).setCountPerChunk(rarityScale[3]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.DINOMISCHUS).setCountPerChunk(rarityScale[4]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.ECHMATOCRINUS).setCountPerChunk(rarityScale[4]));
		addDecoration(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.GRYPANIA).setCountPerChunk(rarityScale[4]));
		
		addDecoration(new WorldGenRockBoulders().setRarity(85).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
		
		return this;
	}
	
	@Override
	public float getFogDensity(int x, int y, int z)
	{
		return 1.0F;
	}
	
	@Override
	public Vec3 getFogColor()
	{
		float red = 0.533333333F;
		float green = 0.647058824F;
		float blue = 0.474509804F;
		
		return new Vec3(red, green, blue);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
	
	@Override
	public void generateBiomeTerrain(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.generateBiomeTerrain(world, rand, primer, blockX, blockZ, d);
	}
}
