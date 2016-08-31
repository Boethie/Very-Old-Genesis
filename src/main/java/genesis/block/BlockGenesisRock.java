package genesis.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockGenesisRock extends BlockGenesis
{
	public BlockGenesisRock(float hardness, float resistance)
	{
		this(hardness, resistance, 0);
	}

	public BlockGenesisRock(float hardness, float resistance, int harvestLevel)
	{
		super(Material.ROCK, SoundType.STONE);
		
		setHardness(hardness);
		setResistance(resistance);
		setHarvestLevel("pickaxe", harvestLevel);
	}
}
