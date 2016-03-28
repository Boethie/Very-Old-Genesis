package genesis.item;

import java.util.List;

import genesis.combo.ObjectType;
import genesis.combo.VariantsCombo;
import genesis.combo.variant.EnumSeeds;
import genesis.combo.variant.ToolTypes.ToolType;
import genesis.common.GenesisCreativeTabs;
import genesis.util.RandomReflection;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class ItemGenesisSeeds extends ItemSeedFood
{
	public final VariantsCombo<EnumSeeds, Block, ItemGenesisSeeds> owner;
	protected final ObjectType<Block, ItemGenesisSeeds> type;
	protected final List<EnumSeeds> variants;
	
	public ItemGenesisSeeds(VariantsCombo<EnumSeeds, Block, ItemGenesisSeeds> owner, ObjectType<Block, ItemGenesisSeeds> type, List<EnumSeeds> variants, Class<ToolType> variantClass)
	{
		super(0, 0, null, null);
		
		this.owner = owner;
		this.type = type;
		this.variants = variants;
		
		setHasSubtypes(true);
		
		setCreativeTab(GenesisCreativeTabs.FOOD);
	}
	
	protected boolean isFood(ItemStack stack)
	{
		return owner.getVariant(stack).getFoodAmount() > 0;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		EnumSeeds variant = owner.getVariant(stack);
		BlockPos placePos = pos.offset(side);
		IBlockState placeState = variant.getPlacedState();
		
		if (placeState != null)
		{
			Block placeBlock = placeState.getBlock();
			
			if (side == EnumFacing.UP &&
					world.isAirBlock(placePos) &&
					player.canPlayerEdit(placePos, side, stack) &&
					world.canBlockBePlaced(placeBlock, placePos, false, side, player, stack))
			{
				world.setBlockState(placePos, placeState);
				stack.stackSize--;
				placeBlock.onBlockPlacedBy(world, placePos, placeState, player, stack);
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return isFood(stack) ? EnumAction.EAT : EnumAction.NONE;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (isFood(stack))
		{
			boolean positive = false;
			
			for (PotionEffect effect : owner.getVariant(stack).getEffects())
			{
				if (!RandomReflection.isBadPotionEffect(effect))
				{
					positive = true;
					break;
				}
			}
			
			if (player.canEat(positive))
			{
				player.setItemInUse(stack, getMaxItemUseDuration(stack));
			}
		}
		
		return stack;
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			for (PotionEffect effect : owner.getVariant(stack).getEffects())
			{
				player.addPotionEffect(new PotionEffect(effect));
			}
		}
	}
	
	@Override
	public int getHealAmount(ItemStack stack)
	{
		return owner.getVariant(stack).getFoodAmount();
	}
	
	@Override
	public float getSaturationModifier(ItemStack stack)
	{
		return owner.getVariant(stack).getSaturationModifier();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
	{
		owner.fillSubItems(type, variants, subItems);
	}
	
	// Pointless IPlantable methods because they don't let us get the variant.
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{	// Return a default value so mods at least know that this is a seed still.
		return EnumPlantType.Plains;
	}
	
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return null;
	}
}
