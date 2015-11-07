package genesis.block;

import genesis.client.*;
import genesis.common.*;
import genesis.util.*;
import genesis.util.random.*;
import genesis.world.biome.BiomeGenBaseGenesis;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.IPlantable;

public class BlockMoss extends BlockGrass
{
	public static final int STAGE_LAST = 3;
	public static final IProperty STAGE = PropertyInteger.create("stage", 0, STAGE_LAST);
	
	public BlockMoss()
	{
		setDefaultState(blockState.getBaseState().withProperty(STAGE, 0).withProperty(SNOWY, false));
		setHardness(0.6F);
		setStepSound(GenesisSounds.MOSS);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setHarvestLevel("shovel", 0);
	}
	
	@Override
	public BlockState createBlockState()
	{
		return new BlockState(this, STAGE, SNOWY);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, STAGE, SNOWY);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, STAGE, SNOWY);
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		BiomeGenBase biome = worldIn.getBiomeGenForCoords(pos);
		BiomeGenBaseGenesis biomeGenesis = null;
		List<IBlockState> genesisPlants = null;
		
		if (biome instanceof BiomeGenBaseGenesis)
		{
			biomeGenesis = (BiomeGenBaseGenesis) biome;
			genesisPlants = biomeGenesis.getSpawnablePlants(rand);
		}
		
		BlockPos origTopBlock = pos.up();
		int loops = 0;

		while (loops < 128)
		{
			BlockPos topBlock = origTopBlock;
			int i = 0;

			while (true)
			{
				if (i < (loops / 16))
				{
					topBlock = topBlock.add(rand.nextInt(3) - 1, ((rand.nextInt(3) - 1) * rand.nextInt(3)) / 2, rand.nextInt(3) - 1);

					if ((worldIn.getBlockState(topBlock.down()).getBlock() == this) && !worldIn.getBlockState(topBlock).getBlock().isNormalCube())
					{
						++i;
						continue;
					}
				}
				else if (worldIn.isAirBlock(topBlock))
				{
					IBlockState randPlant = null;

					if (rand.nextInt(8) == 0)
					{
						// Plant Flower
						if (genesisPlants != null)
						{
							randPlant = !genesisPlants.isEmpty() ? genesisPlants.get(rand.nextInt(genesisPlants.size())) : null;
						}
						else
						{
							// Vanilla
							worldIn.getBiomeGenForCoords(topBlock).plantFlower(worldIn, rand, topBlock);
						}
					}
					else
					{
						// Plant Grass
						if (biomeGenesis != null)
						{
							randPlant = biomeGenesis.getRandomWorldGenForGrass(rand).getSpawnablePlant(rand);
						}
						else
						{
							// Vanilla
							IBlockState tallgrass = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
							
							if (Blocks.tallgrass.canBlockStay(worldIn, topBlock, tallgrass))
	                        {
	                            worldIn.setBlockState(topBlock, tallgrass, 3);
	                        }
						}
					}
					
					if (randPlant != null)
					{
						Block block = randPlant.getBlock();
						boolean isBush = block instanceof BlockBush;
						boolean canBushStay = isBush && ((BlockBush) block).canBlockStay(worldIn, topBlock, randPlant);
						boolean canPlaceBlock = !isBush && block.canPlaceBlockAt(worldIn, topBlock);
						
						if (canBushStay || canPlaceBlock)
						{
							worldIn.setBlockState(topBlock, randPlant, 3);
						}
					}
				}

				++loops;
				break;
			}
		}
	}

	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		switch (plantable.getPlantType(world, pos.up()))
		{
		case Cave:
		case Plains:
		case Desert:
			return true;
		case Beach:
			return hasWater(world, pos.east()) || hasWater(world, pos.west()) || hasWater(world, pos.north()) || hasWater(world, pos.south());
		default:
			return super.canSustainPlant(world, pos, direction, plantable);
		}
	}

	@Override
	public void onPlantGrow(World world, BlockPos pos, BlockPos source)
	{
		world.setBlockState(pos, net.minecraft.init.Blocks.dirt.getDefaultState(), 2);
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
	
	public float getFertility(World world, BlockPos pos)
	{
		BlockPos above = pos.up();
		
		if (world.getBlockLightOpacity(above) >= 255)
		{
			return 0;
		}
		
		IBlockState stateAbove = world.getBlockState(above);
		
		if (stateAbove.getBlock().getMaterial() == Material.water)
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
					light += lightFertility[world.getLightFromNeighbors(aboveSample)];
					lightSamples++;
					break;
				}
			}
		}
		
		final int rad = 3;
		
		for (BlockPos sample : WorldUtils.getArea(pos.add(-rad, -rad, -rad), pos.add(rad, rad, rad)))
		{
			if (sample.distanceSq(pos) <= rad * rad && world.getBlockState(sample).getBlock().getMaterial() == Material.water)
			{
				water++;
			}
		}
		
		water /= 10;
		
		light /= lightSamples;
		
		//float humidity = world.getBiomeGenForCoords(pos).rainfall;
		//humidity *= 0.35F;
		
		float out = water + light;
		out = MathHelper.clamp_float(out, 0, 1);
		
		return out;
	}
	
	protected final IntRange[] targetStages = {
		IntRange.create(-1),
		IntRange.create(-1, 0),
		IntRange.create(0),
		IntRange.create(0, 1),
		IntRange.create(1),
		IntRange.create(1, 2),
		IntRange.create(2),
		IntRange.create(2, 3),
		IntRange.create(3),
	};
	
	public int getTargetStage(float fertility, Random rand)
	{
		return targetStages[Math.min(Math.round(fertility * targetStages.length), targetStages.length - 1)].get(rand);
	}

	protected final float growthChanceHumidityEffect = 0.25F;
	protected final float growthChanceMult = 0.25F;
	
	public float getGrowthChance(World world, BlockPos pos, boolean dying)
	{
		float humidity = world.getBiomeGenForCoords(pos).rainfall;
		float chance = 1 - growthChanceHumidityEffect + (humidity * growthChanceHumidityEffect * (dying ? -2 : 1));
		
		return chance * growthChanceMult;
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote)
		{
			int stage = (Integer) state.getValue(STAGE);
			
			float fertility = getFertility(world, pos);
			int targetStage = getTargetStage(fertility, world.rand);
			
			int diff = MathHelper.clamp_int(targetStage - stage, -1, 1);
			
			if (world.rand.nextFloat() <= getGrowthChance(world, pos, diff < 0))
			{
				stage += diff;
			}
			
			if (stage >= 0)
			{
				world.setBlockState(pos, state.withProperty(STAGE, stage));
				
				if (stage >= 1)
				{
					int spreadTries = 4;
					
					for (int i = 0; i < spreadTries; i++)
					{
						BlockPos randPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
						IBlockState randState = world.getBlockState(randPos);
						Block randBlock = randState.getBlock();

						boolean correctBlock = randBlock == Blocks.dirt || randBlock == Blocks.grass;
						
						if (correctBlock)
						{
							if (rand.nextFloat() >= getGrowthChance(world, randPos, false))
							{
								continue;
							}
							else if (getTargetStage(getFertility(world, randPos), world.rand) < 0)
							{
								continue;
							}
							
							world.setBlockState(randPos, getDefaultState());
						}
					}
				}
			}
			else if (rand.nextInt(10) == 0)
			{
				world.setBlockState(pos, Blocks.dirt.getDefaultState());
			}
		}
	}

	/**
	 * @see ItemHoe#useHoe(ItemStack, EntityPlayer, World, BlockPos, IBlockState)
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getCurrentEquippedItem();

		if ((stack != null) && (stack.getItem() instanceof ItemHoe))
		{
			if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
			{
				return false;
			}

			if ((side != EnumFacing.DOWN) && world.isAirBlock(pos.up()))
			{
				IBlockState newState = Blocks.farmland.getDefaultState();

				double x = pos.getX() + 0.5F;
				double y = pos.getY() + 0.5F;
				double z = pos.getZ() + 0.5F;
				String soundName = newState.getBlock().stepSound.getStepSound();
				float volume = (newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F;
				float pitch = newState.getBlock().stepSound.getFrequency() * 0.8F;

				world.playSoundEffect(x, y, z, soundName, volume, pitch);

				if (!world.isRemote)
				{
					world.setBlockState(pos, newState);
					stack.damageItem(1, playerIn);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		if (renderPass == 1)
		{
			int color = super.colorMultiplier(worldIn, pos, renderPass);
			
			int r = (color & 16711680) >> 16;
			int g = (color & 65280) >> 8;
			int b = color & 255;
			
			float avgStage = 0;
			float stageSamples = 0;
			
			for (BlockPos checkPos : WorldUtils.getArea(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
			{
				IBlockState checkState = worldIn.getBlockState(checkPos);
				Block checkBlock = checkState.getBlock();
				
				if (checkBlock == this)
				{
					avgStage += (Integer) checkState.getValue(STAGE);
					stageSamples++;
				}
			}
			
			avgStage /= stageSamples;
			avgStage /= STAGE_LAST;
			avgStage = MathHelper.clamp_float(avgStage, 0, 1);
			
			BiomeGenBase biome = worldIn.getBiomeGenForCoords(pos);
			float temperature = MathHelper.clamp_float(biome.getFloatTemperature(pos), 0, 1);
			float humidity = MathHelper.clamp_float(biome.rainfall, 0, 1);
			
			int dryColor = biome.getModdedBiomeGrassColor(ColorizerDryMoss.getColor(temperature, humidity));
			int toR = (dryColor & 16711680) >> 16;
			int toG = (dryColor & 65280) >> 8;
			int toB = dryColor & 255;
			
			float amount = 1 - avgStage;
			
			r = r + (int) ((toR - r) * amount);	// Interpolate between the two color textures.
			g = g + (int) ((toG - g) * amount);
			b = b + (int) ((toB - b) * amount);
			
			color = ((r & 255) << 16) |
					((g & 255) << 8) |
					(b & 255);
			
			return color;
		}
		
		return getRenderColor(worldIn.getBlockState(pos));
	}

	@Override
	public int getRenderColor(IBlockState state)
	{
		return 16777215;
	}

	private boolean hasWater(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock().getMaterial() == Material.water;
	}
}
