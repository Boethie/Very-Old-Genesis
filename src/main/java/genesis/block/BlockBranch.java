package genesis.block;

import java.util.List;

import genesis.combo.ObjectType;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumTree;
import genesis.item.ItemBlockMulti;
import genesis.util.AABBUtils;
import genesis.util.BlockStateToMetadata;
import genesis.util.blocks.FacingProperties;

import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockBranch extends BlockGenesisLogs
{
	@BlockProperties
	public static final IProperty<?>[] STORE_PROPERTIES = {};
	
	public static final FacingProperties<Boolean> CONNECTIONS =
			FacingProperties.create((f) -> PropertyBool.create(f.getName()));
	public static final PropertyBool LEAVES = PropertyBool.create("leaves");
	
	public static final float RADIUS = 0.0625F * 3;
	
	public final ObjectType<EnumTree, BlockBranch, ItemBlockMulti<EnumTree>> type;
	
	public BlockBranch(TreeBlocksAndItems owner,
			ObjectType<EnumTree, BlockBranch, ItemBlockMulti<EnumTree>> type,
			List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super(owner, type, variants, variantClass);
		
		this.type = type;
		
		blockState = new BlockStateContainer(this, CONNECTIONS.toArrayWith(LEAVES, variantProp));
		setDefaultState(CONNECTIONS.stateWith(getBlockState().getBaseState(), false).withProperty(LEAVES, false));
		
		Blocks.FIRE.setFireInfo(this, 30, 60);
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing,
			float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return owner.getBlockState(type, owner.getVariant(this, meta));
	}
	
	public boolean hasLeaves(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		EnumTree variant = state.getValue(variantProp);
		int neighbors = 0;
		
		for (EnumFacing facing : CONNECTIONS.facings())
			if (owner.isStateOf(world.getBlockState(pos.offset(facing)), variant,
					type, TreeBlocksAndItems.LEAVES, TreeBlocksAndItems.LEAVES_FRUIT)
					&& ++neighbors >= 2)
				return true;
		
		return false;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		EnumTree variant = state.getValue(variantProp);
		boolean leaves = hasLeaves(world, pos, state);
		
		return CONNECTIONS.stateWith(state,
				(f) -> {
					BlockPos sidePos = pos.offset(f);
					IBlockState sideState = world.getBlockState(sidePos);
					
					if (owner.isStateOf(sideState, variant, type))
						return true;
					
					if (f == EnumFacing.DOWN && !(sideState.getBlock() instanceof BlockBranch) &&
							sideState.getBlock().isSideSolid(sideState, world, sidePos, f.getOpposite()))
						return true;
					
					return false;
				}).withProperty(LEAVES, leaves);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
		
		IBlockState leaves = owner.getBlockState(TreeBlocksAndItems.LEAVES, state.getValue(variantProp));
		ret.addAll(leaves.getBlock().getDrops(world, pos, leaves, fortune));
		
		return ret;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = state.getActualState(world, pos);
		
		AxisAlignedBB bb = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
		
		if (!state.getValue(LEAVES))
		{
			bb = new AxisAlignedBB(0.5 - RADIUS, 0.5 - RADIUS, 0.5 - RADIUS, 0.5 + RADIUS, 0.5 + RADIUS, 0.5 + RADIUS);
			double add = 0.5 - RADIUS;
			
			for (FacingProperties.Entry<Boolean> entry : CONNECTIONS)
			{
				if (state.getValue(entry.property))
				{
					bb = bb.addCoord(entry.facing.getFrontOffsetX() * add,
								entry.facing.getFrontOffsetY() * add,
								entry.facing.getFrontOffsetZ() * add);
				}
			}
		}
		
		return bb;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos,
			AxisAlignedBB mask, List<AxisAlignedBB> list, Entity entity)
	{
		state = getActualState(state, world, pos);
		
		if (state.getValue(LEAVES))
		{
			addCollisionBoxToList(pos, mask, list, FULL_BLOCK_AABB);
		}
		else
		{
			addCollisionBoxToList(pos, mask, list,
					new AxisAlignedBB(0.5 - RADIUS, 0.5 - RADIUS, 0.5 - RADIUS, 0.5 + RADIUS, 0.5 + RADIUS, 0.5 + RADIUS));
			
			AxisAlignedBB sideBase = new AxisAlignedBB(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);
			
			for (FacingProperties.Entry<Boolean> entry : CONNECTIONS)
			{
				if (state.getValue(entry.property))
				{
					AxisAlignedBB sideBB = AABBUtils.offset(sideBase, entry.facing, RADIUS);
					sideBB = AABBUtils.extend(sideBB, entry.facing, 0.5 - RADIUS);
					sideBB = AABBUtils.expandSides(sideBB, entry.facing, RADIUS);
					addCollisionBoxToList(pos, mask, list, sideBB);
				}
			}
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, variantProp);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, variantProp);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isVisuallyOpaque()
	{
		return false;
	}
}
