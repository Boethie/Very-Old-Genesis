package genesis.block;

import genesis.client.GenesisSounds;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.EnumFern;
import genesis.metadata.EnumPlant;
import genesis.util.Constants;
import genesis.util.Metadata;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockMoss extends BlockGrass
{
	public BlockMoss()
	{
		setHardness(0.6F);
		setStepSound(GenesisSounds.MOSS);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setHarvestLevel("shovel", 0);
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

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			if ((worldIn.getLightFromNeighbors(pos.up()) < 4) && (worldIn.getBlockState(pos.up()).getBlock().getLightOpacity(worldIn, pos.up()) > 2))
			{
				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
			}
			else if (worldIn.getLightFromNeighbors(pos.up()) <= 14)
			{
				for (int i = 0; i < 4; ++i)
				{
					BlockPos randPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
					IBlockState randBlock = worldIn.getBlockState(randPos);

					Block block = worldIn.getBlockState(randPos.up()).getBlock();

					if ((randBlock.getBlock() == Blocks.dirt) && (randBlock.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) && (worldIn.getLightFromNeighbors(randPos.up()) <= 14) && (block.getLightOpacity(worldIn, randPos.up()) <= 2))
					{
						worldIn.setBlockState(randPos, GenesisBlocks.moss.getDefaultState());
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
			return super.colorMultiplier(worldIn, pos, renderPass);
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
