package genesis.item;

import genesis.block.BlockBenchSeat;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemBenchSeat extends ItemBlock
{
	public ItemBenchSeat(BlockBenchSeat benchSeat)
	{
		super(benchSeat);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		else if (facing == EnumFacing.UP)
		{
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			boolean replaceable = block.isReplaceable(world, pos);
			
			int bedHorizontal = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			EnumFacing bedFacing = EnumFacing.getHorizontal(bedHorizontal);
			
			BlockPos footPos = replaceable ? pos : pos.up();
			BlockPos headPos = footPos.offset(bedFacing);
			
			if (player.canPlayerEdit(footPos, facing, stack) && player.canPlayerEdit(headPos, facing, stack))
			{
				BlockPos belowFoot = footPos.down();
				BlockPos belowHead = headPos.down();
				
				if ((replaceable || world.isAirBlock(footPos))
						&& (world.getBlockState(headPos).getBlock().isReplaceable(world, headPos) || world.isAirBlock(headPos))
						&& (world.getBlockState(belowFoot).isSideSolid(world, belowFoot, EnumFacing.UP))
						&& (world.getBlockState(belowHead).isSideSolid(world, belowHead, EnumFacing.UP)))
				{
					IBlockState footState = getBlock().getDefaultState().withProperty(BlockBenchSeat.FACING, bedFacing).withProperty(BlockBenchSeat.PART, BlockBenchSeat.EnumPartType.FOOT);
					
					if (world.setBlockState(footPos, footState, 11))
					{
						IBlockState headState = footState.withProperty(BlockBenchSeat.PART, BlockBenchSeat.EnumPartType.HEAD);
						world.setBlockState(headPos, headState, 11);
					}
					
					SoundType sound = footState.getBlock().getSoundType();
					world.playSound(null, footPos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
					--stack.stackSize;
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.FAIL;
	}
}
