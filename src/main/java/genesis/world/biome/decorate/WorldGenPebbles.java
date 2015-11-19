package genesis.world.biome.decorate;

import genesis.block.BlockGenesisPebble;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import genesis.metadata.EnumSilt;
import genesis.metadata.EnumToolMaterial;
import genesis.metadata.SiltBlocks;
import genesis.metadata.ToolItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenPebbles extends WorldGenDecorationBase
{
	public List<EnumToolMaterial> pebbleTypes = new ArrayList<EnumToolMaterial>();
	
	private boolean waterRequired = true;
	private List<PropertyBool> pebblePositions;
	
	public WorldGenPebbles()
	{
		
		pebbleTypes.add(EnumToolMaterial.DOLERITE);
		pebbleTypes.add(EnumToolMaterial.RHYOLITE);
		pebbleTypes.add(EnumToolMaterial.GRANITE);
		pebbleTypes.add(EnumToolMaterial.QUARTZ);
		pebbleTypes.add(EnumToolMaterial.BROWN_FLINT);
		pebbleTypes.add(EnumToolMaterial.BLACK_FLINT);
	}
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = getPosition(world, pos);
		Block block = world.getBlockState(pos).getBlock();
		
		if (
				!(block == GenesisBlocks.moss 
				|| block == Blocks.dirt
				|| block == GenesisBlocks.limestone
				|| block == GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT)))
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		boolean waterExists = findBlockInRange(world, pos, Blocks.water.getDefaultState(), 4, 3, 4);
		
		if (!waterExists && waterRequired)
			return false;
		
		populatePositions();
		
		int maxPebbles = random.nextInt(2) + random.nextInt(2) + random.nextInt(2);
		IBlockState pebble = GenesisItems.tools.getBlockState(ToolItems.PEBBLE, pebbleTypes.get(random.nextInt(pebbleTypes.size()))).withProperty(getPosition(random), true);
		
		for (int i = 1; i <= maxPebbles; ++i)
		{
			pebble = pebble.withProperty(getPosition(random), true);
		}
		
		setBlockInWorld(world, pos.up(), pebble);
		
		return true;
	}
	
	public WorldGenPebbles setWaterRequired(boolean required)
	{
		waterRequired = required;
		return this;
	}
	
	private void populatePositions()
	{
		pebblePositions = new ArrayList<PropertyBool>();
		
		pebblePositions.add(BlockGenesisPebble.NE);
		pebblePositions.add(BlockGenesisPebble.NW);
		pebblePositions.add(BlockGenesisPebble.SE);
		pebblePositions.add(BlockGenesisPebble.SW);
	}
	
	private PropertyBool getPosition(Random rand)
	{
		int index = rand.nextInt(pebblePositions.size());
		PropertyBool pos = pebblePositions.get(index);
		pebblePositions.remove(index);
		return pos;
	}
}
