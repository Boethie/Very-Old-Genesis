package genesis.block;

import genesis.combo.DungBlocksAndItems;
import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumDung;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.util.BitMask;
import genesis.util.BlockStateToMetadata;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockDung extends BlockGenesisVariants<EnumDung>
{
	public static final int MIN_HEIGHT = 1;
	public static final int MAX_HEIGHT = 8;
	public static final PropertyInteger HEIGHT = PropertyInteger.create("height", MIN_HEIGHT, MAX_HEIGHT);
	public static final BitMask HEIGHT_MASK = BitMask.forValueCount(HEIGHT.getAllowedValues().size()).shiftTo(Short.SIZE - 1);
	
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{ HEIGHT };
	}
	
	private static final AxisAlignedBB[] BBS = new AxisAlignedBB[HEIGHT.getAllowedValues().size()];
	
	static
	{
		for (int i = 0; i < BBS.length; i++)
		{
			BBS[i] = new AxisAlignedBB(0, 0, 0, 1, (i + 1) / (double) MAX_HEIGHT, 1);
		}
	}
	
	private boolean ready = false;
	
	public BlockDung(DungBlocksAndItems owner,
			ObjectType<EnumDung, ? extends BlockGenesisVariants<EnumDung>, ? extends Item> type,
			List<EnumDung> variants, Class<EnumDung> variantClass)
	{
		super(owner, type, variants, variantClass, Material.GROUND, GenesisSoundTypes.DUNG);
		
		blockState = new BlockStateContainer(this, variantProp, HEIGHT);
		setDefaultState(blockState.getBaseState().withProperty(HEIGHT, MAX_HEIGHT));
		
		ready = true;
		
		setHarvestLevel("shovel", 0);
		clearDrops().addDrop((s, r) -> owner.getStack(DungBlocksAndItems.DUNG, s.getValue(variantProp), s.getValue(HEIGHT) + 1));
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (EnumDung variant : variants)
		{
			ItemStack stack = owner.getStack(type, variant);
			stack.setItemDamage(HEIGHT_MASK.encode(stack.getMetadata(), MAX_HEIGHT - MIN_HEIGHT));
			list.add(stack);
			
			stack = stack.copy();
			stack.setItemDamage(HEIGHT_MASK.encode(stack.getMetadata(), 0));
			list.add(stack);
		}
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer)
	{
		IBlockState state = world.getBlockState(pos);
		EnumDung variant = owner.getVariant(this, meta);
		
		int height = 0;
		
		if (owner.isStateOf(state, variant, type))
			height = state.getValue(HEIGHT);
		else
			state = owner.getBlockState(type, variant);
		
		return state.withProperty(HEIGHT,
				Math.min(MAX_HEIGHT, height + MIN_HEIGHT + HEIGHT_MASK.decode(meta)));
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return world.isSideSolid(pos.down(), EnumFacing.UP);
	}

	@Override
	public void onNeighborChange(IBlockAccess blockAccess, BlockPos pos, BlockPos neighbor)
	{
		if (blockAccess instanceof World)
		{
			World world = (World) blockAccess;
			if (!canPlaceBlockAt(world, pos))
				world.destroyBlock(pos, true);
		}
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos)
	{
		return state.getValue(HEIGHT) * 0.1F;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		IBlockState state = world.getBlockState(pos);
		ItemStack held = player.inventory.getCurrentItem();
		
		if (held == null)
			return false;
		
		return held.getItem().getHarvestLevel(held, getHarvestTool(state)) >= getHarvestLevel(state);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BBS[state.getValue(HEIGHT) - MIN_HEIGHT];
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		int index = state.getValue(HEIGHT) - MIN_HEIGHT - 1;
		
		if (index >= 0)
			return BBS[index];
		
		return null;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, HEIGHT, variantProp);
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata, HEIGHT, variantProp);
	}
	
	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(HEIGHT) <= (MIN_HEIGHT + MAX_HEIGHT) / 2;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		if (ready)
			return state.getValue(HEIGHT) >= MAX_HEIGHT;
		
		return false;
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return isFullCube(state);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return isFullCube(state);
	}
}
