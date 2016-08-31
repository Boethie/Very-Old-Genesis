package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.util.WorldUtils;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockPrototaxitesMycelium extends BlockMycelium
{
	public static final EnumPlantType SOIL_TYPE = EnumPlantType.getPlantType("prototaxitesMycelium");
	
	public BlockPrototaxitesMycelium()
	{
		setHardness(0.6F);
		setSoundType(SoundType.PLANT);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote)
		{
			int light = world.getLightFromNeighbors(pos.up());
			IBlockState topState = world.getBlockState(pos.up());
			
			if ((light < 4)
					&& (topState.getLightOpacity(world, pos.up()) > 2)
					&& (topState.getBlock() != GenesisBlocks.prototaxites))
			{
				world.setBlockState(pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
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
						
						if (randState.getBlock() == Blocks.DIRT
								&& randState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT
								&& world.getLightFromNeighbors(above) >= 4
								&& world.getBlockState(above).getLightOpacity(world, above) <= 2)
						{
							world.setBlockState(randPos, getDefaultState());
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		EnumPlantType type = plantable.getPlantType(world, pos.offset(direction));
		return type == SOIL_TYPE
			|| type == EnumPlantType.Plains;
	}
}
