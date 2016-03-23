package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockGenesisStairs extends BlockStairs
{
	public BlockGenesisStairs(IBlockState modelState)
	{
		super(modelState);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
}
