package genesis.client.model;

import com.google.common.collect.ImmutableMap;
import genesis.util.ItemStackKey;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

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
	public Collection<ModelResourceLocation> getVariants()
	{
		return map.values().stream().map(location -> new ModelResourceLocation(location.getResourceDomain() + ":" + location.getResourcePath(), "inventory")).collect(Collectors.toSet());
	}
}
