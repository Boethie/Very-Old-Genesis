package genesis.item;

import java.util.List;

import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.variant.EnumMaterial;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemMaterial extends ItemMulti<EnumMaterial>
{
	public ItemMaterial(VariantsOfTypesCombo<EnumMaterial> owner,
			ObjectType<EnumMaterial, ? extends Block, ? extends ItemMulti<EnumMaterial>> type,
			List<EnumMaterial> variants, Class<EnumMaterial> variantClass)
	{
		super(owner, type, variants, variantClass);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world,
			BlockPos pos, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ)
	{
		EnumMaterial material = owner.getVariant(stack);
		
		switch (material)
		{
		case VEGETAL_ASH:
			if (player.canPlayerEdit(pos.offset(side), side, stack) && ItemDye.applyBonemeal(stack, world, pos, player))
			{
				if (!world.isRemote)
				{
					world.playEvent(2005, pos, 0);
				}
				
				return EnumActionResult.SUCCESS;
			}
			
			return EnumActionResult.FAIL;
		default:
			break;
		}
		
		return EnumActionResult.PASS;
	}
}
