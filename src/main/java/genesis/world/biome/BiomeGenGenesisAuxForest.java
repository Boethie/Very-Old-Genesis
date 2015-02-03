package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.GenesisWorldHelper;
import genesis.world.gen.feature.WorldGenBoulder;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeGenGenesisAuxForest extends BiomeGenGenesisBase {

	public BiomeGenGenesisAuxForest(int par1) {
		super(par1);
		this.theBiomeDecorator.treesPerChunk = 5;
	}

	@Override
	public WorldGenAbstractTree genBigTreeChance(Random rand) {
		return new WorldGenTreeAraucarioxylon(10, 15, true);
	}

	@Override
	public void decorate(World world, Random rand, BlockPos blockPos) {
		int posX, posY, posZ;
		BlockPos thisPos;

		if(rand.nextInt(10) == 0){
			GenesisWorldHelper.generateMossyGraniteBoulder(world, rand, blockPos);
		}

		super.decorate(world, rand, blockPos);
	}

	/**
	 * takes temperature, returns color
	 */
	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float par1) {
		return 0x61a3e7;
	}
}