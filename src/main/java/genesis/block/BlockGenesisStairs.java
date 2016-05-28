package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class BlockGenesisStairs extends BlockStairs
{
	public BlockGenesisStairs(IBlockState modelState)
	{
		super(modelState);
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		useNeighborBrightness = true;

		int encouragement = Blocks.fire.getEncouragement(modelState.getBlock());
		int flammability = Blocks.fire.getFlammability(modelState.getBlock());
		Blocks.fire.setFireInfo(this, encouragement, flammability);
	}
}
