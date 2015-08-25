package genesis.block;

import java.util.ArrayList;
import java.util.Random;

import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms.CanStayOptions;
import genesis.common.GenesisBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class SurviveOnDirtCustoms implements IGrowingPlantCustoms
{
    @Override
    public ArrayList<ItemStack> getPlantDrops(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, int fortune, boolean firstBlock)
    {
        return null;
    }
    
    @Override
    public void plantUpdateTick(BlockGrowingPlant plant, World world, BlockPos pos, IBlockState state, Random rand, boolean grew)
    {
    }
    
    @Override
    public void managePlantMetaProperties(BlockGrowingPlant plant, ArrayList<IProperty> metaProps)
    {
    }
    
    @Override
    public CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World world, BlockPos pos, boolean placed)
    {
        if (placed)
        {
            Block block = world.getBlockState(pos.down()).getBlock();
            
            if (block == Blocks.grass || block == Blocks.dirt || block == GenesisBlocks.moss)
            {
                return CanStayOptions.YES;
            }
        }
        
        return CanStayOptions.YIELD;
    }
	
	@Override
	public boolean shouldUseBonemeal(World world, BlockPos pos, IBlockState state)
	{
		return true;
	}
}
