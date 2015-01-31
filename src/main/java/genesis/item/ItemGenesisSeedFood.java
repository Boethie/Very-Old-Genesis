package genesis.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemGenesisSeedFood extends ItemSeedFood
{
	IPlantable cropPlantable;
	Block cropBlock;
	
	public ItemGenesisSeedFood(int hunger, float saturation)
	{
		super(hunger, saturation, null, null);
	}
	
	public ItemGenesisSeedFood setCrop(IPlantable plantable)
	{
		cropPlantable = plantable;
		cropBlock = (Block) plantable;
		
		return this;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (side == EnumFacing.UP &&
				worldIn.isAirBlock(pos.up()) &&
				playerIn.canPlayerEdit(pos.offset(side), side, stack) &&
				worldIn.getBlockState(pos).getBlock().canSustainPlant(worldIn, pos, EnumFacing.UP, cropPlantable))
		{
			worldIn.setBlockState(pos.up(), cropBlock.getDefaultState());
			stack.stackSize--;
			
			return true;
		}
		
		return false;
	}

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
        return null;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos)
    {
        return null;
    }
}
