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
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.*;

public class BlockPlant extends BlockBush
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final VariantsOfTypesCombo owner;
	public final ObjectType<BlockPlant, ItemBlockMulti> type;

	public final List<IMetadata> variants;
	public final PropertyIMetadata<IMetadata> variantProp;
	
	public BlockPlant(List<IMetadata> variants, VariantsOfTypesCombo owner, ObjectType<BlockPlant, ItemBlockMulti> type)
	{
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setBlockBounds(0.5F - 0.4F, 0.0F, 0.5F - 0.4F, 0.5F + 0.4F, 0.4F * 2, 0.5F + 0.4F);

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
        return useBiomeColor(world.getBlockState(pos)) ? BiomeColorHelper.getGrassColorAtPos(world, pos) : super.colorMultiplier(world, pos, renderPass);
    }

	@SideOnly(Side.CLIENT)
	protected boolean useBiomeColor(IBlockState state)
	{
		return GenesisBlocks.plants.getVariant(state) == EnumPlant.ASTEROXLYON;
	}
}
