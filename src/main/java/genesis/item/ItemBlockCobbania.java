package genesis.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemLilyPad;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		RayTraceResult hit = getRayTraceResultFromPlayer(world, player, true);

		if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			BlockPos hitPos = hit.getBlockPos();

			if (world.isBlockModifiable(player, hitPos) && player.canPlayerEdit(hitPos.offset(hit.sideHit), hit.sideHit, stack))
			{
				IBlockState hitState = world.getBlockState(hitPos);
				BlockPos placePos = hitPos.up();
	
				if (hitState.getBlock().getMaterial() == Material.water
					&& hitState.getValue(BlockLiquid.LEVEL) == 0
					&& world.isAirBlock(placePos))
				{
					world.setBlockState(placePos, block.getDefaultState());
					player.swingItem();
	
					if (!player.capabilities.isCreativeMode)
					{
						--stack.stackSize;
					}
	
					player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
				}
			}
		}

		return stack;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return block.getRenderColor(block.getStateFromMeta(stack.getMetadata()));
	}
}
