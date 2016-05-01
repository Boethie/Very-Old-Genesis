package genesis.item;

import genesis.util.Actions;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class ItemBlockFloating extends ItemBlock
{
	public ItemBlockFloating(Block block)
	{
		super(block);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		RayTraceResult hit = getMovingObjectPositionFromPlayer(world, player, true);
		
		if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			float x = (float) hit.hitVec.xCoord;
			float y = (float) hit.hitVec.yCoord;
			float z = (float) hit.hitVec.zCoord;
			BlockPos hitPos = hit.getBlockPos().up();
			
			if (world.isBlockModifiable(player, hitPos)
					&& player.canPlayerEdit(hitPos.offset(hit.sideHit), hit.sideHit, stack)
					&& world.canBlockBePlaced(block, hitPos, true, EnumFacing.UP, player, stack))
			{
				IBlockState placing = block.onBlockPlaced(world, hitPos,
						EnumFacing.UP, x, y, z,
						getMetadata(stack.getMetadata()), player);
				
				if (placeBlockAt(stack, player, world, hitPos,
						EnumFacing.UP, z, y, z,
						placing))
				{
					if (!player.capabilities.isCreativeMode)
						stack.stackSize--;
					
					SoundType sound = block.getSoundType();
					world.playSound(player, hitPos, sound.getPlaceSound(), SoundCategory.BLOCKS,
							(sound.getVolume() + 1) / 2, sound.getPitch() * 0.8F);
					
					player.swingArm(hand);
					return Actions.success(stack);
				}
			}
			
			// Send an update for the block in case it was placed on the client and not the server.
			if (player instanceof EntityPlayerMP)
				((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new SPacketBlockChange(world, hitPos));
		}
		
		return Actions.fail(stack);
	}
}
