package genesis.world.biome;

import genesis.common.GenesisBiomes;
import genesis.common.GenesisBlocks;
import genesis.world.GenesisWorldHelper;
import genesis.world.gen.feature.WorldGenBoulder;
import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenGenesisRainforest extends BiomeGenGenesisBase 
{
	public BiomeGenGenesisRainforest(int biomeID) 
	{
		super(biomeID);
		this.theBiomeDecorator.treesPerChunk = 10;
		this.edgeVersion = GenesisBiomes.rainforestEdge;
	}

	@Override
	public WorldGenAbstractTree genBigTreeChance(Random rand) 
	{
		int treeType = rand.nextInt(20);
		return treeType < 14 ? new WorldGenTreeLepidodendron(10, 5, true) : treeType < 17 ? new WorldGenTreeSigillaria(8, 3, true) : treeType < 19 ? new WorldGenTreeCordaites(15, 5, true) : new WorldGenTreePsaronius(5, 4, true);
	}

	public void decorate(World world, Random rand, BlockPos pos) 
	{
		int posX, posY, posZ;
		BlockPos thisPos;

		if(rand.nextInt(16) == 0)
		{
			GenesisWorldHelper.generateMossyGraniteBoulder(world, rand, pos);
		}

		super.decorate(world, rand, pos);

		if (rand.nextInt(20) == 0) 
		{
			for (int a = 0; a < 5; a++) 
			{
				posX = pos.getX() + rand.nextInt(16);
				posZ = pos.getZ() + rand.nextInt(16);
				posY = GenesisWorldHelper.getTopBlockOfType(world, posX, posZ, GenesisBlocks.moss, Blocks.dirt);
				thisPos = new BlockPos(posX, posY, posZ);
				if (world.isAirBlock(thisPos)) 
				{
					if (world.isAirBlock(thisPos.add(0, 1, 0))) 
					{
						//TODO: Get the plants working when they are added back in.
						//world.setBlock(varX, varY, varZ, GenesisBlocks.zingiberopsis, 7, 2);
						//world.setBlock(varX, varY + 1, varZ, GenesisBlocks.zingiberopsis_top, 2, 2);
					} 
					else 
					{
						//world.setBlock(varX, varY, varZ, GenesisBlocks.zingiberopsis, 0, 2);
					}
				}
			}
		}

		if (rand.nextInt(10) == 0) 
		{
			for (int a = 0; a < 5; a++) 
			{
				posX = pos.getX() + rand.nextInt(16);
				posZ = pos.getZ() + rand.nextInt(16);
				posY = GenesisWorldHelper.getTopBlockOfType(world, posX, posZ, GenesisBlocks.moss, Blocks.dirt);
				thisPos = new BlockPos(posX, posY, posZ);
				if (world.isAirBlock(thisPos)) 
				{
					if (world.isAirBlock(thisPos.add(0, 1, 0))) 
					{
						int stage = rand.nextInt(8);
						//TODO: Get the plants working when they are added back in.
						//world.setBlock(varX, varY, varZ, GenesisBlocks.sphenophyllum, stage, 2);
						//if(stage >= 5) world.setBlock(varX, varY + 1, varZ, GenesisBlocks.sphenophyllum_top, stage - 5, 2);
					} 
					else 
					{
						//  world.setBlock(varX, varY, varZ, GenesisBlocks.sphenophyllum, rand.nextInt(5), 2);
					}
				}
			}
		}

		posX = pos.getX() + rand.nextInt(16) + 8;
		posZ = pos.getZ() + rand.nextInt(16) + 8;
		posY = GenesisWorldHelper.getBlockHeight(world, posX, posZ);
		thisPos = new BlockPos(posX, posY, posZ);
		new WorldGenBoulder(GenesisBlocks.mossy_granite, 0).generate(world, rand, thisPos);

		for (int a = 0; a < 75; a++) 
		{
			posX = pos.getX() + rand.nextInt(16);
			posZ = pos.getZ() + rand.nextInt(16);
			posY = GenesisWorldHelper.getTopBlockOfType(world, posX, posZ, GenesisBlocks.moss, Blocks.dirt);
			thisPos = new BlockPos(posX, posY, posZ);
			if (world.isAirBlock(thisPos)) 
			{
				world.setBlockState(thisPos, GenesisBlocks.fern.getDefaultState(), 2);
			}
		}

//        posX = pos.getX() + rand.nextInt(16);
//        posZ = pos.getZ() + rand.nextInt(16);
//        posY = GenesisWorldHelper.getTopBlockOfType(world, posX, posZ, GenesisBlocks.moss, Blocks.dirt);
//        thisPos = new BlockPos(posX, posY, posZ);
//        if(world.isAirBlock(thisPos)) {
//            world.setBlockState(thisPos, Blocks.leaves.getDefaultState(), 2);
//        }
	}
}
