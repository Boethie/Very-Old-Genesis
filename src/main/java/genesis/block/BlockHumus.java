package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import java.util.List;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHumus extends BlockDirt
{
	public BlockHumus()
	{
		blockState = new BlockStateContainer(this);
		setDefaultState(blockState.getBaseState());
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}
	
	/**
	 * @see net.minecraft.item.ItemSpade#onItemUse
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (stack != null
				&& player.canPlayerEdit(pos.offset(facing), facing, stack)
				&& facing != EnumFacing.DOWN
				&& world.isAirBlock(pos.up()))
		{
			IBlockState newState = null;
			SoundEvent sound = null;
			
			if (stack.getItem() instanceof ItemSpade)
			{
				newState = GenesisBlocks.humus_path.getDefaultState();
				sound = SoundEvents.item_shovel_flatten;
			}
			else if (stack.getItem() instanceof ItemHoe)
			{
				newState = Blocks.farmland.getDefaultState();
				sound = SoundEvents.item_hoe_till;
			}
			
			if (newState != null)
			{
				if (sound != null)
				{
					world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				
				if (!world.isRemote)
				{
					world.setBlockState(pos, newState, 11);
					stack.damageItem(1, player);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	private void onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState newState, SoundEvent sound)
	{
		world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
		
		if (!world.isRemote)
		{
			world.setBlockState(pos, newState, 11);
			stack.damageItem(1, player);
		}
	}
	
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		return MapColor.dirtColor;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		list.add(new ItemStack(item));
	}
	
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
}
