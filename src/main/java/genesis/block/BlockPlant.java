package genesis.block;

import genesis.common.*;
import genesis.item.ItemBlockMulti;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.*;

public class BlockPlant<V extends IPlantMetadata<V>> extends BlockBush implements IGrowable, IShearable
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final VariantsOfTypesCombo<V> owner;
	public final ObjectType<? extends BlockPlant<V>, ?> type;
	
	public final List<V> variants;
	public final PropertyIMetadata<V> variantProp;
	
	public final ObjectType<? extends BlockGenesisDoublePlant<V>, ?> doubleType;
	
	public BlockPlant(VariantsOfTypesCombo<V> owner, ObjectType<? extends BlockPlant<V>, ? extends ItemBlockMulti<V>> type, List<V> variants, Class<V> variantClass, ObjectType<? extends BlockGenesisDoublePlant<V>, ? extends ItemBlockMulti<V>> doubleType)
	{
		setStepSound(soundTypeGrass);
		
		final float size = 0.4F;
		setBlockBounds(0.5F - size, 0, 0.5F - size, 0.5F + size, size * 2, 0.5F + size);
		setHardness(0);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		this.owner = owner;
		this.type = type;
		
		variantProp = new PropertyIMetadata<V>("variant", variants, variantClass);
		this.variants = variants;
		
		blockState = new BlockState(this, variantProp);
		setDefaultState(getBlockState().getBaseState());
		
		this.doubleType = doubleType;
	}
	
	@Override
	protected boolean canPlaceBlockOn(Block ground)
	{
		return (ground == GenesisBlocks.moss) || super.canPlaceBlockOn(ground);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
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
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType()
	{
		return Block.EnumOffsetType.XYZ;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 100;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 60;
	}
	
	@Override
	public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() == this)
		{	// TODO: Doesn't work on block breaking particles from packets, because the block is already air.
			return state.getValue(variantProp).getColorMultiplier(world, pos);
		}
		
		return super.colorMultiplier(world, pos, renderPass);
	}
	
	@Override
	public int getRenderColor(IBlockState state)
	{
		return state.getValue(variantProp).getRenderColor();
	}
	
	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return doubleType != null && owner.getValidVariants(doubleType).contains(state.getValue(variantProp));
	}
	
	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}
	
	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		world.setBlockToAir(pos);
		TileEntity te = world.getTileEntity(pos);
		V variant = state.getValue(variantProp);
		
		BlockGenesisDoublePlant<V> doublePlant = owner.getBlock(doubleType, variant);
		
		if (!doublePlant.placeAt(world, pos, variant, 3))
		{
			world.setBlockState(pos, state);
			world.setTileEntity(pos, te);
		}
	}
	
	public boolean placeAt(World world, BlockPos bottom, V variant, int flags)
	{
		if (world.isAirBlock(bottom))
		{
			world.setBlockState(bottom, owner.getBlockState(type, variant), flags);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(variantProp).isShearable(item, world, pos);
	}
	
	protected ItemStack getDrop(IBlockState state)
	{
		return owner.getStack(type, state.getValue(variantProp));
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		IBlockState state = world.getBlockState(pos);
		return state.getValue(variantProp).onSheared(item, world, pos, Collections.singletonList(getDrop(state)));
	}
	
	@Override
	public boolean isReplaceable(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(variantProp).isReplaceable(world, pos);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return state.getValue(variantProp).getDrops(world, pos, state, WorldUtils.getWorldRandom(world, RANDOM), Collections.singletonList(getDrop(state)));
	}
}
