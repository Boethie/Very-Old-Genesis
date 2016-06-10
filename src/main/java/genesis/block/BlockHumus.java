package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHumus extends Block
{
	public BlockHumus()
	{
		super(Material.ground);
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		
		setHardness(0.5F);
		setSoundType(SoundType.GROUND);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this);
	}
	
	/**
	 * @see ItemHoe#onItemUse
	 * @see ItemSpade#onItemUse
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
				world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
				
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
}
