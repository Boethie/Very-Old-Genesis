package genesis.item;

import java.util.*;

import genesis.metadata.ToolItems;
import genesis.metadata.ToolItems.ToolObjectType;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.VariantsOfTypesCombo.ItemVariantCount;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

@ItemVariantCount(1)
public class ItemGenesisKnife extends ItemTool
{
	public final ToolItems owner;
	
	protected final ToolType type;
	protected final ToolObjectType<Block, ItemGenesisKnife> objType;
	
	public ItemGenesisKnife(ToolItems owner, ToolObjectType<Block, ItemGenesisKnife> objType, ToolType type, Class<ToolType> variantClass)
	{
		super(3, type.toolMaterial, Collections.<Block>emptySet());
		
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

	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
	{
		return Items.shears.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
	}
	
	@Override
	public boolean canHarvestBlock(Block block)
	{
		return Items.shears.canHarvestBlock(block);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, Block block)
	{
		return Items.shears.getStrVsBlock(stack, block);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity)
	{
		return Items.shears.itemInteractionForEntity(stack, player, entity);
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player)
	{
		return Items.shears.onBlockStartBreak(stack, pos, player);
	}
}
