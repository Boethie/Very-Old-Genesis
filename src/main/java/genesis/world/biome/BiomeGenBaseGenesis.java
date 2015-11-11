package genesis.world.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumFern;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenDecorationBase;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.gen.feature.WorldGenTreeBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public abstract class BiomeGenBaseGenesis extends BiomeGenBase
{
	public IBlockState oceanFloor = GenesisBlocks.ooze.getDefaultState();
	public int[] mossStages = new int[0];
	
	public BiomeGenBaseGenesis(int id)
	{
		super(id);
		theBiomeDecorator.clayPerChunk = 1;
		topBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST);
		spawnableCaveCreatureList.clear();
		spawnableCreatureList.clear();
		spawnableMonsterList.clear();
		spawnableWaterCreatureList.clear();
		waterColorMultiplier = 0xaa791e;
		getGenesisDecorator().sandPerChunk2 = 1;
	}
	
	public BiomeGenBaseGenesis setWaterColor(int color)
	{
		waterColorMultiplier = color;
		return this;
	}
	
	public List<IBlockState> getSpawnablePlants(Random rand)
	{
		List<IBlockState> spawnablePlants = new ArrayList<IBlockState>();
		
		for (WorldGenDecorationBase decoration : getGenesisDecorator().decorations)
		{
			IBlockState plant = decoration.getSpawnablePlant(rand);
			
			if (plant != null)
			{
				spawnablePlants.add(plant);
			}
		}
		
		return spawnablePlants;
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
	public BiomeGenBaseGenesis setColor(int color)
	{
		super.setColor(color);
		return this;
	}
	
	@Override
	public BiomeGenBaseGenesis setBiomeName(String name)
	{
		super.setBiomeName(name);
		return this;
	}
	
	@Override
	public BiomeGenBaseGenesis setHeight(BiomeGenBase.Height height)
	{
		super.setHeight(height);
		return this;
	}
	
	public BiomeGenBaseGenesis setHeight(float minHeight, float maxHeight)
	{
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		return this;
	}
	
	public Vec3 getSkyColor()
	{
		return new Vec3(0.29411764705882352941176470588235D, 0.47450980392156862745098039215686D, 0.1960784313725490196078431372549D);
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getFernBlockState(EnumFern.ZYGOPTERIS)).setVolume(64);
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
		int l = (int)(d / 3.0D + 5.0D + rand.nextDouble() * 0.25D);
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
				
				if (state.getBlock().getMaterial() == Material.air)
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
						
						if (y < 63 && (top == null || top.getBlock().getMaterial() == Material.air))
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
							k = rand.nextInt(6) + Math.max(0, y - 58);
							filler = GenesisBlocks.limestone.getDefaultState();
						}
						
						if(k == 0 && GenesisBlocks.silt.isStateOf(filler, SiltBlocks.SILT, EnumSilt.SILT))
						{
							k = rand.nextInt(5) + Math.max(0, y - 58);
							filler = GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.SILT);
						} else if(k == 0 && GenesisBlocks.silt.isStateOf(filler, SiltBlocks.SILT, EnumSilt.RED_SILT))
						{
							k = rand.nextInt(5) + Math.max(0, y - 58);
							filler = GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT);
						}
					}
				}
			}
		}
	}
}
