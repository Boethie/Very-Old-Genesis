package genesis.block;

import java.util.Collections;
import java.util.List;

import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.BlockStateToMetadata;
import genesis.util.WorldUtils;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BlockMultiOre<T extends IOreVariant> extends BlockOre
{
	@BlockProperties
	public static IProperty[] properties = {};
	
	public VariantsOfTypesCombo<ObjectType, T> owner;
	public ObjectType type;
	public List<T> variants;
	
	public PropertyIMetadata<T> variantProp;
	
	public BlockMultiOre(List<T> variants, VariantsOfTypesCombo<ObjectType, T> owner, ObjectType type)
	{
		this.variants = variants;
		this.owner = owner;
		this.type = type;
		
		variantProp = new PropertyIMetadata<T>("variant", variants);
		blockState = new BlockState(this, variantProp);
		setDefaultState(blockState.getBaseState());
		
		// Set defaults for when a function doesn't get the variant from which to get the value to return.
		setHardness(4.2F);
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
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	protected IOreVariant getVariant(IBlockState state)
	{
		if (state.getBlock() == this)
		{
			return (IOreVariant) state.getValue(variantProp);
		}
		
		return null;
	}
	
	protected IOreVariant getVariant(IBlockAccess world, BlockPos pos)
	{
		return getVariant(world.getBlockState(pos));
	}
	
	@Override
	public float getBlockHardness(World world, BlockPos pos)
	{
		IOreVariant variant = getVariant(world, pos);
		return variant != null ? variant.getHardness() : super.getBlockHardness(world, pos);
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		IOreVariant variant = getVariant(world, pos);
		
		if (variant != null)
		{
			return getVariant(world, pos).getExplosionResistance() / 5;	// Divide by 5 because that's what vanilla does.
		}
		
		return super.getExplosionResistance(world, pos, exploder, explosion);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return getVariant(state).getDrops(state, WorldUtils.getWorldRandom(world, RANDOM));
	}
	
	@Override
	public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune)
	{
		return getVariant(world, pos).getDropExperience().get(WorldUtils.getWorldRandom(world, RANDOM));
	}
}
