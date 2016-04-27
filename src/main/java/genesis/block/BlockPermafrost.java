package genesis.block;

import genesis.sounds.GenesisSoundTypes;
import net.minecraft.block.material.Material;

public class BlockPermafrost extends BlockGenesis
{
	public BlockPermafrost()
	{
		super(Material.rock, GenesisSoundTypes.PERMAFROST);
		
		slipperiness = 0.98F;
		setHardness(0.5F);
		setSoundType(GenesisSoundTypes.PERMAFROST);
		setHarvestLevel("pickaxe", 0);
	}
}
