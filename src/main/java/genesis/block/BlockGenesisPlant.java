package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.util.MetadataUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlockGenesisPlant extends BlockBush {
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumPlant.class);

    public BlockGenesisPlant() {
        setDefaultState(getBlockState().getBaseState().withProperty(VARIANT, EnumPlant.COOKSONIA));
        setCreativeTab(GenesisCreativeTabs.DECORATIONS);

        float size = 0.4F;
        setBlockBounds(0.5F - size, 0.0F, 0.5F - size, 0.5F + size, 0.8F, 0.5F + size);
    }

    @Override
    protected boolean canPlaceBlockOn(Block ground) {
        return ground == GenesisBlocks.moss || super.canPlaceBlockOn(ground);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumPlant) state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (int metadata = 0; metadata < EnumPlant.values().length; ++metadata) {
            list.add(new ItemStack(itemIn, 1, metadata));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, MetadataUtils.byMetadata(EnumPlant.class, meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumPlant) state.getValue(VARIANT)).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT);
    }

    @Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XYZ;
    }
}
