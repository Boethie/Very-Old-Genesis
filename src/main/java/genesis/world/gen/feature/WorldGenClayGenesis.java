package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenClayGenesis extends WorldGenerator
{
	private Block field_150546_a;
    private int numberOfBlocks;
    private static final String __OBFID = "CL_00000405";

    public WorldGenClayGenesis(int p_i2011_1_)
    {
        this.field_150546_a = GenesisBlocks.red_clay;
        this.numberOfBlocks = p_i2011_1_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (worldIn.getBlockState(p_180709_3_).getBlock().getMaterial() != Material.water)
        {
            return false;
        }
        else
        {
            int i = p_180709_2_.nextInt(this.numberOfBlocks - 2) + 2;
            byte b0 = 1;

            for (int j = p_180709_3_.getX() - i; j <= p_180709_3_.getX() + i; ++j)
            {
                for (int k = p_180709_3_.getZ() - i; k <= p_180709_3_.getZ() + i; ++k)
                {
                    int l = j - p_180709_3_.getX();
                    int i1 = k - p_180709_3_.getZ();

                    if (l * l + i1 * i1 <= i * i)
                    {
                        for (int j1 = p_180709_3_.getY() - b0; j1 <= p_180709_3_.getY() + b0; ++j1)
                        {
                            BlockPos blockpos1 = new BlockPos(j, j1, k);
                            Block block = worldIn.getBlockState(blockpos1).getBlock();

                            if (block == Blocks.dirt || block == GenesisBlocks.red_clay)
                            {
                                worldIn.setBlockState(blockpos1, this.field_150546_a.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
