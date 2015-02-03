package genesis.world;

import genesis.world.gen.layer.GenLayerBiomeEdgeGenesis;
import genesis.world.gen.layer.GenLayerBiomeGenesis;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldTypeGenesis extends WorldType {

	public static WorldTypeGenesis instance;

	public WorldTypeGenesis(String str) {
		super(str);
	}

	@SideOnly(Side.CLIENT)
	public boolean getCanBeCreated()
	{
		return false;
	}

	@Override
	public WorldChunkManager getChunkManager(World world) {
		return new WorldChunkManagerGenesis(world);
	}

	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
		return new ChunkProviderGenesis(world, world.getSeed());
	}

	/**
	 * Creates the GenLayerBiome used for generating the world
	 *
	 * @param worldSeed   The world seed
	 * @param parentLayer The parent layer to feed into any layer you return
	 * @return A GenLayer that will return ints representing the Biomes to be generated, see GenLayerBiome
	 */
	public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer, String chunkProviderSettingsJson) {
		GenLayer ret = new GenLayerBiomeGenesis(200L, parentLayer, chunkProviderSettingsJson);
		ret = GenLayerZoom.magnify(1000L, ret, 2);
		ret = new GenLayerBiomeEdgeGenesis(1000L, ret);
		return ret;
	}
}