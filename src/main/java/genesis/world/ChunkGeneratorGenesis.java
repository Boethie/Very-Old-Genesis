package genesis.world;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;
import genesis.common.GenesisBlocks;
import genesis.world.gen.MapGenCavesGenesis;
import genesis.world.gen.MapGenRavineGenesis;
import genesis.world.gen.feature.WorldGenGenesisLakes;

import java.util.Random;

import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

//TODO: access transforming
public class ChunkGeneratorGenesis extends ChunkProviderGenerate
{
	private Random rand;
	private NoiseGeneratorPerlin field_147430_m;
	private World worldObj;
	private final boolean mapFeaturesEnabled;
	private ChunkProviderSettings settings;
	private double[] stoneNoise;
	private MapGenBase caveGenerator;
	private MapGenStronghold strongholdGenerator;
	private MapGenVillage villageGenerator;
	private MapGenMineshaft mineshaftGenerator;
	private MapGenScatteredFeature scatteredFeatureGenerator;
	private StructureOceanMonument oceanMonumentGenerator;
	private MapGenBase ravineGenerator;
	private BiomeGenBase[] biomesForGeneration;
	
	public ChunkGeneratorGenesis(World world, long seed, boolean mapFeaturesEnabled, String generatorOptions)
	{
		super(world, seed, mapFeaturesEnabled, generatorOptions);
		
		stoneNoise = new double[256];
		caveGenerator = new MapGenCavesGenesis();
		strongholdGenerator = new MapGenStronghold();
        villageGenerator = new MapGenVillage();
        mineshaftGenerator = new MapGenMineshaft();
        scatteredFeatureGenerator = new MapGenScatteredFeature();
        ravineGenerator = new MapGenRavineGenesis();
        oceanMonumentGenerator = new StructureOceanMonument();
        
		worldObj = world;
		this.mapFeaturesEnabled = mapFeaturesEnabled;
		rand = new Random(seed);
		field_147430_m = new NoiseGeneratorPerlin(rand, 4);
		
		if (generatorOptions != null)
		{
			settings = ChunkProviderSettings.Factory.func_177865_a(generatorOptions).func_177864_b();
		}
	}
	
