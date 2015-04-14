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
	private Block clayBlock;
    private int numberOfBlocks;
    private static final String __OBFID = "CL_00000405";

    public WorldGenClayGenesis(int p_i2011_1_)
    {
        this.clayBlock = GenesisBlocks.red_clay;
        this.numberOfBlocks = p_i2011_1_;
    }

    public boolean generate(World worldIn, Random random, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock().getMaterial() != Material.water)
        {
            return false;
        }
        else
        {
            int i = random.nextInt(this.numberOfBlocks - 2) + 2;
            byte b0 = 1;

            for (int j = pos.getX() - i; j <= pos.getX() + i; ++j)
            {
                for (int k = pos.getZ() - i; k <= pos.getZ() + i; ++k)
                {
                    int l = j - pos.getX();
                    int i1 = k - pos.getZ();

                    if (l * l + i1 * i1 <= i * i)
                    {
                        for (int j1 = pos.getY() - b0; j1 <= pos.getY() + b0; ++j1)
                        {
                            BlockPos blockpos1 = new BlockPos(j, j1, k);
                            Block block = worldIn.getBlockState(blockpos1).getBlock();

                            if (block == Blocks.dirt || block == GenesisBlocks.red_clay)
                            {
                                worldIn.setBlockState(blockpos1, this.clayBlock.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
