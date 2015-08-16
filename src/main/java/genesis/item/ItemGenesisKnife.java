package genesis.item;

import genesis.metadata.ToolItems;
import genesis.metadata.ToolItems.ToolObjectType;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.VariantsOfTypesCombo.ItemVariantCount;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

@ItemVariantCount(1)
public class ItemGenesisKnife extends ItemTool
{
	public final ToolItems owner;
	
	protected final ToolType type;
	protected final ToolObjectType objType;
	
	public ItemGenesisKnife(ToolType type, ToolItems owner, ToolObjectType objType)
	{
		super(3, type.toolMaterial, Collections.emptySet());
		
		this.owner = owner;
		this.type = type;
		this.objType = objType;
	}
	
	@Override
	public int getMetadata(ItemStack stack)
	{
		return 0;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
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
