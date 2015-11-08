package genesis.client.model;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import genesis.util.ItemStackKey;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
		return map.values().stream().map(ModelResourceLocation::getResourcePath).collect(Collectors.toList());
	}
}
