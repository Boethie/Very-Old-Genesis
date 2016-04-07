package genesis.world.biome.decorate;

import genesis.block.BlockPebble;
import genesis.combo.ToolItems;
import genesis.combo.variant.EnumToolMaterial;
import genesis.common.GenesisItems;
import genesis.util.WorldBlockMatcher;
import genesis.util.WorldUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenPebbles extends WorldGenDecorationBase
{
	public List<EnumToolMaterial> pebbleTypes = new ArrayList<EnumToolMaterial>();
	
	protected List<PropertyBool> pebblePositions = ImmutableList.of(BlockPebble.NE, BlockPebble.SE, BlockPebble.SW, BlockPebble.NW);
	private boolean waterRequired = true;
	
	public WorldGenPebbles()
	{
		super(WorldBlockMatcher.AIR_LEAVES, WorldBlockMatcher.SOLID_TOP);
		
		pebbleTypes.add(EnumToolMaterial.DOLERITE);
		pebbleTypes.add(EnumToolMaterial.RHYOLITE);
		pebbleTypes.add(EnumToolMaterial.GRANITE);
		pebbleTypes.add(EnumToolMaterial.QUARTZ);
		pebbleTypes.add(EnumToolMaterial.BROWN_FLINT);
		pebbleTypes.add(EnumToolMaterial.BLACK_FLINT);
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (!state.getBlock().isAir(state, world, pos))
			return false;
		
		if (waterRequired && !WorldUtils.waterInRange(world, pos, 4, 3, 4))
			return false;
		
		if (rand.nextInt(rarity) != 0)
			return false;
		
		int maxPebbles = rand.nextInt(2) + rand.nextInt(2) + rand.nextInt(2);
		IBlockState pebble = GenesisItems.tools.getBlockState(ToolItems.PEBBLE, pebbleTypes.get(rand.nextInt(pebbleTypes.size()))).withProperty(getPosition(rand), true);
		
		for (int i = 1; i <= maxPebbles; ++i)
		{
			pebble = pebble.withProperty(getPosition(rand), true);
		}
		
		setBlockInWorld(world, pos.up(), pebble);
		
		return true;
	}
	
	public WorldGenPebbles setWaterRequired(boolean required)
	{
		waterRequired = required;
		return this;
	}
	
	private PropertyBool getPosition(Random rand)
	{
		int index = rand.nextInt(pebblePositions.size());
		PropertyBool pos = pebblePositions.get(index);
		//pebblePositions.remove(index);
		return pos;
	}
}
