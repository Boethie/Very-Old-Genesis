package genesis.block;

import com.google.common.collect.Lists;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.event.GenesisEventHandler;
import genesis.util.blocks.ISitOnBlock;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("UnnecessaryUnboxing")
public class BlockSmoker extends BlockGenesis implements ISitOnBlock
{

	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);//So the game doesn't crash
	public static final ArrayList<Material> materialsThisCanBePlacedOn = Lists.newArrayList(Material.SAND, Material.ROCK, Material.GROUND, Material.CLAY);

	public BlockSmoker(Material material, SoundType sound)
	{
		super(material, sound);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
		setTickRandomly(true);
		setHarvestLevel("pickaxe", 0);
		setHardness(1.5F);
		setResistance(10.0F);
	}

	/*@Override
	public int tickRate(World worldIn)
	{
		return 300;
	}*/

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return new AxisAlignedBB(0.25F, 0F, 0.25F, 0.75F, 1.0F, 0.75F);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((Integer)state.getValue(LEVEL)).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {LEVEL});
	}

	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (worldIn.getBlockState(pos.up()).getMaterial().isLiquid() && !(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockSmoker))
		{
			Random random = worldIn.rand;

			for (int i = 0; i < 8; ++i)
			{
				double x = pos.getX() + 0.5;
				double y = pos.up().getY() + i * 0.1;
				double z = pos.getZ() + 0.5;

				worldIn.spawnParticle(random.nextInt(10) == 0 ? EnumParticleTypes.WATER_BUBBLE : EnumParticleTypes.SMOKE_LARGE, x, y, z, 0.0D, 0.01D, 0.0D);
			}
		}
	}

	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		if(!canPlaceBlockAt(worldIn, pos))
			worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
		if(getMetaFromState(state) == 0 && worldIn.getBlockState(pos.down()).getBlock() instanceof BlockSmoker)
			worldIn.setBlockState(pos, state.withProperty(LEVEL, 1));
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);

		if(world instanceof World){
			if(!canPlaceBlockAt((World)world, pos)) {
				((World) world).setBlockState(pos, Blocks.WATER.getDefaultState());
				if(world.getBlockState(pos.up()).getBlock() instanceof BlockSmoker)
					GenesisEventHandler.updateBlocksT1.add(pos);
			}
			if(getMetaFromState(world.getBlockState(pos)) == 0 && world.getBlockState(pos.down()).getBlock() instanceof BlockSmoker)
				((World)world).setBlockState(pos, getDefaultState().withProperty(LEVEL, 1));
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.up()).getMaterial() == Material.WATER && (materialsThisCanBePlacedOn.contains(worldIn.getBlockState(pos.down()).getMaterial()) || worldIn.getBlockState(pos.down()).getBlock() instanceof BlockSmoker);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack)
	{
		if(world.getBlockState(pos.down()).getBlock() instanceof BlockSmoker)
			return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(1));
		return getDefaultState();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(GenesisBlocks.SMOKER);
	}
}
