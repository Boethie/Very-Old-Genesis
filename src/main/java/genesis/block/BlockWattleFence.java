package genesis.block;

import java.util.List;

import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.PropertyIMetadata;
import genesis.item.ItemBlockMulti;
import genesis.util.*;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWattleFence extends BlockGenesisFence
{
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{};
	}
	
	// Side connection properties.
	public enum EnumConnectState implements IStringSerializable
	{
		NONE, SIDE, SIDE_TOP, SIDE_BOTTOM, SIDE_TOP_BOTTOM;
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}
	
	// TODO: Use FacingProperties
	public static final PropertyEnum<EnumConnectState> NORTH = PropertyEnum.create("north", EnumConnectState.class);
	public static final PropertyEnum<EnumConnectState> EAST = PropertyEnum.create("east", EnumConnectState.class);
	public static final PropertyEnum<EnumConnectState> SOUTH = PropertyEnum.create("south", EnumConnectState.class);
	public static final PropertyEnum<EnumConnectState> WEST = PropertyEnum.create("west", EnumConnectState.class);
	
	// Fields specific to this instance.
	public final TreeBlocksAndItems owner;
	public final ObjectType<EnumTree, BlockWattleFence, ItemBlockMulti<EnumTree>> type;
	
	public final PropertyIMetadata<EnumTree> variantProp;
	public final List<EnumTree> variants;
	
	public BlockWattleFence(TreeBlocksAndItems owner,
			ObjectType<EnumTree, BlockWattleFence, ItemBlockMulti<EnumTree>> type,
			List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super(Material.WOOD, 0.125F, 1.0F, 0.125F, 0.875F, 1.5F);
		
		setHardness(2);
		setResistance(5);
		setSoundType(SoundType.WOOD);
		
		this.owner = owner;
		this.type = type;
		
		variantProp = new PropertyIMetadata<>("variant", variants, variantClass);
		this.variants = variants;
		
		blockState = new BlockStateContainer(this, variantProp, NORTH, EAST, SOUTH, WEST);
		setDefaultState(getBlockState().getBaseState());
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, variantProp);
	}

	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata, variantProp);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, state.getValue(variantProp));
	}
	
	protected IBlockState setSideState(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side, PropertyEnum<EnumConnectState> property, boolean above, boolean below)
	{
		EnumConnectState sideState = EnumConnectState.NONE;
		BlockPos sidePos = pos.offset(side);
		
		if (canConnectTo(world, pos, side))
		{	// TODO: Improve for T shapes with connections on the sides
			boolean up = above && canConnectTo(world, sidePos, EnumFacing.UP);
			boolean down = below && canConnectTo(world, sidePos, EnumFacing.DOWN);
			
			if (up && down)
			{
				sideState = EnumConnectState.SIDE_TOP_BOTTOM;
			}
			else if (up)
			{
				sideState = EnumConnectState.SIDE_BOTTOM;
			}
			else if (down)
			{
				sideState = EnumConnectState.SIDE_TOP;
			}
			else
			{
				sideState = EnumConnectState.SIDE;
			}
		}
		
		return state.withProperty(property, sideState);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		boolean fenceAbove = owner.containsBlockState(type, world.getBlockState(pos.up()));
		boolean fenceBelow = owner.containsBlockState(type, world.getBlockState(pos.down()));
		
		state = setSideState(world, state, pos, EnumFacing.NORTH, NORTH, fenceAbove, fenceBelow);
		state = setSideState(world, state, pos, EnumFacing.EAST, EAST, fenceAbove, fenceBelow);
		state = setSideState(world, state, pos, EnumFacing.SOUTH, SOUTH, fenceAbove, fenceBelow);
		state = setSideState(world, state, pos, EnumFacing.WEST, WEST, fenceAbove, fenceBelow);
		return state;
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
}
