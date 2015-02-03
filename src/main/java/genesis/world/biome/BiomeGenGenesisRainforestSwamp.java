package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.GenesisWorldHelper;
import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenGenesisRainforestSwamp extends BiomeGenGenesisBase {
	public BiomeGenGenesisRainforestSwamp(int par1) {
		super(par1);
		this.theBiomeDecorator.treesPerChunk = 7;
		this.theBiomeDecorator.dirtPerChunk = 15;
	}

	@Override
	public WorldGenAbstractTree genBigTreeChance(Random rand) {
		int treeType = rand.nextInt(20);
		return treeType < 6 ? new WorldGenTreeLepidodendron(7, 5, true) : treeType < 13 ? new WorldGenTreeSigillaria(7, 3, true) : treeType < 18 ? new WorldGenTreeCordaites(12, 5, true) : new WorldGenTreePsaronius(5, 3, true);
	}

	public void decorate(World world, Random rand, BlockPos pos) {
		int posX, posY, posZ;
		BlockPos thisPos;

		if(rand.nextInt(28) == 0){
			GenesisWorldHelper.generateMossyGraniteBoulder(world, rand, pos);
		}

		super.decorate(world, rand, pos);

		for (int a = 0; a < 30; a++) {
			posX = pos.getX() + rand.nextInt(16);
			posZ = pos.getZ() + rand.nextInt(16);
			posY = GenesisWorldHelper.getTopBlockOfType(world, posX, posZ, GenesisBlocks.moss, Blocks.dirt);
			thisPos = new BlockPos(posX, posY, posZ);
			if (world.isAirBlock(thisPos)) {
				//TODO: Get this working.
				//world.setBlock(varX, varY, varZ, GenesisBlocks.asteroxylon, 0, 2);
			}
		}

		if (rand.nextInt(5) == 0) {
			for (int a = 0; a < 8; a++) {
				posX = pos.getX() + rand.nextInt(16);
				posZ = pos.getZ() + rand.nextInt(16);
				posY = GenesisWorldHelper.getTopBlockOfType(world, posX, posZ, GenesisBlocks.moss, Blocks.dirt);
				thisPos = new BlockPos(posX, posY, posZ);
				if (world.isAirBlock(thisPos)) {
					if (world.isAirBlock(thisPos.add(0, 1, 0))) {
						int stage = rand.nextInt(8);
						//TODO: Get the plants working when they are added back in.
						//world.setBlockState(thisPos, GenesisBlocks.sphenophyllum.getDefaultState(), stage, 2);
						//if(stage >= 5) world.setBlock(varX, varY + 1, varZ, GenesisPlantBlocks.sphenophyllum_top, stage - 5, 2);
					} else {
						//world.setBlock(varX, varY, varZ, GenesisPlantBlocks.sphenophyllum, rand.nextInt(5), 2);
					}
				}
			}
		}

		for (int a = 0; a < 30; a++) {
			posX = pos.getX() + rand.nextInt(16);
			posZ = pos.getZ() + rand.nextInt(16);
			posY = GenesisWorldHelper.getTopBlockOfType(world, posX, posZ, GenesisBlocks.moss, Blocks.dirt);
			thisPos = new BlockPos(posX, posY, posZ);
			if (world.isAirBlock(thisPos)) {
				world.setBlockState(thisPos, GenesisBlocks.fern.getDefaultState(), 2);
			}
		}
	}
}
