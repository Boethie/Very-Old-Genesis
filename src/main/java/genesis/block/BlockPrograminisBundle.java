package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockHay;
import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;

public class BlockPrograminisBundle extends BlockHay
{
	public BlockPrograminisBundle()
	{
		setHardness(0.5F);
		setSoundType(SoundType.PLANT);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		Blocks.FIRE.setFireInfo(this, 60, 20);
	}
}
