package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStemonitis extends BlockGenesisMushroom
{
	public BlockStemonitis()
	{
		super(BlockGenesisMushroom.MushroomGrowType.GROW_TOP);
		setBoundsSize(0.375F, 0.75F, 0);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}

	@Override
	protected boolean canGrowOnTop(World world, BlockPos pos, IBlockState state, BlockPos bottomPos, IBlockState bottomState)
	{
		Block bottomBlock = bottomState.getBlock();
		return bottomBlock instanceof BlockLog;
	}
}
