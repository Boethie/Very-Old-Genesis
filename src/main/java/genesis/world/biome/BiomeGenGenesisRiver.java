package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.GenesisWorldHelper;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenGenesisRiver extends BiomeGenGenesisBase 
{
	public BiomeGenGenesisRiver(int biomeID) 
	{
		super(biomeID);
		this.topBlock = GenesisBlocks.limestone.getDefaultState();
		this.fillerBlock = GenesisBlocks.limestone.getDefaultState();
	}

	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int p_180622_4_, int p_180622_5_, double p_180622_6_) 
	{
		/*this.topBlock = ModBlocks.limestone;
        this.fillerBlock = ModBlocks.limestone;

        if(p_150573_7_ > 1.45D)
        {
            this.topBlock = ModBlocks.shale;
            this.fillerBlock = ModBlocks.shale;
        }*/

		this.generateBiomeTerrain(world, rand, primer, p_180622_4_, p_180622_5_, p_180622_6_);
	}

	public void decorate(World world, Random rand, BlockPos pos) 
	{
		int posX, posY, posZ;
		BlockPos thisPos;

		super.decorate(world, rand, pos);

		for (int a = 0; a < 5; a++) 
		{
			posX = pos.getX() + rand.nextInt(16);
			posZ = pos.getZ() + rand.nextInt(16);
			posY = GenesisWorldHelper.getTopBlockOfType(world, posX, posZ, GenesisBlocks.moss, Blocks.dirt);
			thisPos = new BlockPos(posX, posY, posZ);
			//TODO: Get algae working:
				//            if(GenesisBlocks.algae.canBlockStay(world, varX, varY, varZ)) { 
			//            	world.setBlock(varX, varY, varZ, GenesisAquaticBlocks.algae, 1, 2);
			//            }
		}
	}
}
