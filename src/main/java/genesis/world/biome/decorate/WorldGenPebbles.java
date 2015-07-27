package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import genesis.metadata.EnumToolMaterial;
import genesis.metadata.ToolItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenPebbles extends WorldGenDecorationBase
{
	public List<EnumToolMaterial> pebbleTypes = new ArrayList<EnumToolMaterial>();
	
	public WorldGenPebbles()
	{
		pebbleTypes.add(EnumToolMaterial.BLACK_FLINT);
		pebbleTypes.add(EnumToolMaterial.BROWN_FLINT);
		pebbleTypes.add(EnumToolMaterial.DOLERITE);
		pebbleTypes.add(EnumToolMaterial.GRANITE);
		pebbleTypes.add(EnumToolMaterial.QUARTZITE);
		pebbleTypes.add(EnumToolMaterial.RHYOLITE);
	}
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = getPosition(world, pos);
		Block block = world.getBlockState(pos).getBlock();
		
		if (
				!(block == GenesisBlocks.moss 
				|| block == Blocks.dirt
				|| block == GenesisBlocks.limestone))
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		boolean water_exists = findBlockInRange(world, pos, Blocks.water.getDefaultState(), 3, 2, 3);
		
		if (!water_exists)
			return false;
		
		setBlockInWorld(world, pos.up(), GenesisItems.tools.getBlockState(ToolItems.PEBBLE, pebbleTypes.get(random.nextInt(pebbleTypes.size()))));
		
		return true;
	}
}
