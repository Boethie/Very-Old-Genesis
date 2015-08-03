package genesis.block;

import java.util.List;

import genesis.client.*;
import genesis.client.model.*;
import genesis.common.*;
import genesis.item.ItemBlockMulti;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWattleFence extends BlockFence
{
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	// Side connection properties.
	public static enum EnumConnectState implements IStringSerializable
	{
		NONE, SIDE, SIDE_TOP, SIDE_BOTTOM, SIDE_TOP_BOTTOM;
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}

	public static final PropertyEnum NORTH = PropertyEnum.create("north", EnumConnectState.class);
	public static final PropertyEnum EAST = PropertyEnum.create("east", EnumConnectState.class);
	public static final PropertyEnum SOUTH = PropertyEnum.create("south", EnumConnectState.class);
	public static final PropertyEnum WEST = PropertyEnum.create("west", EnumConnectState.class);
	
	// Fields specific to this instance.
	public final TreeBlocksAndItems owner;
	public final ObjectType<BlockWattleFence, ItemBlockMulti> type;
	
	public final PropertyIMetadata<EnumTree> variantProp;
	public final List<EnumTree> variants;
	
	public BlockWattleFence(List<EnumTree> variants, TreeBlocksAndItems owner, ObjectType<BlockWattleFence, ItemBlockMulti> type)
	{
		super(Material.wood);
		
		setHardness(2);
		setResistance(5);
		setStepSound(soundTypeWood);
		
		this.owner = owner;
		this.type = type;
		
		variantProp = new PropertyIMetadata<EnumTree>("variant", variants);
		this.variants = variants;
		
		blockState = new BlockState(this, variantProp, NORTH, EAST, SOUTH, WEST);
		setDefaultState(getBlockState().getBaseState());
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
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
		return owner.getItemMetadata(type, (EnumTree) state.getValue(variantProp));
	}
	
	protected IBlockState setSideState(IBlockAccess world, IBlockState state, BlockPos sidePos, PropertyEnum property, boolean above, boolean below)
	{
		EnumConnectState sideState = EnumConnectState.NONE;
		
		if (canConnectTo(world, sidePos))
		{
			boolean up = above && canConnectTo(world, sidePos.up());
			boolean down = below && canConnectTo(world, sidePos.down());
			
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
	
	public static boolean isSameVariant(IBlockState state1, IBlockState state2)
	{
		Block block1 = state1.getBlock();
		
		if (block1 != state2.getBlock())
		{
			return false;
		}
		
		if (!(block1 instanceof BlockWattleFence))
		{
			return false;
		}
		
		BlockWattleFence fence = (BlockWattleFence) block1;
		
		return state1.getValue(fence.variantProp) == state2.getValue(fence.variantProp);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		IBlockState above = world.getBlockState(pos.up());
		boolean fenceAbove = (above.getBlock() == this && above.getValue(variantProp) == state.getValue(variantProp));
		IBlockState below = world.getBlockState(pos.down());
		boolean fenceBelow = (below.getBlock() == this && below.getValue(variantProp) == state.getValue(variantProp));
		
		state = setSideState(world, state, pos.north(), NORTH, fenceAbove, fenceBelow);
		state = setSideState(world, state, pos.east(), EAST, fenceAbove, fenceBelow);
		state = setSideState(world, state, pos.south(), SOUTH, fenceAbove, fenceBelow);
		state = setSideState(world, state, pos.west(), WEST, fenceAbove, fenceBelow);
		
		return state;
	}
	
	@Override
	public BlockWattleFence setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(Unlocalized.PREFIX + name);
		
		return this;
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		for (EnumTree treeType : variants)
		{
			list.add(new ItemStack(itemIn, 1, variants.indexOf(treeType)));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		
		if (collidingEntity instanceof EntityFX)  // Make particles collide with the actual top of the fence, rather than the raised version.
		{
			for (int i = 0; i < list.size(); i++)
			{
				AxisAlignedBB bb = (AxisAlignedBB) list.get(i);
				bb = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY - 0.5, bb.maxZ);
				list.set(i, bb);
			}
		}
	}
}
