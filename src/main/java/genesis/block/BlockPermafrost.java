package genesis.block;

import net.minecraft.block.material.Material;

public class BlockPermafrost extends BlockGenesis
{
	public BlockPermafrost()
	{
		super(Material.rock);
		slipperiness = 0.98F;
		setHardness(0.5F);
		setHarvestLevel("pickaxe", 0);
	}
}
