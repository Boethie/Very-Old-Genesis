package genesis.block;

import java.util.List;

import genesis.client.*;
import genesis.client.model.*;
import genesis.common.*;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWattleFence extends BlockFence
{
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}

	public final VariantsOfTypesCombo owner;
	public final ObjectType type;
	
	public final PropertyIMetadata variantProp;
	public final List<EnumTree> variants;
	
	public BlockWattleFence(final List<EnumTree> variants, VariantsOfTypesCombo owner, ObjectType type)
	{
		super(Material.wood);
		
		this.owner = owner;
		this.type = type;
		
		variantProp = new PropertyIMetadata("variant", variants);
		this.variants = variants;
		
		blockState = new BlockState(this, variantProp, NORTH, EAST, WEST, SOUTH);
		setDefaultState(getBlockState().getBaseState());
		
		Genesis.proxy.callSided(new SidedFunction()
		{
			@SideOnly(Side.CLIENT)
			@Override
			public void client(GenesisClient client)
			{
				WattleFenceModel.register(variants);
			}
		});
		
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
	
	@Override
	public BlockWattleFence setUnlocalizedName(String name)
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
	@SideOnly(Side.CLIENT)
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
