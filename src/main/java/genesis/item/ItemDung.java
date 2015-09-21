package genesis.item;

import genesis.metadata.IMetadata;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ItemDung extends ItemMulti
{
	public ItemDung(List<IMetadata> variants, VariantsOfTypesCombo owner, ObjectType type)
	{
		super(variants, owner, type);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if (playerIn.canPlayerEdit(pos.offset(side), side, stack) && ItemDye.applyBonemeal(stack, worldIn, pos, playerIn))
		{
			if (!worldIn.isRemote)
			{
				worldIn.playAuxSFX(2005, pos, 0);
			}

			return true;
		}
		
		return false;
    }
}
