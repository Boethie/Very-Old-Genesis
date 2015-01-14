package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockMoss extends BlockGrass {
    public BlockMoss() {
        super();
        setCreativeTab(GenesisCreativeTabs.BLOCK);
        setHarvestLevel("shovel", 0);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SNOWY);
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        switch (plantable.getPlantType(world, pos.up())) {
            case Cave:
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

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (worldIn.getLightFromNeighbors(pos.up()) > 0 || worldIn.getBlockState(pos.up()).getBlock().getLightOpacity(worldIn, pos.up()) > 2) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            } else if (worldIn.getLightFromNeighbors(pos.up()) < 13) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos pos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                    Block block = worldIn.getBlockState(pos1.up()).getBlock();
                    IBlockState state1 = worldIn.getBlockState(pos1);

                    if (state1.getBlock() == Blocks.dirt && state1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(pos1.up()) < 18 && block.getLightOpacity(worldIn, pos1.up()) <= 2) {
                        worldIn.setBlockState(pos1, Blocks.grass.getDefaultState());
                    }
                }
            }
        }
    }

    private boolean hasWater(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().getMaterial() == Material.water;
    }
}
