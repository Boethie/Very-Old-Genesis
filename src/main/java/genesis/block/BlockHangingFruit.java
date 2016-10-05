package genesis.block;

import genesis.combo.ObjectType;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.item.ItemBlockMulti;
import genesis.util.BlockStateToMetadata;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.pipeline.BlockInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class BlockHangingFruit extends BlockGenesis
{
	@BlockProperties
	public static final IProperty<?>[] STORE_PROPERTIES = {};

	public final TreeBlocksAndItems owner;
	public final ObjectType<EnumTree, BlockHangingFruit, ItemBlockMulti<EnumTree>> type;

	public final List<EnumTree> variants;
	public final PropertyIMetadata<EnumTree> variantProp;

	public BlockHangingFruit(TreeBlocksAndItems owner,
			ObjectType<EnumTree, BlockHangingFruit, ItemBlockMulti<EnumTree>> type,
			List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super(Material.WOOD, GenesisSoundTypes.CONE);

		this.owner = owner;
		this.type = type;

		this.variants = variants;
		variantProp = new PropertyIMetadata<>("variant", variants, variantClass);

		blockState = new BlockStateContainer(this, variantProp);
		setDefaultState(getBlockState().getBaseState());
		
		setHarvestLevel("axe", 0);

		setHardness(0.2F);
	}

	public boolean canBlockStay(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return owner.isStateOf(world.getBlockState(pos.up()),
				state.getValue(variantProp),
				TreeBlocksAndItems.LEAVES, TreeBlocksAndItems.LEAVES_FRUIT);
	}

	protected boolean canBlockStay(IBlockAccess world, BlockPos pos)
	{
		return canBlockStay(world, pos, world.getBlockState(pos));
	}

	@Override
	public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack)
	{
		return side == EnumFacing.DOWN && canBlockStay(world, pos, owner.getBlockState(type, owner.getVariant(stack)));
	}

	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(world, pos, state))
		{
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		super.neighborChanged(state, world, pos, block);
		this.checkAndDropBlock(world, pos, state);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		checkAndDropBlock(world, pos, state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		EnumTree variant = state.getValue(variantProp);
		float radius = variant.getFruitWidth() / 2;
		float height = variant.getFruitHeight();

		float x = 0.5F;
		float y = 1;
		float z = 0.5F;

		BlockInfo offsetCalc = new BlockInfo(Minecraft.getMinecraft().getBlockColors());
		offsetCalc.setState(state);
		offsetCalc.setBlockPos(pos);
		offsetCalc.updateShift();
		x += offsetCalc.getShx();
		y += offsetCalc.getShy();
		z += offsetCalc.getShz();

		return new AxisAlignedBB(x - radius, y - height, z - radius, x + radius, y, z + radius);
	}

	@Override
	public EnumOffsetType getOffsetType()
	{
		return EnumOffsetType.XZ;
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
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return Collections.singletonList(owner.getStack(TreeBlocksAndItems.FRUIT, state.getValue(variantProp)));
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return owner.getStack(TreeBlocksAndItems.FRUIT, world.getBlockState(pos).getValue(variantProp));
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		//owner.fillSubItems(type, variants, list);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		return NULL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
}
