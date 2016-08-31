package genesis.block;

import genesis.combo.DebrisBlocks;
import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumDebrisOther;
import genesis.combo.variant.IMetadata;
import genesis.combo.variant.MultiMetadataList.MultiMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.item.ItemBlockMulti;
import genesis.util.BlockStateToMetadata;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockGenesisDebris extends BlockGenesisVariants<MultiMetadata>
{
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{};
	}
	
	private static final AxisAlignedBB BB = new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1);
	
	protected final DebrisBlocks debrisOwner;
	
	public BlockGenesisDebris(DebrisBlocks owner,
			ObjectType<MultiMetadata, BlockGenesisDebris, ItemBlockMulti<MultiMetadata>> type,
			List<MultiMetadata> variants, Class<MultiMetadata> variantClass)
	{
		super(owner, type, variants, variantClass, Material.VINE, GenesisSoundTypes.DEBRIS);
		
		debrisOwner = owner;
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		Blocks.FIRE.setFireInfo(this, 60, 100);
		setTickRandomly(true);
		
		clearDrops();
		addDrop((state, rand) -> {
			MultiMetadata variant = debrisOwner.getVariant(state);
			ItemStack original = debrisOwner.getStack(variant);
			
			if (variant.getOriginal() instanceof EnumDebrisOther)
				return (((EnumDebrisOther) variant.getOriginal()).getDrop(original));
			
			return original;
		});
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
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess world, BlockPos pos)
	{
		//return !debrisOwner.isStateOf(world.getBlockState(pos), debrisOwner.getVariant(EnumDebrisOther.EPIDEXIPTERYX_FEATHER));
		IMetadata<?> variant = world.getBlockState(pos).getValue(variantProp).getOriginal();
		
		if (variant instanceof EnumDebrisOther)
			return ((EnumDebrisOther) variant).getDrop(null) == null;
		
		return true;
	}
	
	public boolean canBlockStay(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return world.isSideSolid(pos.down(), EnumFacing.UP, false);
	}
	
	public boolean checkAndDropBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(world, pos, state))
			return world.destroyBlock(pos, true);
		return false;
	}

	@Override
	public void onNeighborChange(IBlockAccess blockAccess, BlockPos pos, BlockPos neighbor)
	{
		if (blockAccess instanceof World)
		{
			World world = (World) blockAccess;
			checkAndDropBlock(world, pos, world.getBlockState(pos));
		}
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		checkAndDropBlock(world, pos, state);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return super.canPlaceBlockAt(world, pos) && world.isSideSolid(pos.down(), EnumFacing.UP);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BB;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		return NULL_AABB;
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
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
}
