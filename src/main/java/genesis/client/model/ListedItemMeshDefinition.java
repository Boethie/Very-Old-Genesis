package genesis.client.model;

import java.util.Collection;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.util.ResourceLocation;

public interface ListedItemMeshDefinition extends ItemMeshDefinition
{
	Collection<ResourceLocation> getVariants();
}
