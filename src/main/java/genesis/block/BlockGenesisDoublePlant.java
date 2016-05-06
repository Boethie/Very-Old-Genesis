package genesis.block;

import java.util.Collections;
import java.util.List;

import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.IPlantMetadata;
import genesis.item.ItemBlockMulti;
import genesis.util.BlockStateToMetadata;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGenesisDoublePlant<V extends IPlantMetadata<V>> extends BlockPlant<V>
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{TOP};
	}
	
	public static final PropertyBool TOP = PropertyBool.create("top");
	
	private static final AxisAlignedBB BB_TOP =
			BB.setMaxY(0.75);
	
	public BlockGenesisDoublePlant(VariantsOfTypesCombo<V> owner,
			ObjectType<V, ? extends BlockGenesisDoublePlant<V>, ? extends ItemBlockMulti<V>> type,
			List<V> variants, Class<V> variantClass,
			SoundType sound)
	{
		super(owner, type, variants, variantClass, null, sound);
		
		blockState = new BlockStateContainer(this, variantProp, TOP);
		setDefaultState(getBlockState().getBaseState().withProperty(TOP, false));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(TOP) ? BB_TOP : BB;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return super.canPlaceBlockAt(world, pos) && world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos);
	}
	
	@Override
	public boolean canBlockStay(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		boolean top = state.getValue(TOP);
		IBlockState checkState = top ?
				world.getBlockState(pos.down())
				: world.getBlockState(pos.up());
		
		if (!owner.isStateOf(checkState, state.getValue(variantProp), type)
				|| checkState.getValue(TOP) == top)
			return false;
		
		if (!top)
			return super.canBlockStay(world, pos, state);
		
		return true;
	}
	
	@Override
	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(world, pos, state))
		{
			boolean isTop = state.getValue(TOP);
			BlockPos top = isTop ? pos : pos.up();
			BlockPos bottom = isTop ? pos.down() : pos;
			Block topBlock = isTop ? this : world.getBlockState(top).getBlock();
			Block bottomBlock = isTop ? world.getBlockState(bottom).getBlock() : this;
			
			if (!isTop)
			{
				dropBlockAsItem(world, pos, state, 0); //Forge move above the setting to air.
			}
			
			if (topBlock == this)
			{
				world.setBlockState(top, Blocks.air.getDefaultState(), 3);
			}
			
			if (bottomBlock == this)
			{
				world.setBlockState(bottom, Blocks.air.getDefaultState(), 3);
			}
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		if (state.getValue(TOP))
		{
			return Collections.emptyList();
		}
		
		return super.getDrops(world, pos, state, fortune);
	}
	
	@Override
	public boolean placeAt(World world, BlockPos bottom, V variant, int flags)
	{
		if (!canReplace(world, bottom, EnumFacing.UP, owner.getStack(type, variant)))
			return false;
		
		IBlockState state = owner.getBlockState(type, variant);
		
		if (world.isAirBlock(bottom) && world.isAirBlock(bottom.up()))
		{
			world.setBlockState(bottom, state.withProperty(TOP, false), flags);
			world.setBlockState(bottom.up(), state.withProperty(TOP, true), flags);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		world.setBlockState(pos.up(), state.withProperty(TOP, true), 2);
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		boolean top = world.getBlockState(pos).getValue(TOP);
		BlockPos other = top ? pos.down() : pos.up();

		IBlockState otherState = world.getBlockState(other);
		
		if (otherState.getBlock() == this)
		{
			world.playAuxSFXAtEntity(player, 2001, other, Block.getStateId(otherState));
			
			if (!player.capabilities.isCreativeMode)	// Drop the bottom of the plant as an item.
			{
				dropBlockAsItem(world, other, otherState, 0);
			}
			
			world.setBlockToAir(other);
		}
		
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, TOP, variantProp);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, TOP, variantProp);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType()
	{
		return Block.EnumOffsetType.XZ;
	}
}
