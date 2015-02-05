package genesis.block;

import genesis.client.ColorizerDryMoss;
import genesis.client.GenesisSounds;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.EnumFern;
import genesis.metadata.EnumPlant;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;
import genesis.util.Metadata;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.IPlantable;

public class BlockMoss extends BlockGrass
{
	protected static final int STAGE_LAST = 3;
	protected static final IProperty STAGE = PropertyInteger.create("stage", 0, STAGE_LAST);
	
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

					if ((worldIn.getBlockState(topBlock.down()).getBlock() == GenesisBlocks.moss) && !worldIn.getBlockState(topBlock).getBlock().isNormalCube())
					{
						++i;
						continue;
					}
				}
				else if (worldIn.isAirBlock(topBlock))
				{
					BlockPlant plant;
					IBlockState randPlant;

					if (rand.nextInt(8) == 0)
					{
						// Plant Flower
						plant = GenesisBlocks.plant;
						randPlant = plant.getDefaultState().withProperty(Constants.PLANT_VARIANT, Metadata.getRandom(EnumPlant.class));
					}
					else
					{
						// Plant Grass
						plant = GenesisBlocks.fern;
						randPlant = plant.getDefaultState().withProperty(Constants.FERN_VARIANT, Metadata.getRandom(EnumFern.class));
					}

					if (plant.canBlockStay(worldIn, topBlock, randPlant))
					{
						worldIn.setBlockState(topBlock, randPlant, 3);
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
	
	protected float[] lightGrowthChances = {
			-0.2F,	// 0
			-0.15F,	// 1
			-0.1F,	// 2
			-0.05F,	// 3
			0,		// 4
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
			-1F};	// 15
	
	protected float getGrowthChance(IBlockAccess worldIn, BlockPos pos, int light)
	{
		float chance = lightGrowthChances[light];
		
		float humidity = worldIn.getBiomeGenForCoords(pos).getFloatRainfall();
		chance += (humidity * 0.25F) - 0.125F;
		
		return chance;
	}
	
	protected float getGrowthChance(World worldIn, BlockPos pos)
	{
		return getGrowthChance(worldIn, pos, worldIn.getLightFromNeighbors(pos.up()));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			int newStage = (Integer) worldIn.getBlockState(pos).getValue(STAGE);
			
			float growthChance = getGrowthChance(worldIn, pos);
			float randFloat = rand.nextFloat();
			
			if (growthChance < 0 && randFloat - 1 > growthChance)
			{
				newStage--;
			}
			else if (randFloat < growthChance)
			{
				newStage++;
			}
			
			if (newStage < 0 && rand.nextInt(10) <= 0)
			{
				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
			}
			else
			{
				newStage = MathHelper.clamp_int(newStage, 0, STAGE_LAST);
				worldIn.setBlockState(pos, state.withProperty(STAGE, newStage));
	
				if (randFloat < growthChance)
				{
					float mult = 1;
					
					if (newStage < 1)
					{
						mult = 0;
					}
					
					mult = MathHelper.clamp_float(mult, 0, 1);
					
					for (int i = 0; i < 4 * mult; i++)
					{
						BlockPos randPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
						
						if (!randPos.equals(pos))
						{
							IBlockState randState = worldIn.getBlockState(randPos);
							Block randBlock = randState.getBlock();
	
							if (worldIn.getBlockState(randPos.up()).getBlock().getLightOpacity(worldIn, randPos.up()) <= 2)
							{
								if ((randState.getBlock() == Blocks.dirt) && (randState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT))
								{
									worldIn.setBlockState(randPos, GenesisBlocks.moss.getDefaultState());
								}
								else if (randBlock == this)
								{
									int randStage = (Integer) randState.getValue(STAGE);
									
									if (newStage >= STAGE_LAST && randStage < 2)
									{
										worldIn.setBlockState(randPos, randState.withProperty(STAGE, Math.min(randStage + 1, STAGE_LAST)));
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @see ItemHoe#useHoe(ItemStack, EntityPlayer, World, BlockPos, IBlockState)
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getCurrentEquippedItem();

		if ((stack != null) && (stack.getItem() instanceof ItemHoe))
		{
			if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
			{
				return false;
			}

			if ((side != EnumFacing.DOWN) && worldIn.isAirBlock(pos.up()))
			{
				IBlockState newState = Blocks.farmland.getDefaultState();

				double x = pos.getX() + 0.5F;
				double y = pos.getY() + 0.5F;
				double z = pos.getZ() + 0.5F;
				String soundName = newState.getBlock().stepSound.getStepSound();
				float volume = (newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F;
				float pitch = newState.getBlock().stepSound.getFrequency() * 0.8F;

				worldIn.playSoundEffect(x, y, z, soundName, volume, pitch);

				if (!worldIn.isRemote)
				{
					worldIn.setBlockState(pos, newState);
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
			
			float avgChance = 0;
			float chanceSamples = 0;
			
			float avgStage = 0;
			float stageSamples = 0;
			
	        for (BlockPos checkPos : (Iterable<BlockPos>) BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
	        {
	        	IBlockState checkState = worldIn.getBlockState(checkPos);
	        	Block checkBlock = checkState.getBlock();
	        	
	        	if (checkBlock == this || checkBlock == Blocks.dirt)
	        	{
					int combLight = worldIn.getCombinedLight(checkPos.up(), 0);
					int skyLight = (combLight >> 20) & 15;
					int blockLight = (combLight >> 4) & 15;
					float growthChance = getGrowthChance(worldIn, checkPos, skyLight);
					
					avgChance += growthChance;
					chanceSamples++;
	        	}
	        	
	        	if (checkBlock == this)
	        	{
					avgStage += (Integer) checkState.getValue(STAGE);
					stageSamples++;
	        	}
	        }

	        avgChance /= chanceSamples;
			avgChance = MathHelper.clamp_float(avgChance, 0, 1);
			
	        avgStage /= stageSamples;
			
			BiomeGenBase biome = worldIn.getBiomeGenForCoords(pos);
	        float temperature = MathHelper.clamp_float(biome.getFloatTemperature(pos), 0, 1);
	        float humidity = MathHelper.clamp_float(biome.getFloatRainfall(), 0, 1);
			
			int dryColor = biome.getModdedBiomeGrassColor(ColorizerDryMoss.getColor(temperature, humidity));
            int toR = (dryColor & 16711680) >> 16;
            int toG = (dryColor & 65280) >> 8;
			int toB = dryColor & 255;
			
			float amount = 1 - (avgStage / STAGE_LAST);

			r = r + (int) ((toR - r) * amount);
			g = g + (int) ((toG - g) * amount);
			b = b + (int) ((toB - b) * amount);
			
			// Desaturation code.
	        float saturation = 0.75F + (avgChance * 0.25F);
	        
			int[] rgb = {r, g, b};
			int smallest = 255;
			int biggest = 0;
			
			for (int curCol : rgb)
			{
				if (curCol < smallest)
				{
					smallest = curCol;
				}
				
				if (curCol > biggest)
				{
					biggest = curCol;
				}
			}
			
			float size = (biggest - (float) smallest);
			
			for (int i = 0; i < rgb.length; i++)
			{
				float part = (rgb[i] - biggest) / size;
				rgb[i] = (int) ((part * saturation) * size) + biggest;
			}

			// Update r, g, and b with color from desaturation code
			r = rgb[0];
			g = rgb[1];
			b = rgb[2];

			r = MathHelper.clamp_int(r, 0, 255);
			g = MathHelper.clamp_int(g, 0, 255);
			b = MathHelper.clamp_int(b, 0, 255);

	        color = ((r & 255) << 16) |
	        		((g & 255) << 8) |
	        		(b & 255);
			
			return color;
		}
		else
		{
			return getRenderColor(worldIn.getBlockState(pos));
		}
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
