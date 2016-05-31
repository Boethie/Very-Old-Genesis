package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.*;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.*;
import genesis.world.gen.feature.*;
import genesis.world.gen.feature.WorldGenTreeBase.TreeTypes;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenMetaForest extends BiomeGenBaseGenesis
{
	public BiomeGenMetaForest(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		//TODO: WorldGenPaleogaracites should be instead called/moved inside the dead log generation.
		addDecoration(new WorldGenPalaeoagaracites().setPatchCount(24), 14);
		addDecoration(new WorldGenStemonitis().setPatchCount(18), 11);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPatchCount(5), 1.75F);
		
		addDecoration(new WorldGenRockBoulders().setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 2);
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.142F);
		/*addDecoration(
				new WorldGenRockBoulders(GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.HERBIVORE))
						.setInGround(false)
						.setWaterRequired(false)
						.setRadius(2),
				0.0714F);*/
		addDecoration(new WorldGenDebris(), 7);
		addDecoration(new WorldGenRoots(), 13);
		
		getDecorator().setGrassCount(5);
		addGrass(WorldGenPlant.create(EnumPlant.ASTRALOPTERIS).setPatchCount(9), 3);
		addGrass(WorldGenPlant.create(EnumPlant.MATONIDIUM).setPatchCount(9), 1);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(5.7F);
		
		addTree(new WorldGenTreeArchaeanthus(7, 20, false), 15);
		addTree(new WorldGenTreeMetasequoia(12, 24, true), 300);
		addTree(new WorldGenTreeMetasequoia(23, 27, true).setType(TreeTypes.TYPE_2), 120);
		addTree(new WorldGenTreeGinkgo(8, 13, false), 5);
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2), 1);
		
		addTree(new WorldGenDeadLog(4, 8, EnumTree.METASEQUOIA, true), 60);
		addTree(new WorldGenDeadLog(4, 8, EnumTree.METASEQUOIA, true).setType(1), 15);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.genTerrainBlocks(world, rand, primer, blockX, blockZ, d);
	}
}
