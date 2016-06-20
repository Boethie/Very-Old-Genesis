package genesis.world.biome;

import java.util.Random;

import genesis.combo.PlantBlocks;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenSplash;
import genesis.world.gen.feature.WorldGenTreeBjuvia;
import genesis.world.gen.feature.WorldGenTreeVoltzia;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenRedDesert extends BiomeGenBaseGenesis
{
	private boolean isHills = false;
	
	public BiomeGenRedDesert(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		
		theBiomeDecorator.sandPerChunk = 0;
		theBiomeDecorator.sandPerChunk2 = 0;
		
		getDecorator().setFlowerCount(0.5F);
		addFlower(WorldGenPlant.create(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.AETHOPHYLLUM), 1);
		addFlower(WorldGenPlant.create(EnumPlant.APOLDIA), 4);
		
		addDecoration(new WorldGenBoulders(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT), 1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1F)), 0.4F);
		addDecoration(new WorldGenSplash(GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT), GenesisBlocks.silt.getBlockState(SiltBlocks.CRACKED_SILT, EnumSilt.RED_SILT)).setPatchRadius(4), 8);
		addDecoration(new WorldGenPebbles().setWaterRequired(false), 5);
		addDecoration(new WorldGenRoots(), 5);
	}
	
	public BiomeGenRedDesert setIsHills(boolean hills)
	{
		this.isHills = hills;
		return this;
	}
	
	public BiomeGenRedDesert addFern()
	{
		getDecorator().setGrassCount(this.isHills ? 1.55F : 0.15F);
		addGrass(WorldGenPlant.create(EnumPlant.WACHTLERIA).setPatchCount(4), 1);
		
		return this;
	}
	
	public BiomeGenRedDesert addTrees()
	{
		getDecorator().setTreeCount(0.4F);
		
		if (!this.isHills)
		{
			addTree(new WorldGenTreeBjuvia(4, 6, true), 3);
			addTree(new WorldGenTreeVoltzia(4, 8, true), 10);
		}
		else
		{
			getDecorator().setTreeCount(4.6F);
			
			addTree(new WorldGenTreeVoltzia(4, 8, true), 10);
		}
		return this;
	}
	
	public BiomeGenRedDesert setTopBlock(IBlockState state)
	{
		this.topBlock = state;
		return this;
	}
	
	@Override
	public float getFogDensity()
	{
		return 0.75F;
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.25F;
	}
	
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunkPrimer, int x, int z, double noiseVal)
    {
		if (!this.isHills)
		{
			super.genTerrainBlocks(world, rand, chunkPrimer, x, z, noiseVal);
		}
		else
		{
			int chunkZ = x & 15;
			int chunkX = z & 15;
			int seaLevel = world.getSeaLevel();
			IBlockState topState = this.topBlock;
			IBlockState fillerState = this.fillerBlock;
			int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
			boolean flag = Math.cos(noiseVal / 3.0D * Math.PI) > 0.0D;
			int l = -1;
			
			for (int i1 = 255; i1 >= 0; --i1)
			{
				if (i1 <= rand.nextInt(5))
				{
					chunkPrimer.setBlockState(chunkX, i1, chunkZ, BEDROCK);
				}
				else
				{
					IBlockState iblockstate1 = chunkPrimer.getBlockState(chunkX, i1, chunkZ);
					
					if (iblockstate1.getMaterial() == Material.air)
					{
						l = -1;
					}
					else if (iblockstate1.getBlock() == GenesisBlocks.granite)
					{
						if (l == -1)
						{
							if (k <= 0)
							{
								topState = AIR;
								fillerState = GenesisBlocks.granite.getDefaultState();
							}
							else if (i1 >= seaLevel - 4 && i1 <= seaLevel + 1)
							{
								topState = this.topBlock;
								fillerState = this.fillerBlock;
                            }
							
							if (i1 < seaLevel && (topState == null || topState.getMaterial() == Material.air))
							{
								topState = WATER;
							}
							
							l = k + Math.max(0, i1 - seaLevel);
							
							if (i1 < seaLevel - 1)
							{
								chunkPrimer.setBlockState(chunkX, i1, chunkZ, fillerState);
								
								if (fillerState == GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT))
								{
									chunkPrimer.setBlockState(chunkX, i1, chunkZ, this.topBlock);
								}
							}
							else if (i1 > 70 + k * 2)
							{
								if (flag)
								{
									chunkPrimer.setBlockState(chunkX, i1, chunkZ, GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT));
								}
								else
								{
									chunkPrimer.setBlockState(chunkX, i1, chunkZ, this.topBlock);
								}
							}
							else if (i1 <= seaLevel + 3 + k)
							{
								chunkPrimer.setBlockState(chunkX, i1, chunkZ, this.topBlock);
							}
							else
							{
								IBlockState iblockstate2;
								
								iblockstate2 = this.topBlock;
								
								chunkPrimer.setBlockState(chunkX, i1, chunkZ, iblockstate2);
							}
						}
						else if (l > 0)
						{
							--l;
							chunkPrimer.setBlockState(chunkX, i1, chunkZ, this.topBlock);
						}
					}
				}
			}
		}
    }
}
