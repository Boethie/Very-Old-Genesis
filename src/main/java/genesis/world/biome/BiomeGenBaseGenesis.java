package genesis.world.biome;

import java.util.List;
import java.util.Random;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import genesis.block.BlockMoss;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.PlantGenerator;
import genesis.world.biome.decorate.WorldGenDecorationBase;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.gen.feature.WorldGenTreeBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public abstract class BiomeGenBaseGenesis extends BiomeGenBase implements IBiomeGenFog
{
	public int biomeID;
	
	public static class FlowerGeneratorEntry extends WeightedRandom.Item
	{
		private final PlantGenerator gen;
		
		public FlowerGeneratorEntry(PlantGenerator gen, int weight)
		{
			super(weight);
			
			this.gen = gen;
		}
		
		public PlantGenerator getGenerator()
		{
			return gen;
		}
	}
	
	public IBlockState oceanFloor = GenesisBlocks.ooze.getDefaultState();
	public int[] mossStages = new int[0];
	
	private List<FlowerGeneratorEntry> plants = Lists.newArrayList();
	private int plantsWeight = 0;
	
	public BiomeGenBaseGenesis(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		theBiomeDecorator.clayPerChunk = 1;
		topBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST);
		spawnableCaveCreatureList.clear();
		spawnableCreatureList.clear();
		spawnableMonsterList.clear();
		spawnableWaterCreatureList.clear();
		
		//properties.setWaterColor(0xAA791E);
		
		getGenesisDecorator().sandPerChunk2 = 1;
	}
	
	public void setBiomeId(int id)
	{
		biomeID = id;
	}
	
	public void addGrassFlower(PlantGenerator gen, int weight)
	{
		plants.add(new FlowerGeneratorEntry(gen, weight));
		plantsWeight += weight;
	}
	
	public void addGrassFlowers(int defaultWeight)
	{
		for (PlantGenerator gen : FluentIterable.from(getGenesisDecorator().decorations).filter(PlantGenerator.class))
		{
			addGrassFlower(gen, defaultWeight);
		}
	}
	
	public void addGrassFlowers()
	{
		addGrassFlowers(25);
	}
	
	public void clearGrassFlowers()
	{
		plants.clear();
		plantsWeight = 0;
	}
	
	@Override
	public void plantFlower(World world, Random rand, BlockPos pos)
	{
		if (plantsWeight > 0)
			WeightedRandom.getRandomItem(rand, plants, plantsWeight).getGenerator().placePlant(world, pos, rand);
	}
	
	protected void addDecoration(WorldGenDecorationBase decoration)
	{
		getGenesisDecorator().decorations.add(decoration);
	}
	
	protected void addTree(WorldGenTreeBase tree)
	{
		getGenesisDecorator().trees.add(tree);
	}
	
	public BiomeDecoratorGenesis getGenesisDecorator()
	{
		return (BiomeDecoratorGenesis) theBiomeDecorator;
	}
	
	@Override
	public BiomeDecoratorGenesis createBiomeDecorator()
	{
		//return getModdedBiomeDecorator(new BiomeDecoratorGenesis());
		return new BiomeDecoratorGenesis();
	}
	
	@Override
	public int getSkyColorByTemp(float temperature)
	{
		return 0x4B7932;
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getPlantBlockState(EnumPlant.ZYGOPTERIS)).setVolume(64);
	}
	
	private IBlockState getTopBlock(Random rand)
	{
		IBlockState top = topBlock;
		
		if (top.getBlock() instanceof BlockMoss && mossStages.length > 0)
		{
			top = top.withProperty(BlockMoss.STAGE, mossStages[rand.nextInt(mossStages.length)]);
		}
		
		return top;
	}
	
	@Override
	public void generateBiomeTerrain(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		IBlockState top = getTopBlock(rand);
		IBlockState filler = fillerBlock;
		int k = -1;
		int l = (int)(d / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int chunkX = blockX & 15;
		int chunkZ = blockZ & 15;
		
		for (int y = 255; y >= 0; --y)
		{
			if (y <= rand.nextInt(5))
			{
				primer.setBlockState(chunkZ, y, chunkX, Blocks.bedrock.getDefaultState());
			}
			else
			{
				IBlockState state = primer.getBlockState(chunkZ, y, chunkX);
				
				if (state.getBlock().getMaterial(state) == Material.air)
				{
					k = -1;
				}
				else if (state.getBlock() == GenesisBlocks.granite)
				{
					if (k == -1)
					{
						if (l <= 0)
						{
							top = null;
							filler = GenesisBlocks.granite.getDefaultState();
						}
						else if (y >= 59 && y <= 64)
						{
							top = getTopBlock(rand);
							filler = fillerBlock;
						}
						
						if (y < 63 && (top == null || top.getBlock().getMaterial(top) == Material.air))
						{
							if (getFloatTemperature(new BlockPos(blockX, y, blockZ)) < 0.15F)
							{
								top = Blocks.ice.getDefaultState();
							}
							else
							{
								top = Blocks.water.getDefaultState();
							}
						}
						
						k = l;
						
						if (y >= 62)
						{
							primer.setBlockState(chunkZ, y, chunkX, top);
						}
						else if (y < 56 - l)
						{
							top = null;
							filler = oceanFloor;
							primer.setBlockState(chunkZ, y, chunkX, filler);
						}
						else if (y < 62 - l)
						{
							top = null;
							primer.setBlockState(chunkZ, y, chunkX, filler);
							filler = GenesisBlocks.limestone.getDefaultState();
						}
						else
						{
							primer.setBlockState(chunkZ, y, chunkX, filler);
						}
					}
					else if (k > 0)
					{
						--k;
						primer.setBlockState(chunkZ, y, chunkX, filler);
						
						if (k == 0 && filler == oceanFloor)
						{
							k = rand.nextInt(6) + Math.max(1, y - 58);
							filler = GenesisBlocks.limestone.getDefaultState();
						}
						
						if (k == 0 && GenesisBlocks.silt.isStateOf(filler, SiltBlocks.SILT))
						{
							k = rand.nextInt(5) + Math.max(1, y - 58);
							filler = GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, GenesisBlocks.silt.getVariant(filler));
						}
					}
				}
			}
		}
	}
	
	@Override
	public float getFogDensity()
	{
		return 1.0F;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		float red = 0.533333333F;
		float green = 0.647058824F;
		float blue = 0.474509804F;
		
		return new Vec3d(red, green, blue);
	}
	
	@Override
	public Vec3d getFogColorNight()
	{
		float red = 0.070941176F;
		float green = 0.070941176F;
		float blue = 0.070941176F;
		
		return new Vec3d(red, green, blue);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.01F;
	}
	
	//TODO: Get rid of this? It's unused...
	public int getIntFromColor(float red, float green, float blue)
	{
		int r = Math.round(255 * red);
		int g = Math.round(255 * green);
		int b = Math.round(255 * blue);
		
		r = (r << 16) & 0x00FF0000;
		g = (g << 8) & 0x0000FF00;
		b = b & 0x000000FF;
		
		return 0xFF000000 | r | g | b;
	}
}
