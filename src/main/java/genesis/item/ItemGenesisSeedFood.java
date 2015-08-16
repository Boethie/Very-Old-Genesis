package genesis.item;

import genesis.common.GenesisCreativeTabs;
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
		
		setCreativeTab(GenesisCreativeTabs.FOOD);
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
		BlockPos placePos = pos.offset(side);
		IBlockState state = worldIn.getBlockState(placePos);
		
		if (side == EnumFacing.UP &&
				worldIn.isAirBlock(placePos) &&
				playerIn.canPlayerEdit(placePos, side, stack) &&
				cropBlock.canPlaceBlockOnSide(worldIn, placePos, side) &&
				worldIn.canBlockBePlaced(cropBlock, placePos, false, side, null, stack))
		{
			worldIn.setBlockState(placePos, cropBlock.getDefaultState());
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
