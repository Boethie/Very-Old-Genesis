package genesis.world.biome;

import genesis.metadata.EnumAquaticPlant;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenAquaticPlants;

public class BiomeGenDeepOcean extends BiomeGenBaseGenesis
{
	public BiomeGenDeepOcean(int id)
	{
		super(id);
		this.biomeName = "Deep Ocean";
		this.minHeight = -1.0F;
		this.maxHeight = -0.1F;
		this.waterColorMultiplier = 0x008d49;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 5).setPlantType(EnumAquaticPlant.BANGIOMORPHA).setCountPerChunk(2));
		
		int[] rarityScale = {2, 2, 2, 1, 1};
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.HAZELLA).setCountPerChunk(rarityScale[0]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.DIAONIELLA).setCountPerChunk(rarityScale[1]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.CHANCELLORIA).setCountPerChunk(rarityScale[1]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.PIRANIA).setCountPerChunk(rarityScale[2]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.VAUXIA).setCountPerChunk(rarityScale[2]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.PTERIDINIUM).setCountPerChunk(rarityScale[2]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.CHARNIA, EnumAquaticPlant.CHARNIA_TOP).setCountPerChunk(rarityScale[2]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.ERNIETTA).setCountPerChunk(rarityScale[2]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.WAPKIA).setCountPerChunk(rarityScale[3]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.MARPOLIA).setCountPerChunk(rarityScale[3]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.MARGERETIA).setCountPerChunk(rarityScale[3]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.DINOMISCHUS).setCountPerChunk(rarityScale[4]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.ECHMATOCRINUS).setCountPerChunk(rarityScale[4]));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.GRYPANIA).setCountPerChunk(rarityScale[4]));
	}
}
