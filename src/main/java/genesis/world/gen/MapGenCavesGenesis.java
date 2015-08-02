package genesis.world.gen;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenCaves;

import com.google.common.base.Objects;

public class MapGenCavesGenesis extends MapGenCaves
{
	@Override
    protected boolean func_175793_a(IBlockState p_175793_1_, IBlockState p_175793_2_)
    {
        return p_175793_1_.getBlock() == Blocks.stone ? true : (p_175793_1_.getBlock() == Blocks.dirt ? true : (p_175793_1_.getBlock() == Blocks.grass ? true : (p_175793_1_.getBlock() == Blocks.hardened_clay ? true : (p_175793_1_.getBlock() == Blocks.stained_hardened_clay ? true : (p_175793_1_.getBlock() == Blocks.sandstone ? true : (p_175793_1_.getBlock() == Blocks.red_sandstone ? true : (p_175793_1_.getBlock() == Blocks.mycelium ? true : (p_175793_1_.getBlock() == Blocks.snow_layer ? true : (p_175793_1_.getBlock() == Blocks.sand || p_175793_1_.getBlock() == Blocks.gravel) && p_175793_2_.getBlock().getMaterial() != Material.water))))))));
    }
	
	@Override
    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop, IBlockState state, IBlockState up)
    {
        net.minecraft.world.biome.BiomeGenBase biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        IBlockState top = biome.topBlock;
        IBlockState filler = biome.fillerBlock;

        if (func_175793_a(state, up) || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock())
        {
            if (y < 10)
            {
                data.setBlockState(x, y, z, GenesisBlocks.komatiitic_lava.getDefaultState());
            }
            else
            {
                data.setBlockState(x, y, z, Blocks.air.getDefaultState());

                if (up.getBlock() == Blocks.sand)
                {
                    data.setBlockState(x, y + 1, z, up.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState());
                }

                if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler.getBlock())
                {
                    data.setBlockState(x, y - 1, z, top.getBlock().getDefaultState());
                }
            }
        }
    }
}
