package genesis.world.biome;

import java.util.*;

import genesis.block.BlockMoss;
import genesis.combo.SiltBlocks;
import genesis.common.GenesisBlocks;
import genesis.util.WeightedRandomList;
import genesis.util.random.i.*;
import genesis.world.biome.decorate.*;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.*;

public abstract class BiomeGenBaseGenesis extends BiomeGenBase implements IBiomeGenFog
{
	public IBlockState oceanFloor = GenesisBlocks.ooze.getDefaultState();
	public int[] mossStages = new int[0];
	
	private WeightedRandomList<WorldGenDecorationBase> grass = new WeightedRandomList<>();
	private WeightedRandomList<WorldGenDecorationBase> plants = new WeightedRandomList<>();
	private WeightedRandomList<WorldGenAbstractTree> trees = new WeightedRandomList<>();
	private List<DecorationEntry> decor = new ArrayList<>();
	
	public BiomeGenBaseGenesis(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		theBiomeDecorator.clayPerChunk = 1;
		topBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST);
		spawnableCaveCreatureList.clear();
		spawnableCreatureList.clear();
		spawnableMonsterList.clear();
		spawnableWaterCreatureList.clear();
		
		getDecorator().sandPerChunk2 = 1;
	}
	
	public void addGrass(WorldGenDecorationBase gen, int weight)
	{
		grass.add(gen, weight);
	}
	
	public void clearGrass()
	{
		grass.clear();
	}
	
	@Override
	public WorldGenDecorationBase getRandomWorldGenForGrass(Random rand)
	{
		return grass.get(rand);
	}
	
	public void addFlower(WorldGenDecorationBase gen, int weight)
	{
		plants.add(gen, weight);
	}
	
	public void clearFlowers()
	{
		plants.clear();
	}
	
	@Override
	public void plantFlower(World world, Random rand, BlockPos pos)
	{
		plants.get(rand).place(world, rand, pos);
	}
	
	public WorldGenerator getRandomFlower(Random rand)
	{
		return plants.get(rand);
	}
	
	protected void addTree(WorldGenAbstractTree tree, int weight)
	{
		trees.add(tree, weight);
	}
	
	@Override
	public WorldGenAbstractTree genBigTreeChance(Random rand)
	{
		return trees.get(rand);
	}
	
	protected void addDecoration(WorldGenDecorationBase decoration, RandomIntProvider chunkCount)
	{
		decor.add(new DecorationEntry(decoration, chunkCount));
	}
	
	protected void addDecoration(WorldGenDecorationBase decoration, int chunkCount)
	{
		addDecoration(decoration, IntRange.create(chunkCount));
	}
	
	protected void addDecoration(WorldGenDecorationBase decoration, float chunkCount)
	{
		addDecoration(decoration, new IntFromFloat(chunkCount));
	}
	
	public List<DecorationEntry> getDecorations()
	{
		return decor;
	}
	
	public BiomeDecoratorGenesis getDecorator()
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
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
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
