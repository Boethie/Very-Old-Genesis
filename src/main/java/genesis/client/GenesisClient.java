package genesis.client;

import genesis.common.GenesisProxy;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenesisClient extends GenesisProxy {
    private final HashMap<Item, String> itemTextures = new HashMap<Item, String>();
    private boolean hasInit = false;

    @Override
    public void registerBlock(Block block, String name) {
        super.registerBlock(block, name);
        registerModel(block, name);
    }

    @Override
    public void registerItem(Item item, String name) {
        super.registerItem(item, name);
        registerModel(item, name);
    }

    @Override
    public void init() {
        hasInit = true;

        Iterator<Map.Entry<Item, String>> iterator = itemTextures.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Item, String> entry = iterator.next();
            registerModel(entry.getKey(), entry.getValue());
            iterator.remove();
        }
    }

    private void registerModel(Block block, String textureName) {
        registerModel(Item.getItemFromBlock(block), textureName);
    }

    private void registerModel(Item item, String textureName) {
        if (!hasInit) {
            itemTextures.put(item, textureName);
        } else {
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Constants.MOD_ID + ":" + textureName, "inventory"));
        }
    }
}
