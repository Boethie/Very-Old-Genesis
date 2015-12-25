package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.util.WorldUtils;

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
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote)
		{
			int light = world.getLightFromNeighbors(pos.up());
			Block topBlock = world.getBlockState(pos.up()).getBlock();

			if ((light < 4) && (topBlock.getLightOpacity(world, pos.up()) > 2) && (topBlock != GenesisBlocks.prototaxites))
			{
				world.setBlockState(pos, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
			}
			else
			{
				int areaMycelium = 10;
				
				for (BlockPos areaPos : WorldUtils.getArea(pos, 10))
				{
					if (world.getBlockState(areaPos).getBlock() == this)
					{
						areaMycelium--;
						
						if (areaMycelium <= 0)
							return;
					}
				}
				
				if (light >= 9)
				{
					for (int i = 0; i < 4; ++i)
					{
						BlockPos randPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
						IBlockState randState = world.getBlockState(randPos);
						
						BlockPos above = randPos.up();
						Block aboveBlock = world.getBlockState(above).getBlock();
						
						if (randState.getBlock() == Blocks.dirt
								&& randState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT
								&& world.getLightFromNeighbors(above) >= 4
								&& aboveBlock.getLightOpacity(world, above) <= 2)
						{
							world.setBlockState(randPos, getDefaultState());
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
	{
		EnumPlantType type = plantable.getPlantType(world, pos.offset(direction));
		return type == SOIL_TYPE
			|| type == EnumPlantType.Plains;
	}
}
