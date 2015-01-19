package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockNewPermafrost extends BlockPermafrost {
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = super.getDrops(world, pos, state, fortune);
        Random random = world instanceof World ? ((World) world).rand : RANDOM;
        Item drop = getDrop(random.nextInt(100));

        if (drop != null) {
            drops.add(new ItemStack(drop));
        }

        return drops;
    }

    private Item getDrop(int chance) {
        //if (chance < 3) return GenesisItems.necklace;
        if (chance < 5) return Items.arrow;
        else if (chance < 7) return Items.bow;
        else if (chance < 10) return Items.wooden_pickaxe;//pick
        else if (chance < 35) return Items.bone;
        else if (chance < 40) return GenesisItems.eryops_leg;//meat
        return null;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(GenesisBlocks.permafrost);
    }
}
