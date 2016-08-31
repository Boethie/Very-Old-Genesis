package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.util.WorldUtils;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockHumus extends Block
{
	public BlockHumus()
	{
		super(Material.GROUND);
		
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
				sound = SoundEvents.ITEM_SHOVEL_FLATTEN;
			}
			else if (stack.getItem() instanceof ItemHoe)
			{
				newState = Blocks.FARMLAND.getDefaultState();
				sound = SoundEvents.ITEM_HOE_TILL;
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
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		switch (plantable.getPlantType(world, pos.up()))
		{
		case Cave:
		case Plains:
		case Desert:
			return true;
		case Beach:
			return WorldUtils.isWater(world, pos.east()) ||
					WorldUtils.isWater(world, pos.west()) ||
					WorldUtils.isWater(world, pos.north()) ||
					WorldUtils.isWater(world, pos.south());
		default:
			return false;
		}
	}
}