	@Override
	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ)
	{
		BlockFalling.fallInstantly = true;
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;
		BlockPos pos = new BlockPos(blockX, 0, blockZ);
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(pos.add(16, 0, 16));
		rand.setSeed(worldObj.getSeed());
		long i1 = rand.nextLong() / 2L * 2L + 1L;
		long j1 = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed((long) chunkX * i1 + (long) chunkZ * j1 ^ worldObj.getSeed());
		boolean flag = false;
		ChunkCoordIntPair coords = new ChunkCoordIntPair(chunkX, chunkZ);
		
		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(chunkProvider, worldObj, rand, chunkX, chunkZ, flag));
		
        if (settings.useMineShafts && mapFeaturesEnabled)
        {
            mineshaftGenerator.func_175794_a(worldObj, rand, coords);
        }

        if (settings.useVillages && mapFeaturesEnabled)
        {
            flag = villageGenerator.func_175794_a(worldObj, rand, coords);
        }

        if (settings.useStrongholds && mapFeaturesEnabled)
        {
            strongholdGenerator.func_175794_a(worldObj, rand, coords);
        }

        if (settings.useTemples && mapFeaturesEnabled)
        {
            scatteredFeatureGenerator.func_175794_a(worldObj, rand, coords);
        }

        if (settings.useMonuments && mapFeaturesEnabled)
        {
            oceanMonumentGenerator.func_175794_a(worldObj, rand, coords);
        }

        if (biome != BiomeGenBase.desert && biome != BiomeGenBase.desertHills && settings.useWaterLakes && !flag && rand.nextInt(settings.waterLakeChance) == 0
            && TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, LAKE))
        {
            int x = rand.nextInt(16) + 8;
            int y = rand.nextInt(256);
            int z = rand.nextInt(16) + 8;
            (new WorldGenGenesisLakes(Blocks.water)).generate(worldObj, rand, pos.add(x, y, z));
        }

        if (TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, LAVA) && !flag && rand.nextInt(settings.lavaLakeChance / 10) == 0 && settings.useLavaLakes)
        {
        	int x = rand.nextInt(16) + 8;
        	int y = rand.nextInt(rand.nextInt(248) + 8);
        	int z = rand.nextInt(16) + 8;

            if (y < 63 || rand.nextInt(settings.lavaLakeChance / 8) == 0)
            {
                (new WorldGenGenesisLakes(GenesisBlocks.komatiitic_lava)).generate(worldObj, rand, pos.add(x, y, z));
            }
        }

        if (settings.useDungeons)
        {
            boolean doGen = TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, DUNGEON);
            for (int x = 0; doGen && x < settings.dungeonChance; ++x)
            {
            	int y = rand.nextInt(16) + 8;
            	int z = rand.nextInt(256);
                int j2 = rand.nextInt(16) + 8;
                (new WorldGenDungeons()).generate(worldObj, rand, pos.add(y, z, j2));
            }
        }

        biome.decorate(worldObj, rand, new BlockPos(blockX, 0, blockZ));
        if (TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, ANIMALS))
        {
        	SpawnerAnimals.performWorldGenSpawning(worldObj, biome, blockX + 8, blockZ + 8, 16, 16, rand);
        }
        
        pos = pos.add(8, 0, 8);

        boolean doGen = TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, flag, ICE);
        
        for (int x = 0; doGen && x < 16; ++x)
        {
            for (int y = 0; y < 16; ++y)
            {
                BlockPos surface = worldObj.getPrecipitationHeight(pos.add(x, 0, y));
                BlockPos water = surface.down();

                if (worldObj.canBlockFreezeNoWater(water))
                {
                    worldObj.setBlockState(water, Blocks.ice.getDefaultState(), 2);
                }

                if (worldObj.canSnowAt(surface, true))
                {
                    worldObj.setBlockState(surface, Blocks.snow_layer.getDefaultState(), 2);
                }
            }
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(chunkProvider, worldObj, rand, chunkX, chunkZ, flag));

        BlockFalling.fallInstantly = false;
    }
	
	@Override
	public void replaceBlocksForBiome(int chunkX, int chunkZ, ChunkPrimer primer, BiomeGenBase[] biomes)
    {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, chunkX, chunkZ, primer, worldObj);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;
        
        double d0 = 0.03125D;
        stoneNoise = field_147430_m.func_151599_a(stoneNoise, (double) (chunkX * 16), (double) (chunkZ * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);
        
        for (int offsetX = 0; offsetX < 16; ++offsetX)
        {
            for (int offsetZ = 0; offsetZ < 16; ++offsetZ)
            {
                BiomeGenBase biome = biomes[offsetZ + offsetX * 16];
                biome.genTerrainBlocks(worldObj, rand, primer, chunkX * 16 + offsetX, chunkZ * 16 + offsetZ, stoneNoise[offsetZ + offsetX * 16]);
            }
        }
    }
	
	@Override
	public Chunk provideChunk(int x, int z)
	{
		rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		setBlocksInChunk(x, z, chunkprimer);
		biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x * 16, z * 16, 16, 16);
		replaceBlocksForBiome(x, z, chunkprimer, biomesForGeneration);

        if (settings.useCaves)
        {
            caveGenerator.func_175792_a(this, worldObj, x, z, chunkprimer);
        }

        if (settings.useRavines)
        {
            ravineGenerator.func_175792_a(this, worldObj, x, z, chunkprimer);
        }

        if (settings.useMineShafts && mapFeaturesEnabled)
        {
            mineshaftGenerator.func_175792_a(this, worldObj, x, z, chunkprimer);
        }

        if (settings.useVillages && mapFeaturesEnabled)
        {
            villageGenerator.func_175792_a(this, worldObj, x, z, chunkprimer);
        }

        if (settings.useStrongholds && mapFeaturesEnabled)
        {
            strongholdGenerator.func_175792_a(this, worldObj, x, z, chunkprimer);
        }

        if (settings.useTemples && mapFeaturesEnabled)
        {
            scatteredFeatureGenerator.func_175792_a(this, worldObj, x, z, chunkprimer);
        }

        if (settings.useMonuments && mapFeaturesEnabled)
        {
            oceanMonumentGenerator.func_175792_a(this, worldObj, x, z, chunkprimer);
        }

        Chunk chunk = new Chunk(worldObj, chunkprimer, x, z);
        byte[] biomeArray = chunk.getBiomeArray();

        for (int k = 0; k < biomeArray.length; ++k)
        {
            biomeArray[k] = (byte) biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }
}
