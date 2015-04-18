package genesis.world;

import java.util.Arrays;

import genesis.common.GenesisBiomes;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class ChunkManagerGenesis extends WorldChunkManager
{

	public ChunkManagerGenesis(World world)
	{
		super(world);
	}

	@Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] gens, int p_76937_2_, int p_76937_3_, int p_76937_4_, int p_76937_5_)
	{
		if (gens == null || gens.length < p_76937_4_ * p_76937_5_)
        {
            gens = new BiomeGenBase[p_76937_4_ * p_76937_5_];
        }
		Arrays.fill(gens, GenesisBiomes.rainforest);
		return gens;
	}
	
	
	
}
