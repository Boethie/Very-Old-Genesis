package genesis.world.biome.decorate;

import genesis.block.BlockAquaticPlant;
import genesis.combo.variant.EnumAquaticPlant;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;

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
	
	public WorldGenAquaticPlants(EnumAquaticPlant bottom, EnumAquaticPlant top)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER, WorldBlockMatcher.TRUE);
		
		this.bottom = bottom;
		this.top = top;
	}
	
	public WorldGenAquaticPlants(EnumAquaticPlant bottom)
	{
		this(bottom, null);
	}
	
	@Override
	public boolean place(World world, Random random, BlockPos pos)
	{
		BlockPos checkPos = pos;
		
		// Check plant bottom position.
		if (world.getBlockState(checkPos).getBlock() != Blocks.WATER)
			return false;
		
		// Check plant top position if the plant has a top.
		if (top != null && world.getBlockState(checkPos = checkPos.up()).getBlock() != Blocks.WATER)
			return false;
		
		// Check for water above the plant.
		if (world.getBlockState(checkPos = checkPos.up()).getMaterial() != Material.WATER)
			return false;
		
		IBlockState bottomState = GenesisBlocks.AQUATIC_PLANTS.getBlockState(bottom);
		
		if (!((BlockAquaticPlant) bottomState.getBlock()).canBlockStay(world, pos, bottomState))
			return false;
		
		setBlock(world, pos, bottomState);
		
		if (top != null)
			setReplaceableBlock(world, pos.up(), GenesisBlocks.AQUATIC_PLANTS.getBlockState(top));
		
		return true;
	}
}
