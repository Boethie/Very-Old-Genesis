package genesis.block;

import java.util.List;
import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.VariantsOfTypesCombo.ObjectType;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.PropertyIMetadata;
import genesis.item.ItemBlockMulti;
import genesis.util.BlockStateToMetadata;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHangingFruit extends BlockGenesis
{
	@BlockProperties
	public static final IProperty<?>[] STORE_PROPERTIES = {};
	
	public final TreeBlocksAndItems owner;
	public final ObjectType<BlockHangingFruit, ItemBlockMulti<EnumTree>> type;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata<EnumTree> variantProp;
	
	public BlockHangingFruit(TreeBlocksAndItems owner,
			ObjectType<BlockHangingFruit, ItemBlockMulti<EnumTree>> type,
			List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super(Material.wood);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<EnumTree>("variant", variants, variantClass);
		
		blockState = new BlockState(this, variantProp);
		setDefaultState(getBlockState().getBaseState());
		
		setStepSound(soundTypeGrass);
		setHardness(0.25F);
	}
	
	public boolean canBlockStay(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return owner.isStateOf(world.getBlockState(pos.up()),
				state.getValue(variantProp),
				TreeBlocksAndItems.LEAVES, TreeBlocksAndItems.LEAVES_FRUIT);
	}
	
	protected boolean canBlockStay(IBlockAccess world, BlockPos pos)
	{
		return canBlockStay(world, pos);
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
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(world, pos, state, neighborBlock);
		
		checkAndDropBlock(world, pos, state);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		checkAndDropBlock(world, pos, state);
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
		return owner.getItemMetadata(type, state.getValue(variantProp));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
	{
		return owner.getStack(type, world.getBlockState(pos).getValue(variantProp));
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state)
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
	
	@SideOnly(Side.CLIENT)
	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}
}
