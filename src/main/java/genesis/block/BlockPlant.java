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
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.*;

public class BlockPlant extends BlockBush implements IGrowable, IShearable
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final VariantsOfTypesCombo<IPlantMetadata> owner;
	public final ObjectType<? extends BlockPlant, ?> type;
	
	public final List<IPlantMetadata> variants;
	public final PropertyIMetadata<IPlantMetadata> variantProp;
	
	public BlockPlant(List<IPlantMetadata> variants, VariantsOfTypesCombo<IPlantMetadata> owner, ObjectType<? extends BlockPlant, ? extends ItemBlockMulti<IPlantMetadata>> type)
	{
		setStepSound(soundTypeGrass);
		
		final float size = 0.4F;
		setBlockBounds(0.5F - size, 0, 0.5F - size, 0.5F + size, size * 2, 0.5F + size);
		setHardness(0);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		this.owner = owner;
		this.type = type;
		
		variantProp = new PropertyIMetadata<IPlantMetadata>("variant", variants);
		this.variants = variants;
		
		blockState = new BlockState(this, variantProp);
		setDefaultState(getBlockState().getBaseState());
	}
	
	@Override
	protected boolean canPlaceBlockOn(Block ground)
	{
		return (ground == GenesisBlocks.moss) || super.canPlaceBlockOn(ground);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
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
		{
			return ((IPlantMetadata) state.getValue(variantProp)).getColorMultiplier(world, pos);
		}
		
		return super.colorMultiplier(world, pos, renderPass);
	}
	
	@Override
	public int getRenderColor(IBlockState state)
	{
		return ((IPlantMetadata) state.getValue(variantProp)).getRenderColor();
	}
	
	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return type != PlantBlocks.DOUBLE_PLANT && EnumPlant.DOUBLES.contains(state.getValue(variantProp));
	}
	
	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}
	
	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		IPlantMetadata variant = (IPlantMetadata) state.getValue(variantProp);
		
		if (EnumPlant.DOUBLES.contains(variant))
		{
			world.setBlockToAir(pos);
			BlockGenesisDoublePlant doublePlant = owner.getBlock(PlantBlocks.DOUBLE_PLANT, variant);
			
			if (!doublePlant.placeAt(world, pos, variant, 3))
			{
				world.setBlockState(pos, state);
			}
		}
	}
	
	public boolean placeAt(World world, BlockPos bottom, IPlantMetadata variant, int flags)
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
		return ((IPlantMetadata) world.getBlockState(pos).getValue(variantProp)).isShearable(item, world, pos);
	}
	
	protected ItemStack getDrop(IBlockState state)
	{
		return owner.getStack(type, (IPlantMetadata) state.getValue(variantProp));
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		IBlockState state = world.getBlockState(pos);
		return ((IPlantMetadata) state.getValue(variantProp)).onSheared(item, world, pos, Collections.singletonList(getDrop(state)));
	}
	
	@Override
	public boolean isReplaceable(World world, BlockPos pos)
	{
		return ((IPlantMetadata) world.getBlockState(pos).getValue(variantProp)).isReplaceable(world, pos);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return ((IPlantMetadata) state.getValue(variantProp)).getDrops(world, pos, state, WorldUtils.getWorldRandom(world, RANDOM), Collections.singletonList(getDrop(state)));
	}
}
