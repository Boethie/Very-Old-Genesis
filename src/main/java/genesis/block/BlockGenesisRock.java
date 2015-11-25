package genesis.block;

import net.minecraft.block.material.Material;

public class BlockGenesisRock extends BlockGenesis
{
	public BlockGenesisRock(float hardness, float resistance)
	{
		this(hardness, resistance, 0);
	}

	public BlockGenesisRock(float hardness, float resistance, int harvestLevel)
	{
		super(Material.rock);
		setHardness(hardness);
		setResistance(resistance);
		setStepSound(soundTypePiston);
		setHarvestLevel("pickaxe", harvestLevel);
	}
}
