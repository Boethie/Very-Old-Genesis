package genesis.client.model;

import java.util.*;

import com.google.common.collect.ImmutableMap;

import genesis.util.ItemStackKey;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

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
	public Collection<String> getVariants()
	{
		Set<String> variants = new HashSet<String>();
		
		for (ModelResourceLocation location : map.values())
		{
			variants.add(location.getResourcePath());
		}
		
		return variants;
	}
}
