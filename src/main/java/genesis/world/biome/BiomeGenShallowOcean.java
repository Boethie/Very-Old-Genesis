package genesis.world.biome;

import java.util.Random;

import genesis.metadata.EnumAquaticPlant;
import genesis.metadata.EnumCoral;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenCorals;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPebbles;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenShallowOcean extends BiomeGenBaseGenesis
{
	public BiomeGenShallowOcean(int id)
	{
		super(id);
		setBiomeName("Shallow Ocean");
		setHeight(-.8F, 0.1F);
		waterColorMultiplier = 0x008d49;
		
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HALYSITES).setCountPerChunk(1));
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HELIOLITES).setCountPerChunk(1));
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.FAVOSITES).setCountPerChunk(1));
		
		addDecoration(new WorldGenAquaticPlants().setGenerateInGroup(true, 5).setPlantType(EnumAquaticPlant.BANGIOMORPHA).setCountPerChunk(10));
		addDecoration(new WorldGenPebbles().setCountPerChunk(40));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		
		int[] rarityScale = {30, 20, 10, 5, 2};
		
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.HAZELLA).setCountPerChunk(rarityScale[0]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.DIAONIELLA).setCountPerChunk(rarityScale[1]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.CHANCELLORIA).setCountPerChunk(rarityScale[1]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.PIRANIA).setCountPerChunk(rarityScale[2]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.VAUXIA).setCountPerChunk(rarityScale[2]));
		addDecoration(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.PTERIDINIUM).setCountPerChunk(rarityScale[2]));
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
	public Vec3 getSkyColor()
	{
		//return new Vec3(0.294117647D, 0.474509804D, 0.501960784D);
		return new Vec3(0.196078431D, 0.474509804D, 0.380392157D);
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
