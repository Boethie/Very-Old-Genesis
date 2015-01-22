package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import genesis.util.Metadata;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.List;

public class BlockCoral extends BlockGenesis {
    public BlockCoral() {
        super(Material.coral);
        setDefaultState(getBlockState().getBaseState().withProperty(Constants.CORAL_VARIANT, EnumCoral.FAVOSITES));
        setCreativeTab(GenesisCreativeTabs.DECORATIONS);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return Metadata.getMetadata(state, Constants.CORAL_VARIANT);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        Metadata.getSubItems(EnumCoral.class, list);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return Metadata.getState(this, Constants.CORAL_VARIANT, EnumCoral.class, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return Metadata.getMetadata(state, Constants.CORAL_VARIANT);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, Constants.CORAL_VARIANT);
    }
}
