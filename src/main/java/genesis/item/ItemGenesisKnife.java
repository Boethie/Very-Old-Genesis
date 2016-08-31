package genesis.item;

import java.util.*;

import genesis.combo.ToolItems;
import genesis.combo.ToolItems.ToolObjectType;
import genesis.combo.VariantsOfTypesCombo.ItemVariantCount;
import genesis.combo.variant.ToolTypes.ToolType;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ItemVariantCount(1)
public class ItemGenesisKnife extends ItemTool
{
	public final ToolItems owner;
	
	protected final ToolType type;
	protected final ToolObjectType<Block, ItemGenesisKnife> objType;
	
	public ItemGenesisKnife(ToolItems owner, ToolObjectType<Block, ItemGenesisKnife> objType, ToolType type, Class<ToolType> variantClass)
	{
		super(3F, -1F, type.toolMaterial, Collections.emptySet());
		
		this.owner = owner;
		this.type = type;
		this.objType = objType;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		owner.addToolInformation(stack, playerIn, tooltip, advanced);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity)
	{
		return Items.SHEARS.onBlockDestroyed(stack, world, state, pos, entity);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState state)
	{
		return Items.SHEARS.canHarvestBlock(state);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return Items.SHEARS.getStrVsBlock(stack, state);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand)
	{
		return Items.SHEARS.itemInteractionForEntity(stack, player, entity, hand);
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player)
	{
		return Items.SHEARS.onBlockStartBreak(stack, pos, player);
	}
}
