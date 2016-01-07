package genesis.block;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockDungBrick extends BlockGenesis
{
	public BlockDungBrick()
	{
		super(Material.rock);
		setHardness(0.7F);
		setStepSound(soundTypePiston);
		setHarvestLevel("pickaxe", 0);
		Blocks.fire.setFireInfo(this, 5, 5);
	}
}
