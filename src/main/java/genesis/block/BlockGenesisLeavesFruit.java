package genesis.block;

import java.util.Collections;
import java.util.List;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.VariantsOfTypesCombo.ObjectType;
import genesis.combo.variant.EnumTree;
import genesis.item.ItemBlockMulti;
import genesis.util.WorldUtils;
import genesis.util.WorldUtils.DropType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGenesisLeavesFruit extends BlockGenesisLeaves
{
	@BlockProperties
	public static final IProperty<?>[] STORE_PROPERTIES = BlockGenesisLeaves.STORE_PROPERTIES;
	
	public BlockGenesisLeavesFruit(TreeBlocksAndItems owner,
			ObjectType<BlockGenesisLeaves, ItemBlockMulti<EnumTree>> type,
			List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super(owner, type, variants, variantClass);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		List<ItemStack> drops = super.getDrops(world, pos, state, fortune);
		drops.add(owner.getStack(TreeBlocksAndItems.FRUIT, state.getValue(variantProp)));
		return drops;
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return Collections.singletonList(owner.getStack(TreeBlocksAndItems.LEAVES, world.getBlockState(pos).getValue(variantProp)));
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			EnumTree variant = state.getValue(variantProp);
			
			final double offset = 0.05;
			Vec3 itemPos = new Vec3(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
			itemPos.addVector(side.getFrontOffsetX() * offset, side.getFrontOffsetY() * offset, side.getFrontOffsetZ() * offset);
			WorldUtils.spawnItemsAt(world, itemPos, DropType.BLOCK,
					owner.getStack(TreeBlocksAndItems.FRUIT, variant));
			
			world.setBlockState(pos, owner.getBlockState(TreeBlocksAndItems.LEAVES, variant));
		}
		
		return true;
	}
}
