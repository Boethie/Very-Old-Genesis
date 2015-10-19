package genesis.block;

import genesis.client.GenesisClient;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;

import java.util.*;

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
	public final ObjectType<BlockGenesisLeaves, ItemBlockMulti> type;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata<EnumTree> variantProp;
	
	public BlockGenesisLeaves(List<EnumTree> variants, TreeBlocksAndItems owner, ObjectType<BlockGenesisLeaves, ItemBlockMulti> type)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<EnumTree>("variant", variants);
		
		blockState = new BlockState(this, variantProp, CHECK_DECAY, DECAYABLE);
		setDefaultState(getBlockState().getBaseState().withProperty(DECAYABLE, true).withProperty(CHECK_DECAY, false));
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setStepSound(soundTypeGrass);
		
		Blocks.fire.setFireInfo(this, 30, 60);
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
		return owner.getItemMetadata(type, (EnumTree) state.getValue(variantProp));
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
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
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
		state = state.withProperty(variantProp, (Comparable<?>) owner.getVariant(this, meta));
		state = state.withProperty(DECAYABLE, false).withProperty(CHECK_DECAY, false);
		return state;
	}
	
	protected void breakAndDrop(World world, BlockPos pos)
	{
		dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
		world.setBlockToAir(pos);
	}
	
	protected int leafDistance = 5;
	protected float[] distanceCosts = {leafDistance, 1, 1.4142F, 1.7321F};	// 0 uses leafDistance to make sure there are no stack overflows.
	
	public boolean isConnectedToLog(EnumTree treeType, World world, BlockPos curPos, float curCost)
	{
		if (curCost < leafDistance)
		{
			IBlockState state = world.getBlockState(curPos);
			TreeBlocksAndItems.VariantData data = owner.getVariantData(state);
			
			if (data != null && data.variant == treeType)
			{
				if (data.type == owner.LOG)
				{
					return true;
				}
				else if (data.type == owner.LEAVES)
				{
					Iterable<BlockPos> blocksAround = (Iterable<BlockPos>) BlockPos.getAllInBox(curPos.add(-1, -1, -1), curPos.add(1, 1, 1));
					
					for (BlockPos nextPos : blocksAround)
					{
						if (!nextPos.equals(curPos))
						{
							BlockPos diff = curPos.add(nextPos.multiply(-1));
							int blockDist = Math.abs(diff.getX()) + Math.abs(diff.getY()) + Math.abs(diff.getZ());
							float addCost = distanceCosts[blockDist];
							
							if (isConnectedToLog(treeType, world, nextPos, curCost + addCost))
							{
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isConnectedToLog(World world, BlockPos pos)
	{
		return isConnectedToLog((EnumTree) world.getBlockState(pos).getValue(variantProp), world, pos, 0);
	}
	
	protected void checkAndDoDecay(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		
		if ((Boolean) state.getValue(BlockLeaves.CHECK_DECAY) && world.isAreaLoaded(pos.add(-leafDistance, -leafDistance, -leafDistance), pos.add(leafDistance, leafDistance, leafDistance)))
		{
			if (isConnectedToLog(world, pos))
			{
				world.setBlockState(pos, state.withProperty(BlockLeaves.CHECK_DECAY, false), 4);
			}
			else
			{
				breakAndDrop(world, pos);
			}
		}
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		checkAndDoDecay(world, pos);
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
