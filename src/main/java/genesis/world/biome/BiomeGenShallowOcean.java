package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumAquaticPlant;
import genesis.metadata.EnumCoral;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenCorals;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPebbles;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenShallowOcean extends BiomeGenBaseGenesis
{
	public BiomeGenShallowOcean(int id)
	{
		super(id);
		setBiomeName("Shallow Ocean");
		topBlock = GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT).getDefaultState();
		setHeight(-0.8F, 0.1F);
		
		waterColorMultiplier = 0x008d49;
		theBiomeDecorator.grassPerChunk = 0;
		
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HALYSITES).setCountPerChunk(1));
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HELIOLITES).setCountPerChunk(1));
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.FAVOSITES).setCountPerChunk(1));
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(40));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		
		int[] rarityScale = {30, 20, 10, 5, 2};
		
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.HAZELLA).setCountPerChunk(rarityScale[0]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.DIAONIELLA).setCountPerChunk(rarityScale[1]));
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
