package genesis.world;

import genesis.world.layer.GenLayerGenesis;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;

public class WorldChunkManagerGenesis extends WorldChunkManager
{

	public WorldChunkManagerGenesis(long seed)
	{
		super();
		GenLayer[] agenlayer = GenLayerGenesis.initializeAllBiomeGenerators(seed);
		this.genBiomes = agenlayer[0];
		this.biomeIndexLayer = agenlayer[1];
	}

	public WorldChunkManagerGenesis(World worldIn)
	{
		this(worldIn.getSeed());
	}

}
