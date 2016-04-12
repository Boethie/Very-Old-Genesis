package genesis.client.model;

import java.util.*;

import com.google.common.collect.ImmutableMap;

import genesis.util.ItemStackKey;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MappedItemMeshDefinition implements ListedItemMeshDefinition
{
	protected ImmutableMap<ItemStackKey, ModelResourceLocation> map;
	
	public MappedItemMeshDefinition(Map<ItemStackKey, ModelResourceLocation> map)
	{
		this.map = ImmutableMap.copyOf(map);
	}
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return map.get(new ItemStackKey(stack));
	}
	
	@Override
	public Collection<ResourceLocation> getVariants()
	{
		Set<ResourceLocation> variants = new HashSet<>();
		
		for (ModelResourceLocation location : map.values())
		{
			variants.add(new ResourceLocation(location.getResourceDomain(), location.getResourcePath()));
		}
		
		return variants;
	}
}
