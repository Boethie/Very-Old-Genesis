package genesis.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumMaterial;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
import genesis.util.BlockStateToMetadata;
import genesis.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class BlockResin extends BlockGenesis implements IGrowable {

	public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 0, 3);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.HORIZONTALS));
	
	protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.3125D, 0.375D, 0.875D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.125D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.875D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.125D, 0.625D, 0.6875D);
	
	public static EnumTree[] allowedLogs = new EnumTree[]{EnumTree.CORDAITES, EnumTree.VOLTZIA, EnumTree.ARAUCARIOXYLON, EnumTree.METASEQUOIA};
	
	public BlockResin() {
		super(Material.wood, SoundType.SLIME);
		this.setTickRandomly(true);
        this.setCreativeTab(GenesisCreativeTabs.BLOCK);
        this.setHardness(3.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LAYERS, 3));
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return state.getValue(LAYERS)<3;//TODO Add more conditions
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		
		worldIn.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING)).withProperty(LAYERS, state.getValue(LAYERS)+1), 2);
	}

	protected static boolean func_181088_a(World p_181088_0_, BlockPos p_181088_1_, EnumFacing p_181088_2_)
    {
		if(p_181088_2_ == EnumFacing.DOWN || p_181088_2_ == EnumFacing.UP)
			return false;
        BlockPos blockpos = p_181088_1_.offset(p_181088_2_);
        return eqOneOfTrees(p_181088_0_.getBlockState(blockpos));
    }
	
	static IBlockState getTree(EnumTree tree)
	{
		return GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, tree);
	}
	
	static boolean eqOneOfTrees(IBlockState state)
	{
		for(EnumTree tree : allowedLogs)
			if(state.getBlock() == getTree(tree).getBlock())
				return true;
		
		return false;
	}
	
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return func_181088_a(worldIn, pos, side.getOpposite());
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing : EnumFacing.HORIZONTALS)
        {
            if (func_181088_a(worldIn, pos, enumfacing))
            {
                return true;
            }
        }

        return false;
    }
    
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(meta);
    }
    
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (this.checkForDrop(worldIn, pos, state) && !func_181088_a(worldIn, pos, ((EnumFacing)state.getValue(FACING)).getOpposite()))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.canPlaceBlockAt(worldIn, pos))
        {
            return true;
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
    }
    
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
    {
    	super.harvestBlock(worldIn, player, pos, state, te, stack);
    	
    	int n = state.getValue(LAYERS);
    	
    	if(n>0)
    	{
    		worldIn.setBlockState(pos, getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(LAYERS, Integer.valueOf(n-1)), 2);
    	}
    }
    
    public IBlockState getStateFromMeta(int meta)
    {
        return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, FACING, LAYERS);
    }
    
    public int getMetaFromState(IBlockState state)
    {
        return BlockStateToMetadata.getMetaForBlockState(state, FACING, LAYERS);
    }
    
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
    
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }
    
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, LAYERS);
    }
    
    @Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return Arrays.asList(new ItemStack[]{GenesisItems.materials.getStack(EnumMaterial.RESIN, 1+fortune)});
	}
    
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        
        switch (enumfacing)
        {
            case EAST:
                return AABB_EAST;
            case WEST:
                return AABB_WEST;
            case SOUTH:
                return AABB_SOUTH;
            case NORTH:
            default:
                return AABB_NORTH;
        }
    }
    
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
}
