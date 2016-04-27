package genesis.item;

import genesis.util.Actions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemLilyPad;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockCobbania extends ItemLilyPad
{
	public ItemBlockCobbania(Block block)
	{
		super(block);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		RayTraceResult hit = getMovingObjectPositionFromPlayer(world, player, true);
		
		if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			BlockPos hitPos = hit.getBlockPos();

			if (world.isBlockModifiable(player, hitPos) && player.canPlayerEdit(hitPos.offset(hit.sideHit), hit.sideHit, stack))
			{
				IBlockState hitState = world.getBlockState(hitPos);
				BlockPos placePos = hitPos.up();
				
				if (hitState.getMaterial() == Material.water
					&& hitState.getValue(BlockLiquid.LEVEL) == 0
					&& world.isAirBlock(placePos))
				{
					world.setBlockState(placePos, block.getDefaultState());
					
					if (!player.capabilities.isCreativeMode)
					{
						--stack.stackSize;
					}
					
					player.addStat(StatList.getObjectUseStats(this));
					Actions.success(stack);
				}
			}
		}
		
		return Actions.fail(stack);
	}
	
	/*@SideOnly(Side.CLIENT)
	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return block.getRenderColor(block.getStateFromMeta(stack.getMetadata()));
	}*/
}
