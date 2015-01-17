package genesis.client;

import genesis.block.EnumCoral;
import genesis.block.EnumPlant;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisProxy;
import genesis.item.ItemGenesisMetadata;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Iterator;

public class GenesisClient extends GenesisProxy {
    private final ArrayList<ItemTexture> itemTextures = new ArrayList<ItemTexture>();
    private boolean hasInit = false;

    @Override
    public void registerBlock(Block block, String name) {
        super.registerBlock(block, name);
        registerModel(block, name);
    }

    @Override
    public void registerItem(Item item, String name) {
        super.registerItem(item, name);

        if (item instanceof ItemGenesisMetadata) {
            String[] textureNames = ((ItemGenesisMetadata) item).getNames();

            for (int metadata = 0; metadata < textureNames.length; metadata++) {
                String textureName = name + "_" + textureNames[metadata];
                registerModel(item, metadata, textureName);
                addVariantName(item, textureName);
            }
        } else {
            registerModel(item, name);
        }
    }

    @Override
    public void preInit() {
        // Variant names must be added during pre init
        for (int metadata = 0; metadata < EnumCoral.values().length; metadata++) {
            String textureName = EnumCoral.values()[metadata].getName();
            registerModel(GenesisBlocks.coral, metadata, textureName);
            addVariantName(GenesisBlocks.coral, textureName);
        }

        for (int metadata = 0; metadata < EnumPlant.values().length; metadata++) {
            String textureName = EnumPlant.values()[metadata].getName();
            registerModel(GenesisBlocks.plant, metadata, textureName);
            addVariantName(GenesisBlocks.plant, textureName);
        }

        // TODO: Cannot add prefix "genesis" when registering variants!
        //Minecraft.getMinecraft().modelManager.getBlockModelShapes().registerBlockWithStateMapper(GenesisBlocks.coral, (new StateMap.Builder()).setProperty(BlockCoral.VARIANT).build());
    }

    @Override
    public void init() {
        hasInit = true;

        Iterator<ItemTexture> iterator = itemTextures.iterator();
        while (iterator.hasNext()) {
            ItemTexture texture = iterator.next();
            registerModel(texture.item, texture.metadata, texture.name);
            iterator.remove();
        }

        registerModel(GenesisBlocks.moss, "moss");
    }

    private void registerModel(Block block, String textureName) {
        registerModel(block, 0, textureName);
    }

    private void registerModel(Block block, int metadata, String textureName) {
        registerModel(Item.getItemFromBlock(block), metadata, textureName);
    }

    private void registerModel(Item item, String textureName) {
        registerModel(item, 0, textureName);
    }

    private void registerModel(Item item, int metadata, String textureName) {
        if (!hasInit) {
            itemTextures.add(new ItemTexture(item, metadata, textureName));
        } else {
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, metadata, new ModelResourceLocation(Constants.ASSETS + textureName, "inventory"));
        }
    }

    private void addVariantName(Block block, String name) {
        addVariantName(Item.getItemFromBlock(block), name);
    }

    private void addVariantName(Item item, String name) {
        ModelBakery.addVariantName(item, Constants.ASSETS + name);
    }

    private class ItemTexture {
        private final Item item;
        private final int metadata;
        private final String name;

        private ItemTexture(Item item, int metadata, String name) {
            this.item = item;
            this.metadata = metadata;
            this.name = name;
        }
    }
}
