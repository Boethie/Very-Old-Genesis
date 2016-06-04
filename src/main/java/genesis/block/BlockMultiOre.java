package genesis.block;

import java.util.List;

import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.IOreVariant;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.util.BlockStateToMetadata;
import genesis.util.WorldUtils;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMultiOre<V extends IOreVariant<V>> extends BlockOre
{
	@BlockProperties
	public static IProperty<?>[] properties = {};
	
	public VariantsOfTypesCombo<V> owner;
	public ObjectType<V, ? extends BlockMultiOre<V>, ?> type;
	public List<V> variants;
	
	public PropertyIMetadata<V> variantProp;
	
	public BlockMultiOre(VariantsOfTypesCombo<V> owner, ObjectType<V, ? extends BlockMultiOre<V>, ?> type, List<V> variants, Class<V> variantClass)
	{
		this.variants = variants;
		this.owner = owner;
		this.type = type;
		
		variantProp = new PropertyIMetadata<>("variant", variants, variantClass);
		blockState = new BlockStateContainer(this, variantProp);
		setDefaultState(blockState.getBaseState());
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		
		// Set defaults for when a function doesn't get the variant from which to get the value to return.
		setHarvestLevel("pickaxe", 1);
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
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	protected IOreVariant<V> getVariant(IBlockState state)
	{
		if (state.getBlock() == this)
		{
			return state.getValue(variantProp);
		}
		
		return null;
	}
	
	protected IOreVariant<V> getVariant(IBlockAccess world, BlockPos pos)
	{
		return getVariant(world.getBlockState(pos));
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return state.getValue(variantProp).getHarvestLevel();
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos)
	{
		IOreVariant<V> variant = getVariant(world, pos);
		return variant != null ? variant.getHardness() : super.getBlockHardness(state, world, pos);
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		IOreVariant<V> variant = getVariant(world, pos);
		
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
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		return getVariant(world, pos).getDropExperience().get(WorldUtils.getWorldRandom(world, RANDOM));
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, state.getValue(variantProp));
	}
}
