package genesis.client.model;

import java.util.Collection;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public interface ListedItemMeshDefinition extends ItemMeshDefinition
{
	Collection<ModelResourceLocation> getVariants();
}
