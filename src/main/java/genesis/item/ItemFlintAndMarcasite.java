package genesis.item;

import genesis.combo.variant.EnumAquaticPlant;
import genesis.common.GenesisConfig;
import genesis.common.GenesisCreativeTabs;
import genesis.world.biome.decorate.WorldGenAquaticPlants;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFlintAndMarcasite extends ItemFlintAndSteel
{
	public ItemFlintAndMarcasite()
	{
		setMaxDamage(GenesisConfig.flintAndMarcasiteMaxDamage);
		setCreativeTab(GenesisCreativeTabs.TOOLS);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ)
	{
		pos = pos.offset(side);
		
		//if (player.canPlayerEdit(pos, side, stack) && world.isAirBlock(pos))
		{
			/*world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					GenesisSoundEvents.item_flint_and_marcasite_use, SoundCategory.BLOCKS,
					1, world.rand.nextFloat() * 0.4F + 0.8F, false);
			
			world.setBlockState(pos, Blocks.fire.getDefaultState());
			stack.damageItem(1, player);*/
			
			new WorldGenAquaticPlants(EnumAquaticPlant.BANGIOMORPHA).generate(world, world.rand, pos);
			
			return EnumActionResult.SUCCESS;
		}
		
		//return EnumActionResult.FAIL;
	}
}
