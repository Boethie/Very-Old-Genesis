package genesis.block;

import java.util.Random;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
import net.minecraft.block.BlockBed;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBenchSeat extends BlockBed {

	public BlockBenchSeat()
	{
		super();
		this.setHardness(3);
		this.setResistance(4);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, BlockBed.EnumPartType.FOOT).withProperty(OCCUPIED, Boolean.valueOf(false)));
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(PART) == BlockBed.EnumPartType.HEAD ? null : GenesisItems.bench_seat;
    }
	
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(GenesisItems.bench_seat);
    }
	
	public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player)
    {
        return this == GenesisBlocks.bench_seat;
    }
	
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, PART, OCCUPIED});
    }
}
