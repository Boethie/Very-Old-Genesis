package genesis.block;

import java.util.Random;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBenchSeat extends BlockBed
{
	public BlockBenchSeat()
	{
		setHardness(3);
		setResistance(4);
		setDefaultState(blockState.getBaseState().withProperty(PART, EnumPartType.FOOT).withProperty(OCCUPIED, false));
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return state.getValue(PART) == EnumPartType.HEAD ? null : Item.getItemFromBlock(this);
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this);
	}
	
	@Override
	public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player)
	{
		return true;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, PART, OCCUPIED);
	}
}
