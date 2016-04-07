package genesis.world.biome.decorate;

import genesis.block.BlockAquaticPlant;
import genesis.combo.variant.EnumAquaticPlant;
import genesis.common.GenesisBlocks;
import genesis.util.WorldBlockMatcher;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenAquaticPlants extends WorldGenDecorationBase
{
	protected final EnumAquaticPlant bottom;
	protected final EnumAquaticPlant top;
	private boolean generateInGroup = false;
	private int groupSize = 1;
	
	public WorldGenAquaticPlants(EnumAquaticPlant bottom, EnumAquaticPlant top)
	{
		super(WorldBlockMatcher.AIR_WATER_LEAVES, WorldBlockMatcher.TRUE);
		
		this.bottom = bottom;
		this.top = top;
	}
	
	public WorldGenAquaticPlants(EnumAquaticPlant bottom)
	{
		this(bottom, null);
	}
	
	protected boolean tryPlace(World world, BlockPos pos)
	{
		IBlockState bottomState = GenesisBlocks.aquatic_plants.getBlockState(bottom);
		
		BlockPos checkPos = pos;
		
		// Check plant bottom position.
		if (world.getBlockState(checkPos).getBlock() != Blocks.water)
			return false;
		
		// Check plant top position if the plant has a top.
		if (top != null && world.getBlockState(checkPos = checkPos.up()).getBlock() != Blocks.water)
			return false;
		
		// Check for water above the plant.
		if (world.getBlockState(checkPos = checkPos.up()).getMaterial() != Material.water)
			return false;
		
		if (!((BlockAquaticPlant) bottomState.getBlock()).canBlockStay(world, pos, bottomState))
			return false;
		
		setBlockInWorld(world, pos, bottomState);
		
		if (top != null)
			setBlockInWorld(world, pos.up(), GenesisBlocks.aquatic_plants.getBlockState(top));
		
		return true;
	}
	
	@Override
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		boolean success = tryPlace(world, pos);
		
		BlockPos additionalPos;
		
		if (generateInGroup)
		{
			for (int i = 1; i <= groupSize - 1; ++i)
			{
				additionalPos = pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
				if (tryPlace(world, additionalPos))
					success = true;
			}
		}
		
		return success;
	}
	
	public WorldGenAquaticPlants setGenerateInGroup(boolean group, int size)
	{
		generateInGroup = group;
		groupSize = size;
		return this;
	}
}
