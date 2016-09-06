package genesis.world.biome;

import java.util.Random;

import genesis.combo.PlantBlocks;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenBoulders;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenSplash;
import genesis.world.gen.feature.WorldGenTreeBjuvia;
import genesis.world.gen.feature.WorldGenTreeVoltzia;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeRedDesert extends BiomeGenesis
{
	private boolean isHills = false;
	
	public BiomeRedDesert(Biome.BiomeProperties properties)
	{
		super(properties);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		
		theBiomeDecorator.sandPerChunk = 0;
		theBiomeDecorator.sandPerChunk2 = 0;
		
		getDecorator().setFlowerCount(0.38F);
		addFlower(WorldGenPlant.create(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.AETHOPHYLLUM), 1);
		addFlower(WorldGenPlant.create(EnumPlant.APOLDIA), 4);
		
		addDecoration(new WorldGenSplash(GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT), GenesisBlocks.silt.getBlockState(SiltBlocks.CRACKED_SILT, EnumSilt.RED_SILT)).setPatchRadius(4), 7);
		addDecoration(new WorldGenBoulders(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT), 1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.25F, 0.75F)), 0.25F);
		addDecoration(new WorldGenPebbles().setWaterRequired(false), 3.5F);
		addDecoration(new WorldGenRoots(), 5);
	}
	
	public BiomeRedDesert setIsHills(boolean hills)
	{
		this.isHills = hills;
		return this;
	}
	
	public BiomeRedDesert addTrees()
	{
		getDecorator().setTreeCount(0.2175F);
		
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
	
	public BiomeRedDesert setTopBlock(IBlockState state)
	{
		this.topBlock = state;
		return this;
	}
	
	@Override
	public float getFogDensity()
	{
		return 0.3F;
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.7F;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		float red = 0.755039216F;
		float green = 0.676607843F;
		float blue = 0.582490196F;
		
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
	public int getSkyColorByTemp(float temperature)
	{
		return 0xB28F77;
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
					
					if (iblockstate1.getMaterial() == Material.AIR)
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
							
							if (i1 < seaLevel && (topState == null || topState.getMaterial() == Material.AIR))
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
