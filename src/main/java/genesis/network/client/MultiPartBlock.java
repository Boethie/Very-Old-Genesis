package genesis.network.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultiPartBlock
{
	boolean activate(IBlockState state,
			World world, BlockPos pos, int part,
			EntityPlayer player, ItemStack stack, EnumHand hand);
	
	boolean removePart(IBlockState state,
			World world, BlockPos pos, int part,
			EntityPlayer player, boolean harvest);
}
