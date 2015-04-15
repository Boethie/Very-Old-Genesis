package genesis.block;

import java.util.List;

import genesis.client.model.WattleFenceModel;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.EnumTree;
import genesis.metadata.IMetadata;
import genesis.metadata.Properties;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockWattleFence extends BlockFence
{
	@Properties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}

	public final VariantsOfTypesCombo owner;
	
	public final PropertyEnum variantProp;
	public final List<EnumTree> variants;
	
	public BlockWattleFence(List<EnumTree> variants, VariantsOfTypesCombo owner)
	{
		super(Material.wood);
		
		this.owner = owner;
		
		variantProp = PropertyEnum.create("variant", EnumTree.class, variants);
		this.variants = variants;
		
		blockState = new BlockState(this, variantProp, NORTH, EAST, WEST, SOUTH);
		setDefaultState(getBlockState().getBaseState());
		
		WattleFenceModel.register(variants);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
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
	
	public Block setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(Constants.PREFIX + name);
		
		return this;
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		for (EnumTree treeType : variants)
		{
			list.add(new ItemStack(itemIn, 1, variants.indexOf(treeType)));
		}
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		
		if (collidingEntity instanceof EntityFX)
		{
			for (int i = 0; i < list.size(); i++)
			{
				AxisAlignedBB bb = (AxisAlignedBB) list.get(i);
				bb = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY - 0.5, bb.maxZ);
				list.set(i, bb);
			}
		}
	}
}
