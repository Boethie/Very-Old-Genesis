package genesis.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import genesis.util.StreamUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class MetadataModelDefinition implements ListedItemMeshDefinition
{
	public static MetadataModelDefinition from(Item item, Function<ItemStack, ResourceLocation> func)
	{
		return new MetadataModelDefinition(item)
		{
			@Override
			protected ModelResourceLocation getName(ItemStack stack)
			{
				ResourceLocation loc = func.apply(stack);
				
				if (loc instanceof ModelResourceLocation)
					return (ModelResourceLocation) loc;
				
				return new ModelResourceLocation(loc.toString(), "inventory");
			}
		};
	}
	
	final Item item;
	
	public MetadataModelDefinition(Item item)
	{
		this.item = item;
	}
	
	protected abstract ModelResourceLocation getName(ItemStack stack);
	
	@Override
	public Collection<ModelResourceLocation> getVariants()
	{
		List<ItemStack> stacks = new ArrayList<>();
		item.getSubItems(item, item.getCreativeTab(), stacks);
		return stacks.stream().map(this::getName).collect(StreamUtils.toImmSet());
	}
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return getName(stack);
	}
}
