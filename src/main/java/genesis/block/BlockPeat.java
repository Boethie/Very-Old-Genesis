package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockPeat extends BlockGenesis
{
	public BlockPeat()
	{
		super(Material.ground);
		setStepSound(Block.soundTypeGravel);
		setHarvestLevel("shovel", 0);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setHardness(1.0F);
		Blocks.fire.setFireInfo(this, 5, 5);
	}
}
