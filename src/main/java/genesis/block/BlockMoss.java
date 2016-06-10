package genesis.block;

import genesis.common.*;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.util.*;
import genesis.util.random.i.IntRange;
import genesis.world.biome.BiomeGenBaseGenesis;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMoss extends BlockGrass
{
	public enum EnumSoil implements IStringSerializable
	{
		NORMAL("normal", 0),
		HUMUS("humus", 1);
		
		private final String name;
		private final int metadata;
		
		EnumSoil(String name, int metadata)
		{
			this.name = name;
			this.metadata = metadata;
		}
		
		public int getMetadata()
		{
			return metadata;
		}
		
		@Override
		public String getName()
		{
			return name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
	
	public static final PropertyEnum<EnumSoil> DIRT = PropertyEnum.create("soil", EnumSoil.class);
	public static final int STAGE_LAST = 3;
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, STAGE_LAST);
	
	public BlockMoss()
	{
		setDefaultState(blockState.getBaseState().withProperty(DIRT, EnumSoil.NORMAL).withProperty(STAGE, 0).withProperty(SNOWY, false));
		setHardness(0.6F);
		setSoundType(GenesisSoundTypes.MOSS);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setHarvestLevel("shovel", 0);
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DIRT, STAGE, SNOWY);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, DIRT, STAGE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, DIRT, STAGE);
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		BiomeGenBase biome = world.getBiomeGenForCoords(pos);
		BiomeGenBaseGenesis biomeGenesis = null;
		
		if (biome instanceof BiomeGenBaseGenesis)
		{
			biomeGenesis = (BiomeGenBaseGenesis) biome;
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
						world.getBiomeGenForCoords(plantPos).plantFlower(world, rand, plantPos);
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
							world.setBlockState(plantPos, Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS));
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
		case Desert:
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
		world.setBlockState(pos, Blocks.dirt.getDefaultState(), 2);
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
		
		if (stateAbove.getMaterial() == Material.water)
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
			if (sample.distanceSq(pos) <= rad * rad && world.getBlockState(sample).getMaterial() == Material.water)
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
	
	public float getFertility(World world, BlockPos pos)
	{
		return getFertility(world, pos, false);
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
		float humidity = world.getBiomeGenForCoords(pos).getRainfall();
		float chance = 1 - growthChanceHumidityEffect + (humidity * growthChanceHumidityEffect * (dying ? -2 : 1));
		
		return chance * growthChanceMult;
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote)
		{
			int stage = state.getValue(STAGE);
			
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
						
						if (randState == Blocks.dirt.getDefaultState() || randState.getBlock() == Blocks.grass)
						{
							if (getGrowthChance(world, randPos, false) < rand.nextFloat() ||
									getTargetStage(getFertility(world, randPos), world.rand) < 0)
								continue;
							
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
	 * @see ItemHoe#onItemUse
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumHand hand, ItemStack held,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (held != null && (held.getItem() instanceof ItemHoe))
		{
			if (!player.canPlayerEdit(pos.offset(side), side, held))
			{
				return false;
			}
			
			if ((side != EnumFacing.DOWN) && world.isAirBlock(pos.up()))
			{
				IBlockState newState = Blocks.farmland.getDefaultState();
				
				double x = pos.getX() + 0.5F;
				double y = pos.getY() + 0.5F;
				double z = pos.getZ() + 0.5F;
				
				world.playSound(player, x, y, z, SoundEvents.item_hoe_till, SoundCategory.BLOCKS, 1, 1);
				
				if (!world.isRemote)
				{
					world.setBlockState(pos, newState);
					held.damageItem(1, player);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (EnumSoil soil : EnumSoil.values())
			list.add(new ItemStack(item, 1, soil.getMetadata()));
	}
}
