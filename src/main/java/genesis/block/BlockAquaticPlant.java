package genesis.block;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import genesis.client.GenesisSounds;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.EnumAquaticPlant;
import genesis.util.Constants;
import genesis.util.Metadata;
import genesis.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAquaticPlant extends BlockMetadata
{

	private Set<Block> bases;

	public BlockAquaticPlant()
	{
		super(Material.water);
		this.setItemDropped(Item.getItemFromBlock(this)).setQuantityDropped(1).setStepSound(GenesisSounds.AQUATICPLANT).setHardness(0.0F).setTickRandomly(true).setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}

	@Override
	public BlockAquaticPlant setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(name);

		return this;
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		super.getSubBlocks(itemIn, tab, list);
		list.remove(list.size() - 2);
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
	public boolean isReplaceable(World worldIn, BlockPos pos)
	{
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (((EnumAquaticPlant) state.getValue(Constants.AQUATIC_PLANT_VARIANT)) == EnumAquaticPlant.CHANCELLORIA)
		{
			entityIn.attackEntityFrom(Constants.CHANCELLORIA_DMG, 0.5F);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.canBlockStay(worldIn, pos, state))
		{
			this.breakPlant(worldIn, pos, state);
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		this.breakPlant(worldIn, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (((EnumAquaticPlant) state.getValue(Constants.AQUATIC_PLANT_VARIANT)) == EnumAquaticPlant.CHARNIA_TOP)
		{
			worldIn.setBlockState(pos, this.getDefaultState().withProperty(Constants.AQUATIC_PLANT_VARIANT, EnumAquaticPlant.CHARNIA), 3);
			worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(Constants.AQUATIC_PLANT_VARIANT, EnumAquaticPlant.CHARNIA_TOP), 3);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return this.canBlockStay(worldIn, pos, this.getDefaultState());
	}

	private void breakPlant(World world, BlockPos pos, IBlockState state)
	{
		world.setBlockState(pos, Blocks.water.getStateFromMeta(0), 3);
		EnumAquaticPlant aqua = (EnumAquaticPlant) state.getValue(Constants.AQUATIC_PLANT_VARIANT);
		if (aqua == EnumAquaticPlant.CHARNIA_TOP)
		{
			IBlockState below = world.getBlockState(pos.down());
			if (below.getBlock() == this && ((EnumAquaticPlant) below.getValue(Constants.AQUATIC_PLANT_VARIANT)) == EnumAquaticPlant.CHARNIA)
			{
				world.setBlockState(pos.down(), Blocks.water.getStateFromMeta(0), 3);
			}
		}
		else if (aqua == EnumAquaticPlant.CHARNIA)
		{
			IBlockState above = world.getBlockState(pos.up());
			if (above.getBlock() == this && ((EnumAquaticPlant) above.getValue(Constants.AQUATIC_PLANT_VARIANT)) == EnumAquaticPlant.CHARNIA_TOP)
			{
				world.setBlockState(pos.up(), Blocks.water.getStateFromMeta(0), 3);
			}
		}
		if (!world.isRemote && aqua != EnumAquaticPlant.CHARNIA)
		{
			ItemStack stack = Metadata.newStack(aqua);
			Entity entity = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			world.spawnEntityInWorld(entity);
		}
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		EnumAquaticPlant aqua = (EnumAquaticPlant) state.getValue(Constants.AQUATIC_PLANT_VARIANT);
		return Metadata.getMetadata(aqua == EnumAquaticPlant.CHARNIA ? EnumAquaticPlant.CHARNIA_TOP : aqua);
	}

	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		if (this.bases == null)
		{
			this.bases = Sets.newIdentityHashSet();
			this.bases.add(Blocks.dirt);
			this.bases.add(Blocks.sand);
			this.bases.add(Blocks.gravel);
			this.bases.add(Blocks.clay);
			this.bases.add(GenesisBlocks.red_clay);
			this.bases.add(GenesisBlocks.coral);
		}
		final IBlockState below = world.getBlockState(pos.down());
		EnumAquaticPlant aqua = (EnumAquaticPlant) state.getValue(Constants.AQUATIC_PLANT_VARIANT);
		Block block = below.getBlock();
		if (!this.bases.contains(block)
				&& block instanceof BlockGenesisRock == false
				&& (aqua != EnumAquaticPlant.CHARNIA_TOP || block != this || ((EnumAquaticPlant) below.getValue(Constants.AQUATIC_PLANT_VARIANT)) != EnumAquaticPlant.CHARNIA))
		{
			return false;
		}
		final IBlockState above = world.getBlockState(pos.up());
		if (above.getBlock().getMaterial() != Material.water)
		{
			return false;
		}
		if (aqua == EnumAquaticPlant.CHARNIA && world.getBlockState(pos.up().up()).getBlock().getMaterial() != Material.water)
		{
			return false;
		}
		final List<IBlockState> blocks = WorldUtils.getBlocksAround(world, pos);
		for (int i = 0; i < blocks.size();)
		{
			final boolean corner0 = this.isWaterish(blocks.get(i++).getBlock());
			final boolean corner1 = this.isWaterish(blocks.get(i++).getBlock());
			boolean corner2;
			if (i == blocks.size())
			{
				corner2 = this.isWaterish(blocks.get(0).getBlock());
			}
			else
			{
				corner2 = this.isWaterish(blocks.get(i).getBlock());
			}
			if (corner0 && corner1 && corner2)
			{
				return true;
			}
		}
		return false;
	}

	private boolean isWaterish(Block block)
	{
		return (block == Blocks.water) || block == Blocks.flowing_water || (block == this);
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, this.getVariant(), BlockLiquid.LEVEL);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	protected IProperty getVariant()
	{
		return Constants.AQUATIC_PLANT_VARIANT;
	}

	@Override
	protected Class getMetaClass()
	{
		return EnumAquaticPlant.class;
	}

}
