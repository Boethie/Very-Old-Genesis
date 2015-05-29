package genesis.block;

import genesis.client.GenesisClient;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGenesisLeaves extends BlockLeaves
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{ CHECK_DECAY, DECAYABLE };
	}
	
	public final TreeBlocksAndItems owner;
	public final ObjectType type;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata variantProp;
	
	public BlockGenesisLeaves(List<EnumTree> variants, TreeBlocksAndItems owner, ObjectType type)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata("variant", variants);
		
		blockState = new BlockState(this, variantProp, CHECK_DECAY, DECAYABLE);
		setDefaultState(getBlockState().getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setStepSound(soundTypeGrass);
		
		Blocks.fire.setFireInfo(this, 30, 60);
	}

	@Override
	public BlockGenesisLeaves setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(Constants.PREFIX + name);
		
		return this;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getMetadata(type, (EnumTree) state.getValue(variantProp));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
	{
		return owner.getStack(type, (EnumTree) world.getBlockState(pos).getValue(variantProp));
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList();
		drops.add(owner.getStack(type, (EnumTree) world.getBlockState(pos).getValue(variantProp)));
		return drops;
	}
	
	protected ItemStack getSapling(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return owner.getStack(owner.SAPLING, (EnumTree) state.getValue(variantProp));
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		
		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		
		int chance = this.getSaplingDropChance(state);
		
		if (fortune > 0)
		{
		    chance = Math.max(chance - (2 << fortune), 10);
		}
		
		if (rand.nextInt(chance) == 0)
		{
			ret.add(getSapling(world, pos, state));
		}
		
		return ret;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getDefaultState();
		state = state.withProperty(variantProp, (Comparable) owner.getVariant(this, meta));
		state = state.withProperty(DECAYABLE, false);
		return state;
	}

	@Override
	public EnumType getWoodType(int meta)
	{
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return GenesisClient.fancyGraphicsEnabled() ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube()
	{
		return !GenesisClient.fancyGraphicsEnabled();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return !GenesisClient.fancyGraphicsEnabled() && worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
	}
}
