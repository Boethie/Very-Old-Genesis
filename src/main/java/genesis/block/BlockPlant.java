package genesis.block;

import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.IPlantMetadata;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.*;
import genesis.item.ItemBlockMulti;
import genesis.util.*;

import java.util.*;

import com.google.common.collect.Lists;

import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.*;

public class BlockPlant<V extends IPlantMetadata<V>> extends BlockBush implements IGrowable, IShearable
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final VariantsOfTypesCombo<V> owner;
	public final ObjectType<? extends BlockPlant<V>, ?> type;
	
	public final List<V> variants;
	public final PropertyIMetadata<V> variantProp;
	
	public final ObjectType<? extends BlockGenesisDoublePlant<V>, ?> doubleType;
	
	protected static final float BB_INSET = 0.0625F * 2;
	protected static final AxisAlignedBB BB =
			new AxisAlignedBB(BB_INSET, 0, BB_INSET, 1 - BB_INSET, 1, 1 - BB_INSET);
	
	public BlockPlant(VariantsOfTypesCombo<V> owner, ObjectType<? extends BlockPlant<V>, ? extends ItemBlockMulti<V>> type, List<V> variants, Class<V> variantClass, ObjectType<? extends BlockGenesisDoublePlant<V>, ? extends ItemBlockMulti<V>> doubleType)
	{
		setSoundType(SoundType.PLANT);
		
		setHardness(0);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		this.owner = owner;
		this.type = type;
		
		variantProp = new PropertyIMetadata<V>("variant", variants, variantClass);
		this.variants = variants;
		
		blockState = new BlockStateContainer(this, variantProp);
		setDefaultState(getBlockState().getBaseState());
		
		this.doubleType = doubleType;
	}
	
	@Override
	protected boolean func_185514_i(IBlockState ground)
	{
		return (ground == GenesisBlocks.moss) || super.func_185514_i(ground);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
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
	
	/*@Override
	public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() == this)
		{	// TODO: Doesn't work on block breaking particles from packets, because the block is already air.
			return state.getValue(variantProp).getColorMultiplier(world, pos);
		}
		
		return super.colorMultiplier(world, pos, renderPass);
	}
	
	@Override
	public int getRenderColor(IBlockState state)
	{
		return state.getValue(variantProp).getRenderColor();
	}*/
	
	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return doubleType != null && owner.getValidVariants(doubleType).contains(state.getValue(variantProp));
	}
	
	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}
	
	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		world.setBlockToAir(pos);
		TileEntity te = world.getTileEntity(pos);
		V variant = state.getValue(variantProp);
		
		BlockGenesisDoublePlant<V> doublePlant = owner.getBlock(doubleType, variant);
		
		if (!doublePlant.placeAt(world, pos, variant, 3))
		{
			world.setBlockState(pos, state);
			world.setTileEntity(pos, te);
		}
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return owner.getBlockState(type, owner.getVariant(this, meta));
	}
	
	public boolean placeAt(World world, BlockPos bottom, V variant, int flags)
	{
		if (world.isAirBlock(bottom))
		{
			world.setBlockState(bottom, owner.getBlockState(type, variant), flags);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		
		V variant = state.getValue(variantProp);
		state = world.getBlockState(pos);
		
		if (state.getBlock() == this && state.getValue(variantProp) == variant)
		{
			if (variant == EnumPlant.LEPACYCLOTES && world.rand.nextInt(20) == 0)
			{
				int plants = 6;
				
				for (BlockPos checkPos : BlockPos.getAllInBoxMutable(pos.add(-3, -1, -3), pos.add(3, 1, 3)))
					if (world.getBlockState(checkPos) == state && --plants <= 0)
						break;
				
				if (plants > 0)
				{
					int tries = 4;
					ArrayList<BlockPos> toList = Lists.newArrayList(WorldUtils.getArea(pos.add(-1, 0, -1), pos.add(1, 1, 1)));
					
					do
					{
						BlockPos to = toList.remove(rand.nextInt(toList.size()));
						
						if (world.isAirBlock(to) && canBlockStay(world, to, state))
						{
							world.setBlockState(to, state);
							break;
						}
					}
					while (--tries > 0 && !toList.isEmpty());
				}
			}
		}
	}
	
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(variantProp).isShearable(item, world, pos);
	}
	
	protected ItemStack getDrop(IBlockState state)
	{
		return owner.getStack(type, state.getValue(variantProp));
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		IBlockState state = world.getBlockState(pos);
		return state.getValue(variantProp).onSheared(item, world, pos, Collections.singletonList(getDrop(state)));
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(variantProp).isReplaceable(world, pos);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, state.getValue(variantProp));
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return state.getValue(variantProp).getDrops(world, pos, state, WorldUtils.getWorldRandom(world, RANDOM), Collections.singletonList(getDrop(state)));
	}
	
	@Override
	public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack)
	{
		V variant = owner.getVariant(stack);
		return WorldUtils.canSoilSustainTypes(world, pos, variant.getSoilTypes())
				&& waterInRange(world, pos, variant.getWaterDistance());
	}
	
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		V variant = state.getValue(variantProp);
		return WorldUtils.canSoilSustainTypes(world, pos, variant.getSoilTypes())
				&& waterInRange(world, pos, variant.getWaterDistance());
	}
	
	protected boolean waterInRange(World world, BlockPos pos, int waterDistance)
	{
		return waterDistance < 0 || WorldUtils.waterInRange(world, pos.down(), waterDistance, 1);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BB;
	}
}
