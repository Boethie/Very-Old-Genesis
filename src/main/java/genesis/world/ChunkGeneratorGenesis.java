package genesis.world;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;
import genesis.common.GenesisBiomes;
import genesis.common.GenesisBlocks;
import genesis.world.gen.MapGenCavesGenesis;
import genesis.world.gen.MapGenRavineGenesis;
import genesis.world.gen.MapGenUndergroundLavaLakes;
import genesis.world.gen.feature.WorldGenGenesisLakes;

import java.util.List;

import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ChunkGeneratorGenesis extends ChunkProviderGenerate
{
	private MapGenBase caveGenerator;
	private MapGenBase ravineGenerator;
	private MapGenBase underGroundLavaLakeGenerator;
	
	public ChunkGeneratorGenesis(World world, long seed, boolean mapFeaturesEnabled, String generatorOptions)
	{
		super(world, seed, mapFeaturesEnabled, generatorOptions);
		caveGenerator = new MapGenCavesGenesis();
		ravineGenerator = new MapGenRavineGenesis();
		underGroundLavaLakeGenerator = new MapGenUndergroundLavaLakes();
		
		if (generatorOptions != null)
		{
			ChunkProviderSettings.Factory factory = ChunkProviderSettings.Factory.jsonToFactory(generatorOptions);
			factory.useDungeons = false;
			factory.useStrongholds = false;
			factory.useVillages = false;
			factory.useMineShafts = false;
			factory.useTemples = false;
			factory.useMonuments = false;
			settings = factory.func_177864_b();
			field_177476_s = settings.useLavaOceans ? Blocks.lava : Blocks.water;
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
		long xSeed = rand.nextLong() / 2L * 2L + 1L;
		long ySeed = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed(chunkX * xSeed + chunkZ * ySeed ^ worldObj.getSeed());
		boolean flag = false;
		//ChunkCoordIntPair coords = new ChunkCoordIntPair(chunkX, chunkZ);
		
		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(chunkProvider, worldObj, rand, chunkX, chunkZ, flag));
		
		int waterLakeChance = settings.waterLakeChance;
		
		if (biome.biomeID == GenesisBiomes.marsh.biomeID)
			waterLakeChance = 1;
		//if (biome.biomeID == GenesisBiomes.floodplains.biomeID)
			//waterLakeChance = 2;
		else if (biome.biomeID == GenesisBiomes.redLowlands.biomeID)
			waterLakeChance = 2;
		
		if (
				settings.useWaterLakes 
				&& !flag 
				&& rand.nextInt(waterLakeChance) == 0 
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
	public void setBlocksInChunk(int x, int y, ChunkPrimer primer)
	{
		biomesForGeneration = worldObj.getWorldChunkManager().getBiomesForGeneration(biomesForGeneration, x * 4 - 2, y * 4 - 2, 10, 10);
		func_147423_a(x * 4, 0, y * 4);
		
		for (int k = 0; k < 4; ++k)
		{
			int l = k * 5;
			int i1 = (k + 1) * 5;
			
			for (int j1 = 0; j1 < 4; ++j1)
			{
				int k1 = (l + j1) * 33;
				int l1 = (l + j1 + 1) * 33;
				int i2 = (i1 + j1) * 33;
				int j2 = (i1 + j1 + 1) * 33;
				
				for (int k2 = 0; k2 < 32; ++k2)
				{
					double d0 = 0.125D;
					double d1 = field_147434_q[k1 + k2];
					double d2 = field_147434_q[l1 + k2];
					double d3 = field_147434_q[i2 + k2];
					double d4 = field_147434_q[j2 + k2];
					double d5 = (field_147434_q[k1 + k2 + 1] - d1) * d0;
					double d6 = (field_147434_q[l1 + k2 + 1] - d2) * d0;
					double d7 = (field_147434_q[i2 + k2 + 1] - d3) * d0;
					double d8 = (field_147434_q[j2 + k2 + 1] - d4) * d0;
					
					for (int l2 = 0; l2 < 8; ++l2)
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;
						
						for (int i3 = 0; i3 < 4; ++i3)
						{
							double d14 = 0.25D;
							double d16 = (d11 - d10) * d14;
							double d15 = d10 - d16;
							
							for (int j3 = 0; j3 < 4; ++j3)
							{
								if ((d15 += d16) > 0.0D)
								{
									primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, GenesisBlocks.granite.getDefaultState());
								}
								else if (k2 * 8 + l2 < settings.seaLevel)
								{
									primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, field_177476_s.getDefaultState());
								}
							}
							
							d10 += d12;
							d11 += d13;
						}
						
						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}
	
	public void func_180517_a(int blockX, int blockZ, ChunkPrimer chunkPrimer, BiomeGenBase[] biomes)
	{
		ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, blockX, blockZ, chunkPrimer, this.worldObj);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getResult() == Result.DENY) return;
		
		double d0 = 0.03125D;
		this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, blockX * 16, blockZ * 16, 16, 16, d0 * 2, d0 * 2, 1);
		
		for (int k = 0; k < 16; ++k)
		{
			for (int l = 0; l < 16; ++l)
			{
				BiomeGenBase biomegenbase = biomes[l + k * 16];
				biomegenbase.genTerrainBlocks(this.worldObj, this.rand, chunkPrimer, blockX * 16 + k, blockZ * 16 + l, this.stoneNoise[l + k * 16]);
			}
		}
	}
	
	@Override
	public Chunk provideChunk(int x, int z)
	{
		this.rand.setSeed(x * 341873128712L + z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.setBlocksInChunk(x, z, chunkprimer);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
		func_180517_a(x, z, chunkprimer, this.biomesForGeneration);

		if (this.settings.useCaves)
		{
			this.caveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
		}
		
		if (this.settings.useRavines)
		{
			this.ravineGenerator.generate(this, this.worldObj, x, z, chunkprimer);
		}
		
		{
			this.underGroundLavaLakeGenerator.generate(this, this.worldObj, x, z, chunkprimer);
		}
		
		if (this.settings.useMineShafts && this.mapFeaturesEnabled)
		{
			this.mineshaftGenerator.generate(this, this.worldObj, x, z, chunkprimer);
		}

		if (this.settings.useVillages && this.mapFeaturesEnabled)
		{
			this.villageGenerator.generate(this, this.worldObj, x, z, chunkprimer);
		}

		if (this.settings.useStrongholds && this.mapFeaturesEnabled)
		{
			this.strongholdGenerator.generate(this, this.worldObj, x, z, chunkprimer);
		}

		if (this.settings.useTemples && this.mapFeaturesEnabled)
		{
			this.scatteredFeatureGenerator.generate(this, this.worldObj, x, z, chunkprimer);
		}

		if (this.settings.useMonuments && this.mapFeaturesEnabled)
		{
			this.oceanMonumentGenerator.generate(this, this.worldObj, x, z, chunkprimer);
		}

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
		byte[] abyte = chunk.getBiomeArray();

		for (int k = 0; k < abyte.length; ++k)
		{
			abyte[k] = (byte)this.biomesForGeneration[k].biomeID;
		}

		chunk.generateSkylightMap();
		return chunk;
	}
	
	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
	{
		return null;
	}
	
	@Override
	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return worldObj.getBiomeGenForCoords(pos).getSpawnableList(creatureType);
	}
}
