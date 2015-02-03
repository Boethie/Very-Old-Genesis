package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.gen.BiomeDecoratorGenesis;
import genesis.world.gen.feature.WorldGenMinableSurface;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeGenGenesisBase extends BiomeGenBase {
	BiomeDecoratorGenesis theBiomeDecorator;
	public BiomeGenGenesisBase edgeVersion = null;
	public BiomeGenGenesisBase mutatedVersion = null;
	public BiomeGenGenesisBase hillVersion = null;

	public BiomeGenGenesisBase(int par1) {
		super(par1);
		this.topBlock = GenesisBlocks.moss.getDefaultState();
		this.fillerBlock = Blocks.dirt.getDefaultState();
		this.spawnableCaveCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.theBiomeDecorator = new BiomeDecoratorGenesis();
		this.theBiomeDecorator.dirtPerChunk = 10;
	}

	public void decorate(World world, Random rand, BlockPos blockPos){
		this.theBiomeDecorator.decorate(world, rand, this, blockPos);

		for(int c = this.theBiomeDecorator.dirtPerChunk; c > 0; c--)
		{
			BlockPos thisPos = blockPos.add(rand.nextInt(16) + 8, world.getTopSolidOrLiquidBlock(blockPos).getY(), rand.nextInt(16) + 8);
			(new WorldGenMinableSurface(Blocks.dirt, 0, 20, GenesisBlocks.moss)).generate(world, rand, blockPos);
		}
	}

	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int p_180622_4_, int p_180622_5_, double p_180622_6_)
	{
		this.generateBiomeTerrain(world, rand, primer, p_180622_4_, p_180622_5_, p_180622_6_);
	}

	/**
	 * takes temperature, returns color
	 */
	 @SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float par1) {
		return 0x317A00;
	 }

	 public int getWaterColorMultiplier() {
		 return 0xA36F1C;
	 }
}
