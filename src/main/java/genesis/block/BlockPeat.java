package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockPeat extends BlockGenesis
{
	public BlockPeat()
	{
		super(Material.ground, SoundType.GROUND);
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		
		setHarvestLevel("shovel", 0);
		setHardness(1.0F);
		
		Blocks.fire.setFireInfo(this, 5, 5);
	}
}
