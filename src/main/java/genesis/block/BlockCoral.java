package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.MetadataUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlockCoral extends BlockGenesis {
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumCoral.class);

    public BlockCoral() {
        super(Material.coral);
        setDefaultState(getBlockState().getBaseState().withProperty(VARIANT, EnumCoral.FAVOSITES));
        setCreativeTab(GenesisCreativeTabs.DECORATIONS);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumCoral) state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (int metadata = 0; metadata < EnumCoral.values().length; ++metadata) {
            list.add(new ItemStack(itemIn, 1, metadata));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, MetadataUtils.byMetadata(EnumCoral.class, meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumCoral) state.getValue(VARIANT)).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT);
    }
}
