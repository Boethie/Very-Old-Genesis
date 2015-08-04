package genesis.world.biome;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenSwamp extends BiomeGenBaseGenesis
{
	public BiomeGenSwamp(int id)
	{
		super(id);
		setBiomeName("Swamp");
		setHeight(BiomeGenBase.height_ShallowWaters);
	}
}
