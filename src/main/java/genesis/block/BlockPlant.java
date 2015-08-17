package genesis.block;

import genesis.common.*;
import genesis.item.ItemBlockMulti;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

@SuppressWarnings("rawtypes")
public class BlockPlant extends BlockBush implements IGrowable
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final VariantsOfTypesCombo<ObjectType, IMetadata> owner;
	public final ObjectType type;
	
	public final List<IMetadata> variants;
	public final PropertyIMetadata<IMetadata> variantProp;
	
	public BlockPlant(List<IMetadata> variants, VariantsOfTypesCombo<ObjectType, IMetadata> owner, ObjectType type)
	{
		setStepSound(soundTypeGrass);
		
		final float size = 0.4F;
		setBlockBounds(0.5F - size, 0, 0.5F - size, 0.5F + size, size * 2, 0.5F + size);
		setHardness(0);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		this.owner = owner;
		this.type = type;
		
		variantProp = new PropertyIMetadata<IMetadata>("variant", variants);
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
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, (IMetadata) state.getValue(variantProp));
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
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		return useBiomeColor(state) ? ColorizerGrass.getGrassColor(0.5D, 1.0D) : super.getRenderColor(state);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass)
	{
		return useBiomeColor(world.getBlockState(pos)) ? world.getBiomeGenForCoords(pos).getGrassColorAtPos(pos) : super.colorMultiplier(world, pos, renderPass);
	}
	
	@SideOnly(Side.CLIENT)
	protected boolean useBiomeColor(IBlockState state)
	{
		return owner.getVariant(state) == EnumPlant.ASTEROXYLON;
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
		IMetadata variant = (IMetadata) state.getValue(variantProp);
		
		if (EnumPlant.DOUBLES.contains(variant))
		{
			BlockGenesisDoublePlant doublePlant = owner.getBlock(PlantBlocks.DOUBLE_PLANT, variant);
			doublePlant.placeAt(world, pos, variant, 3);
		}
	}
	
	public void placeAt(World world, BlockPos bottom, IMetadata variant, int flags)
	{
		if (world.isAirBlock(bottom))
		{
			world.setBlockState(bottom, owner.getBlockState(type, variant), flags);
		}
	}
}
