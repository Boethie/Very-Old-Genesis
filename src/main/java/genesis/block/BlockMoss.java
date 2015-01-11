package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockMoss extends BlockGrass {
    public BlockMoss() {
        super();
        setCreativeTab(GenesisCreativeTabs.BLOCK);
        setHarvestLevel("shovel", 0);
    }

    @Override
    public Block setUnlocalizedName(String unlocalizedName) {
        return super.setUnlocalizedName(Constants.setUnlocalizedName(unlocalizedName));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SNOWY);
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        switch (plantable.getPlantType(world, pos.up())) {
            case Plains:
                return true;
            case Beach:
                return hasWater(world, pos.east()) || hasWater(world, pos.west()) || hasWater(world, pos.north()) || hasWater(world, pos.south());
        }

        return super.canSustainPlant(world, pos, direction, plantable);
    }

    @Override
    public void onPlantGrow(World world, BlockPos pos, BlockPos source) {
        world.setBlockState(pos, net.minecraft.init.Blocks.dirt.getDefaultState(), 2);
    }

    private boolean hasWater(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().getMaterial() == Material.water;
    }
}
