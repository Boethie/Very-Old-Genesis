package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockPrototaxitesMycelium extends BlockMycelium
{
	public static final EnumPlantType SOIL_TYPE = EnumPlantType.getPlantType("prototaxitesMycelium");
	
	public BlockPrototaxitesMycelium()
	{
		setHardness(0.6F);
		setStepSound(soundTypeGrass);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			int light = worldIn.getLightFromNeighbors(pos.up());
			Block topBlock = worldIn.getBlockState(pos.up()).getBlock();

			if ((light < 4) && (topBlock.getLightOpacity(worldIn, pos.up()) > 2) && (topBlock != GenesisBlocks.prototaxites))
			{
				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
			}
			else
			{
				if (light >= 9)
				{
					for (int i = 0; i < 4; ++i)
					{
						BlockPos randPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
						IBlockState randState = worldIn.getBlockState(randPos);
						Block randBlock = worldIn.getBlockState(randPos.up()).getBlock();

						if ((randState.getBlock() == Blocks.dirt) && (randState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) && (worldIn.getLightFromNeighbors(randPos.up()) >= 4) && (randBlock.getLightOpacity(worldIn, randPos.up()) <= 2))
						{
							worldIn.setBlockState(randPos, getDefaultState());
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
	{
		return plantable.getPlantType(world, pos.offset(direction)) == SOIL_TYPE;
	}
}
