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
		this.biomeName = "River";
		this.setHeight(BiomeGenBase.height_ShallowWaters);
		
		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 0;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).generateDefaultTrees = false;
		
		int[] rarityScale = {30, 20, 10, 5, 2};
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenPebbles().setCountPerChunk(40));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenRockBoulders().setCountPerChunk(10));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.BANGIOMORPHA).setCountPerChunk(8));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.MARPOLIA).setCountPerChunk(rarityScale[3]));
	}
}
