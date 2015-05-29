package genesis.item;

import genesis.common.GenesisConfig;
import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFlintAndMarcasite extends ItemFlintAndSteel
{
	public ItemFlintAndMarcasite()
	{
		setMaxDamage(GenesisConfig.flintAndMarcasiteMaxDamage);
		setCreativeTab(GenesisCreativeTabs.TOOLS);
	}

	@Override
	public ItemFlintAndMarcasite setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(Constants.PREFIX + "tool." + unlocalizedName);
		
		return this;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		pos = pos.offset(side);

		if (!playerIn.canPlayerEdit(pos, side, stack))
		{
			return false;
		}
		else
		{
			if (worldIn.isAirBlock(pos))
			{
				worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, Constants.ASSETS + "fire.ignite", 1, itemRand.nextFloat() * 0.4F + 0.8F);
				worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
			}

			stack.damageItem(1, playerIn);
			return true;
		}
	}
}
