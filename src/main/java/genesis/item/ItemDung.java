package genesis.item;

import genesis.metadata.EnumDung;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemDung extends ItemMulti<EnumDung>
{
	public ItemDung(VariantsOfTypesCombo<EnumDung> owner, ObjectType<Block, ItemDung> type, List<EnumDung> variants, Class<EnumDung> variantClass)
	{
		super(owner, type, variants, variantClass);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (player.canPlayerEdit(pos.offset(side), side, stack) && ItemDye.applyBonemeal(stack, world, pos, player))
		{
			if (!world.isRemote)
			{
				world.playAuxSFX(2005, pos, 0);
			}
			
			return true;
		}
		
		return false;
	}
}
