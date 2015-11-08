package genesis.item;

import java.util.List;

import genesis.metadata.IMetadata;
import genesis.metadata.ItemsCeramicBowls;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemDyeBowl extends ItemMulti<IMetadata>
{
	protected final ItemsCeramicBowls bowlsOwner;
	
	public ItemDyeBowl(List<IMetadata> variants, ItemsCeramicBowls owner, ObjectType<? extends Block, ? extends ItemMulti<IMetadata>> type)
	{
		super(variants, owner, type);
		
		bowlsOwner = owner;
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		return bowlsOwner.getStack(EnumCeramicBowls.BOWL);
	}
}
