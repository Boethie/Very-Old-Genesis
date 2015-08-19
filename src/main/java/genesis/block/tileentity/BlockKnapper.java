package genesis.block.tileentity;

import java.util.Random;

import genesis.client.GenesisParticles;
import genesis.common.Genesis;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisGuiHandler;
import genesis.util.ItemStackKey;
import genesis.util.range.DoubleRange;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.*;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockKnapper extends Block
{
	public BlockKnapper()
	{
		super(Material.wood);
		
		setDefaultState(getBlockState().getBaseState());
		
		setHardness(2.5F);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	public TileEntityKnapper getTileEntity(IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tileEnt = worldIn.getTileEntity(pos);
		
		if (tileEnt instanceof TileEntityKnapper)
		{
			return (TileEntityKnapper) tileEnt;
		}
		
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityKnapper campfire = getTileEntity(world, pos);
		
		if (campfire != null)
		{
			player.openGui(Genesis.instance, GenesisGuiHandler.WORKBENCH_ID, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		
		return false;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		Random rand = world.rand;
		TileEntityKnapper workbench = getTileEntity(world, pos);
		
		if (workbench != null)
		{
			workbench.dropItems();
		}
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityKnapper();
	}
}
