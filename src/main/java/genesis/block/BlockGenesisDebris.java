package genesis.block;

import genesis.combo.DebrisBlocks;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.VariantsOfTypesCombo.ObjectType;
import genesis.combo.variant.EnumDebrisOther;
import genesis.combo.variant.MultiMetadataList.MultiMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisSounds;
import genesis.item.ItemBlockMulti;
import genesis.util.BlockStateToMetadata;
import genesis.util.random.drops.blocks.BlockDrop;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
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
	
	protected final DebrisBlocks debrisOwner;
	
	public BlockGenesisDebris(DebrisBlocks owner, ObjectType<BlockGenesisDebris, ItemBlockMulti<MultiMetadata>> type, List<MultiMetadata> variants, Class<MultiMetadata> variantClass)
	{
		super(owner, type, variants, variantClass, Material.vine);
		
		debrisOwner = owner;
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setStepSound(GenesisSounds.DEBRIS);
		
		Blocks.fire.setFireInfo(this, 60, 100);
		setTickRandomly(true);
		
		setBlockBounds(0, 0, 0, 1, 1/16F, 1);
		
		clearDrops();
		addDrop(new BlockDrop(1)
		{
			@Override
			public ItemStack getStack(IBlockState state, int size)
			{
				MultiMetadata variant = debrisOwner.getVariant(state);
				ItemStack original = debrisOwner.getStack(variant);
				
				if (variant.getOriginal() instanceof EnumDebrisOther)
					return (((EnumDebrisOther) variant.getOriginal()).getDrop(original));
				
				return original;
			}
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
	public boolean isReplaceable(World world, BlockPos pos)
	{
		return !debrisOwner.isStateOf(world.getBlockState(pos), debrisOwner.getVariant(EnumDebrisOther.EPIDEXIPTERYX_FEATHER));
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
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighbor)
	{
		checkAndDropBlock(world, pos, state);
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
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}
}
