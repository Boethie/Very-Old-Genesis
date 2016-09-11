package genesis.block;

import java.util.Random;

import genesis.common.GenesisCreativeTabs;
import genesis.util.WorldUtils;
import genesis.world.biome.BiomeGenesis;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.IPlantable;

public class BlockPeat extends BlockGenesis implements IGrowable
{
	public BlockPeat()
	{
		super(Material.GROUND, SoundType.GROUND);
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		
		setHarvestLevel("shovel", 0);
		setHardness(0.6F);
		
		Blocks.FIRE.setFireInfo(this, 5, 5);
	}
	
	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		Biome biome = world.getBiome(pos);
		BiomeGenesis biomeGenesis = null;
		
		if (biome instanceof BiomeGenesis)
		{
			biomeGenesis = (BiomeGenesis) biome;
		}
		
		BlockPos aboveCenter = pos.up();
		int loops = 0;
		
		while (loops < 128)
		{
			BlockPos plantPos = aboveCenter;
			int i = 0;
			
			while (true)
			{
				if (i < loops / 16)
				{
					plantPos = plantPos.add(rand.nextInt(3) - 1, ((rand.nextInt(3) - 1) * rand.nextInt(3)) / 2, rand.nextInt(3) - 1);
					
					if ((world.getBlockState(plantPos.down()).getBlock() == this) && !world.getBlockState(plantPos).isNormalCube())
					{
						i++;
						continue;
					}
				}
				else if (world.isAirBlock(plantPos))
				{
					if (rand.nextInt(8) == 0)
					{
						world.getBiome(plantPos).plantFlower(world, rand, plantPos);
					}
					else
					{
						// Plant Grass
						if (biomeGenesis != null)
						{
							biomeGenesis.getRandomWorldGenForGrass(rand).place(world, rand, plantPos);
						}
						else
						{
							// Vanilla
							world.setBlockState(plantPos, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS));
						}
					}
				}
				
				loops++;
				break;
			}
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		switch (plantable.getPlantType(world, pos.up()))
		{
		case Cave:
		case Plains:
			return true;
		case Beach:
			return WorldUtils.isWater(world, pos.east()) ||
					WorldUtils.isWater(world, pos.west()) ||
					WorldUtils.isWater(world, pos.north()) ||
					WorldUtils.isWater(world, pos.south());
		default:
			return super.canSustainPlant(state, world, pos, direction, plantable);
		}
	}
	
	@Override
	public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source)
	{
		world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
	}
	
	protected final float[] lightFertility = {
		-0.2F,	// 0
		-0.15F,	// 1
		-0.1F,	// 2
		-0.05F,	// 3
		0.0F,	// 4
		0.125F,	// 5
		0.3F,	// 6
		0.45F,	// 7
		0.55F,	// 8
		0.7F,	// 9
		0.8F,	// 10
		0.9F,	// 11
		1.0F,	// 12
		1.0F,	// 13
		0.5F,	// 14
		0.0F	// 15
	};
	
	public float getFertility(World world, BlockPos pos, boolean generation)
	{
		BlockPos above = pos.up();
		
		if (world.getBlockLightOpacity(above) >= 255)
		{
			return 0;
		}
		
		IBlockState stateAbove = world.getBlockState(above);
		
		if (stateAbove.getMaterial() == Material.WATER)
		{
			return 0;
		}
		
		float light = 0;
		int lightSamples = 0;
		float water = 0;
		
		for (BlockPos sample : WorldUtils.getArea(pos.add(-1, 0, -1), pos.add(1, 0, 1)))
		{
			BlockPos aboveSample = sample;
			
			for (int i = 0; i < 2; i++)
			{
				aboveSample = aboveSample.up();
				
				if (world.getBlockLightOpacity(aboveSample) < 255)
				{
					int lightLevel;
					
					if (generation)
					{
						int block = world.getLightFor(EnumSkyBlock.BLOCK, pos);
						int sky = world.getLightFor(EnumSkyBlock.SKY, pos);
						lightLevel = Math.min(block, sky);
					}
					else
					{
						lightLevel = world.getLightFromNeighbors(aboveSample);
					}
					
					light += lightFertility[lightLevel];
					lightSamples++;
					break;
				}
			}
		}
		
		final int rad = 3;
		
		for (BlockPos sample : WorldUtils.getArea(pos.add(-rad, -rad, -rad), pos.add(rad, rad, rad)))
		{
			if (sample.distanceSq(pos) <= rad * rad && world.getBlockState(sample).getMaterial() == Material.WATER)
			{
				water++;
			}
		}
		
		water /= 10;
		
		light /= lightSamples;
		
		float out = water + light;
		out = MathHelper.clamp_float(out, 0, 1);
		
		return out;
	}
	
	public float getFertility(World world, BlockPos pos)
	{
		return getFertility(world, pos, false);
	}
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return true;
	}
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}
}
