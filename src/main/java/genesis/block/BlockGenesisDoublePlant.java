package genesis.block;

import java.util.List;
import java.util.Random;

import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.BlockStateToMetadata;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.*;

@SuppressWarnings("rawtypes")
public class BlockGenesisDoublePlant extends BlockPlant
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{TOP};
	}
	
	public static final PropertyBool TOP = PropertyBool.create("top");
	
	public BlockGenesisDoublePlant(List<IMetadata> variants, VariantsOfTypesCombo<ObjectType, IMetadata> owner, ObjectType type)
	{
		super(variants, owner, type);

		blockState = new BlockState(this, variantProp, TOP);
		setDefaultState(getBlockState().getBaseState().withProperty(TOP, false));
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		boolean top = (Boolean) world.getBlockState(pos).getValue(TOP);
		float inset = 0.0625F * 2;
		setBlockBounds(inset, 0, inset, 1 - inset, 1, 1 - inset);
		
		if (top)
		{
			maxY = 0.75;
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return super.canPlaceBlockAt(world, pos) && world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos);
	}
	
	@Override
	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(world, pos, state))
		{
			boolean isTop = (Boolean) state.getValue(TOP);
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
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() != this)
		{	// TODO: Make sure Paul uses canPlaceBlockAt instead, so we can remove this.
			return super.canBlockStay(world, pos, state); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
		}
		else if ((Boolean) state.getValue(TOP))
		{
			return world.getBlockState(pos.down()).getBlock() == this;
		}
		else
		{
			return super.canBlockStay(world, pos, state);
		}
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		if ((Boolean) state.getValue(TOP))
		{
			return null;
		}
		else
		{
			return super.getItemDropped(state, rand, fortune);
		}
	}
	
	@Override
	public void placeAt(World world, BlockPos bottom, IMetadata variant, int flags)
	{
		IBlockState state = owner.getBlockState(type, variant);
		
		if (world.isAirBlock(bottom) && world.isAirBlock(bottom.up()))
		{
			world.setBlockState(bottom, state.withProperty(TOP, false), flags);
			world.setBlockState(bottom.up(), state.withProperty(TOP, true), flags);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		world.setBlockState(pos.up(), getDefaultState().withProperty(TOP, true), 2);
	}
	
	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		boolean top = (Boolean) world.getBlockState(pos).getValue(TOP);
		BlockPos other = top ? pos.down() : pos.up();
		
		IBlockState otherState = world.getBlockState(other);
		
		if (otherState.getBlock() == this)
		{
			world.playAuxSFXAtEntity(player, 2001, other, Block.getStateId(otherState));
			
			if (!player.capabilities.isCreativeMode && top)	// Drop the bottom of the plant as an item.
				dropBlockAsItem(world, other, otherState, 0);
			
			world.setBlockToAir(other);
		}
		
		return super.removedByPlayer(world, pos, player, willHarvest);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		/*if ((Boolean) state.getValue(TOP))
		{
			IBlockState bottomState = world.getBlockState(pos.down());

			if (bottomState.getBlock() == this)
			{
				state = bottomState;
			}
		}*/
		
		return state;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, TOP, variantProp);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		if ((Boolean) state.getValue(TOP))
		{
			return BlockStateToMetadata.getMetaForBlockState(state, TOP);
		}
		
		return BlockStateToMetadata.getMetaForBlockState(state, TOP, variantProp);
	}
}
