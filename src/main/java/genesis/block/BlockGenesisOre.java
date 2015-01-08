package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class BlockGenesisOre extends BlockOre {
    private final ItemStack drop;

    public BlockGenesisOre(Block blockDrop) {
        this(new ItemStack(blockDrop));
    }

    public BlockGenesisOre(Item itemDrop) {
        this(new ItemStack(itemDrop));
    }

    public BlockGenesisOre(ItemStack drop) {
        setCreativeTab(GenesisCreativeTabs.BLOCK);
        this.drop = drop;
    }

    @Override
    public Block setUnlocalizedName(String unlocalizedName) {
        return super.setUnlocalizedName(Constants.setUnlocalizedName(unlocalizedName));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return getDrop().getItem();
    }

    @Override
    public int quantityDropped(Random random) {
        return getDrop().stackSize;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getDrop().getMetadata();
    }

    public ItemStack getDrop() {
        return drop;
    }
}
