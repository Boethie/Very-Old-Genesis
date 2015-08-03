package genesis.item;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;

import genesis.common.GenesisItems;
import genesis.metadata.*;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.metadata.VariantsOfTypesCombo.*;

public class ItemCeramicBowl extends ItemGenesis
{
	public final ItemsCeramicBowls owner;
	
	protected final List<IMetadata> variants;
	protected final ObjectType<Block, ItemCeramicBowl> type;
	
	public ItemCeramicBowl(List<IMetadata> variants, ItemsCeramicBowls owner, ObjectType<Block, ItemCeramicBowl> type)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		this.variants = variants;
		
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		owner.fillSubItems(type, variants, subItems);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (owner.getVariant(stack) == EnumCeramicBowls.BOWL)
		{
			MovingObjectPosition hit = getMovingObjectPositionFromPlayer(world, player, true);
	
			if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				BlockPos hitPos = hit.getBlockPos();
				
				if (world.isBlockModifiable(player, hitPos) && player.canPlayerEdit(hitPos.offset(hit.sideHit), hit.sideHit, stack))
				{
					if (world.getBlockState(hitPos).getBlock().getMaterial() == Material.water)
					{
						player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
						
						player.swingItem();
						ItemStack newStack = GenesisItems.bowls.getStack(ItemsCeramicBowls.MAIN, EnumCeramicBowls.WATER_BOWL);
						stack.stackSize--;
						
						if (stack.stackSize <= 0)
						{
							return newStack;
						}
						
						if (!player.inventory.addItemStackToInventory(newStack))
						{
							player.dropPlayerItemWithRandomChoice(newStack, false);
						}
					}
				}
			}
		}
		
		return stack;
	}
}
