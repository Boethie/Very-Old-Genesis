package genesis.world.biome;

import genesis.metadata.EnumAquaticPlant;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenRiver extends BiomeGenBaseGenesis
{
	public BiomeGenRiver(int id)
	{
		super(id);
		setBiomeName("River");
		setHeight(BiomeGenBase.height_ShallowWaters);
		
		theBiomeDecorator.grassPerChunk = 0;
		
		int[] rarityScale = {30, 20, 10, 5, 2};
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(40));
		addDecoration(new WorldGenRockBoulders().setCountPerChunk(10));
		addDecoration(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.BANGIOMORPHA).setCountPerChunk(8));
		addDecoration(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.MARPOLIA).setCountPerChunk(rarityScale[3]));
	}
}
