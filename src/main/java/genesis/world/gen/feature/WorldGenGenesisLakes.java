package genesis.world.gen.feature;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenGenesisLakes extends WorldGenerator
{
    private Block field_150556_a;

    public WorldGenGenesisLakes(Block p_i45455_1_)
    {
        this.field_150556_a = p_i45455_1_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (p_180709_3_ = p_180709_3_.add(-8, 0, -8); p_180709_3_.getY() > 5 && worldIn.isAirBlock(p_180709_3_); p_180709_3_ = p_180709_3_.down())
        {
            ;
        }

        if (p_180709_3_.getY() <= 4)
        {
            return false;
        }
        else
        {
            p_180709_3_ = p_180709_3_.down(4);
            boolean[] aboolean = new boolean[2048];
            int i = p_180709_2_.nextInt(4) + 4;
            int j;

            for (j = 0; j < i; ++j)
            {
                double d0 = p_180709_2_.nextDouble() * 6.0D + 3.0D;
                double d1 = p_180709_2_.nextDouble() * 4.0D + 2.0D;
                double d2 = p_180709_2_.nextDouble() * 6.0D + 3.0D;
                double d3 = p_180709_2_.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                double d4 = p_180709_2_.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                double d5 = p_180709_2_.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                for (int l = 1; l < 15; ++l)
                {
                    for (int i1 = 1; i1 < 15; ++i1)
                    {
                        for (int j1 = 1; j1 < 7; ++j1)
                        {
                            double d6 = ((double)l - d3) / (d0 / 2.0D);
                            double d7 = ((double)j1 - d4) / (d1 / 2.0D);
                            double d8 = ((double)i1 - d5) / (d2 / 2.0D);
                            double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                            if (d9 < 1.0D)
                            {
                                aboolean[(l * 16 + i1) * 8 + j1] = true;
                            }
                        }
                    }
                }
            }

            int k;
            int k1;
            boolean flag;

            for (j = 0; j < 16; ++j)
            {
                for (k1 = 0; k1 < 16; ++k1)
                {
                    for (k = 0; k < 8; ++k)
                    {
                        flag = !aboolean[(j * 16 + k1) * 8 + k] && (j < 15 && aboolean[((j + 1) * 16 + k1) * 8 + k] || j > 0 && aboolean[((j - 1) * 16 + k1) * 8 + k] || k1 < 15 && aboolean[(j * 16 + k1 + 1) * 8 + k] || k1 > 0 && aboolean[(j * 16 + (k1 - 1)) * 8 + k] || k < 7 && aboolean[(j * 16 + k1) * 8 + k + 1] || k > 0 && aboolean[(j * 16 + k1) * 8 + (k - 1)]);

                        if (flag)
                        {
                            Material material = worldIn.getBlockState(p_180709_3_.add(j, k, k1)).getBlock().getMaterial();

                            if (k >= 4 && material.isLiquid())
                            {
                                return false;
                            }

                            if (k < 4 && !material.isSolid() && worldIn.getBlockState(p_180709_3_.add(j, k, k1)).getBlock() != this.field_150556_a)
                            {
                                return false;
                            }
                        }
                    }
                }
            }

            for (j = 0; j < 16; ++j)
            {
                for (k1 = 0; k1 < 16; ++k1)
                {
                    for (k = 0; k < 8; ++k)
                    {
                        if (aboolean[(j * 16 + k1) * 8 + k])
                        {
                            worldIn.setBlockState(p_180709_3_.add(j, k, k1), k >= 4 ? Blocks.air.getDefaultState() : this.field_150556_a.getDefaultState(), 2);
                        }
                    }
                }
            }

            for (j = 0; j < 16; ++j)
            {
                for (k1 = 0; k1 < 16; ++k1)
                {
                    for (k = 4; k < 8; ++k)
                    {
                        if (aboolean[(j * 16 + k1) * 8 + k])
                        {
                            BlockPos blockpos1 = p_180709_3_.add(j, k - 1, k1);

                            if (worldIn.getBlockState(blockpos1).getBlock() == Blocks.dirt && worldIn.getLightFor(EnumSkyBlock.SKY, p_180709_3_.add(j, k, k1)) > 0)
                            {
                                BiomeGenBase biomegenbase = worldIn.getBiomeGenForCoords(blockpos1);

                                if (biomegenbase.topBlock.getBlock() == Blocks.mycelium)
                                {
                                    worldIn.setBlockState(blockpos1, Blocks.mycelium.getDefaultState(), 2);
                                }
                                else
                                {
                                    worldIn.setBlockState(blockpos1, GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST), 2);
                                }
                            }
                        }
                    }
                }
            }
            
            if (this.field_150556_a == GenesisBlocks.komatiitic_lava)
            {
                for (j = 0; j < 16; ++j)
                {
                    for (k1 = 0; k1 < 16; ++k1)
                    {
                        for (k = 0; k < 8; ++k)
                        {
                            flag = !aboolean[(j * 16 + k1) * 8 + k] && (j < 15 && aboolean[((j + 1) * 16 + k1) * 8 + k] || j > 0 && aboolean[((j - 1) * 16 + k1) * 8 + k] || k1 < 15 && aboolean[(j * 16 + k1 + 1) * 8 + k] || k1 > 0 && aboolean[(j * 16 + (k1 - 1)) * 8 + k] || k < 7 && aboolean[(j * 16 + k1) * 8 + k + 1] || k > 0 && aboolean[(j * 16 + k1) * 8 + (k - 1)]);

                            if (flag && (k < 4 || p_180709_2_.nextInt(2) != 0) && worldIn.getBlockState(p_180709_3_.add(j, k, k1)).getBlock().getMaterial().isSolid())
                            {
                                worldIn.setBlockState(p_180709_3_.add(j, k, k1), GenesisBlocks.komatiite.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            if (this.field_150556_a.getMaterial() == Material.water)
            {
                for (j = 0; j < 16; ++j)
                {
                    for (k1 = 0; k1 < 16; ++k1)
                    {
                        byte b0 = 4;

                        if (worldIn.canBlockFreezeNoWater(p_180709_3_.add(j, b0, k1)))
                        {
                            worldIn.setBlockState(p_180709_3_.add(j, b0, k1), Blocks.ice.getDefaultState(), 2);
                        }
                    }
                }
            }

            return true;
        }
    }
}
