package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.common.sounds.GenesisSoundTypes;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockFrullania extends BlockVine
{
	public enum EnumPosition implements IStringSerializable
	{
		BOTTOM("bottom"),
		MIDDLE("middle"),
		TOP("top");
		
		private final String name;
		
		EnumPosition(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return name;
		}
	}
	
	public static final PropertyEnum<EnumPosition> POSITION = PropertyEnum.create("position", EnumPosition.class);
	
	public BlockFrullania()
	{
		blockState = new BlockStateContainer(this, UP, NORTH, EAST, SOUTH, WEST, POSITION);
		setDefaultState(blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(POSITION, EnumPosition.BOTTOM));
		setHardness(0.2F);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setSoundType(GenesisSoundTypes.FERN);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		
		if (!state.getValue(UP))
		{
			if (world.getBlockState(pos.down()).getBlock() != this)
				return state.withProperty(POSITION, EnumPosition.BOTTOM);
			
			if (world.getBlockState(pos.up()).getBlock() != this)
				return state.withProperty(POSITION, EnumPosition.TOP);
		}
		
		return state.withProperty(POSITION, EnumPosition.MIDDLE);
	}
}
