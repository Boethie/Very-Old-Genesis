package genesis.block;

import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumSilt;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.util.BlockStateToMetadata;
import genesis.util.WorldUtils;

import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockSilt extends BlockFalling
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final SiltBlocks owner;
	public final ObjectType<BlockSilt, ItemBlockMulti<EnumSilt>> type;
	
	public final PropertyIMetadata<EnumSilt> variantProp;
	public final List<EnumSilt> variants;
	
	public BlockSilt(SiltBlocks owner, ObjectType<BlockSilt, ItemBlockMulti<EnumSilt>> type, List<EnumSilt> variants, Class<EnumSilt> variantClass)
	{
		super(Material.sand);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		this.variantProp = new PropertyIMetadata<EnumSilt>("variant", variants, variantClass);
		
		blockState = new BlockStateContainer(this, variantProp);
		setDefaultState(blockState.getBaseState());
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setSoundType(SoundType.SAND);
		setHardness(0.5F);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, state.getValue(variantProp));
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plant)
	{
		switch (plant.getPlantType(world, pos))
		{
		case Beach:
			for (EnumFacing side : EnumFacing.HORIZONTALS)
				if (WorldUtils.isWater(world, pos.offset(side)))
					return true;
			
			return false;
		case Desert:
			return true;
		default:
			return super.canSustainPlant(state, world, pos, direction, plant);
		}
	}
	
	protected boolean canSiltFallInto(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock().isAir(state, world, pos))
			return true;
		
		if (state.getBlock() == Blocks.fire)
			return true;
		
		if (state.getMaterial().isLiquid())
			return true;
		
		return false;
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote && canSiltFallInto(world, pos.down()) && pos.getY() >= 0)
		{
			int area = 32;
			
			if (!fallInstantly && world.isAreaLoaded(pos.add(-area, -area, -area), pos.add(area, area, area)))
			{
				EntityFallingBlock falling = new EntityFallingBlock(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
				onStartFalling(falling);
				world.spawnEntityInWorld(falling);
			}
			else
			{
				BlockPos landPos = pos;
				
				do
				{
					landPos = landPos.down();
				} while (canSiltFallInto(world, landPos) && landPos.getY() >= 0);
				
				world.setBlockToAir(pos);
				
				if (landPos.getY() >= 0)
					world.setBlockState(landPos.up(), state);
			}
		}
	}
}
