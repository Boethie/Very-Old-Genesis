package genesis.block;

import genesis.block.tileentity.TileEntityRack;
import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRack extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockRack() {
        super(Material.wood);
        setCreativeTab(GenesisCreativeTabs.DECORATIONS);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
                                ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityRack();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityRack)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityRack) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityRack rack = (TileEntityRack) world.getTileEntity(pos);
        if (rack.getStackInSlot(0) == null && !(TileEntityRack.isItemValid(heldItem)))
        {
            if (heldItem != null)
            {
                ItemStack item = heldItem.copy();
                item.stackSize = 1;
                rack.setInventorySlotContents(0, item);
                heldItem.stackSize--;
            }
        }
        else
        {
            if (heldItem == null)
            {
                //SERVER ONLY
                if (!world.isRemote)
                {
                    InventoryHelper.dropInventoryItems(world, pos, rack);
                    world.updateComparatorOutputLevel(pos, this);
                }
                rack.setInventorySlotContents(0, null);
                rack.markDirty();
            }
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        EnumFacing facing = state.getValue(FACING);
        return facing.getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        float pixel = 1.0F / 16;
        switch (state.getValue(FACING)) {
            default:
                return new AxisAlignedBB(0, pixel * 12, pixel * 14, 1,
                        1, 1);
            case NORTH:
                return new AxisAlignedBB(0, pixel * 12, pixel * 14, 1,
                        1, 1);
            case SOUTH:
                return new AxisAlignedBB(0, pixel * 12, 0, 1,
                        1, pixel * 2);
            case WEST:
                return new AxisAlignedBB(pixel * 14, pixel * 12, 0, 1,
                        1, 1);
            case EAST:
                return new AxisAlignedBB(0, pixel * 12, 0, pixel * 2,
                        1, 1);
        }
    }
}
