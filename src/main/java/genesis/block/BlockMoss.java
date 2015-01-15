package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
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
            if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getBlock().getLightOpacity(worldIn, pos.up()) > 2) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            } else if (worldIn.getLightFromNeighbors(pos.up()) <= 14) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos pos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                    Block block = worldIn.getBlockState(pos1.up()).getBlock();
                    IBlockState state1 = worldIn.getBlockState(pos1);

                    if (state1.getBlock() == Blocks.dirt && state1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(pos1.up()) <= 14 && block.getLightOpacity(worldIn, pos1.up()) <= 2) {
                        worldIn.setBlockState(pos1, GenesisBlocks.moss.getDefaultState());
                    }
                }
            }
        }
    }

    /**
     * @see ItemHoe#useHoe(ItemStack, EntityPlayer, World, BlockPos, IBlockState)
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getCurrentEquippedItem();

        if (stack != null && stack.getItem() instanceof ItemHoe) {
            if (!playerIn.canPlayerEdit(pos.offset(side), side, stack)) return false;

            if (side != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
                IBlockState newState = Blocks.farmland.getDefaultState();

                double x = (double) ((float) pos.getX() + 0.5F);
                double y = (double) ((float) pos.getY() + 0.5F);
                double z = (double) ((float) pos.getZ() + 0.5F);
                String soundName = newState.getBlock().stepSound.getStepSound();
                float volume = (newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F;
                float pitch = newState.getBlock().stepSound.getFrequency() * 0.8F;

                worldIn.playSoundEffect(x, y, z, soundName, volume, pitch);

                if (!worldIn.isRemote) {
                    worldIn.setBlockState(pos, newState);
                    stack.damageItem(1, playerIn);
                }

                return true;
            }
        }

        return false;
    }

    private boolean hasWater(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().getMaterial() == Material.water;
    }
}
