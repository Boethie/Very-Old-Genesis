package genesis.block.tileentity;

import genesis.common.Genesis;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisGuiHandler;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.*;

public class BlockKnapper extends Block
{
	public BlockKnapper()
	{
		super(Material.WOOD);
		
		setDefaultState(getBlockState().getBaseState());
		
		setSoundType(SoundType.WOOD);
		setHardness(2.5F);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	public static TileEntityKnapper getTileEntity(IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEnt = world.getTileEntity(pos);
		
		if (tileEnt instanceof TileEntityKnapper)
		{
			return (TileEntityKnapper) tileEnt;
		}
		
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumHand hand, ItemStack held,
			EnumFacing side, float hitX, float hitY, float hitZ)
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
