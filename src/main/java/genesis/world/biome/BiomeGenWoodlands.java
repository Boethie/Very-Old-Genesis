package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.*;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.*;
import genesis.world.gen.feature.*;
import genesis.world.gen.feature.WorldGenTreeBase.TreeTypes;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenWoodlands extends BiomeGenBaseGenesis
{
	public BiomeGenWoodlands(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		getDecorator().setGrassCount(12);
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		getDecorator().setGrassCount(7);
		addGrass(WorldGenPlant.create(EnumPlant.ONOCLEA).setPatchCount(9), 1);
		
		getDecorator().setFlowerCount(0.15F);
		addFlower(WorldGenPlant.create(EnumPlant.PALAEOASTER).setPatchCount(6), 1);
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.zingiberopsis).setPatchCount(3), 0.15F);
		addDecoration(WorldGenCircleReplacement.getPeatGen(), 1);
		addDecoration(new WorldGenDebris(), 20);
		addDecoration(new WorldGenDebris(EnumDebrisOther.TYRANNOSAURUS_FEATHER).setPatchCount(1, 2), 0.1F);
		addDecoration(new WorldGenRockBoulders().setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 2);
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.333F);
		/*addDecoration(
				new WorldGenRockBoulders(
						GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.CARNIVORE))
								.setInGround(false)
								.setWaterRequired(false)
								.setRadius(2),
						0.085F);*/
		addDecoration(new WorldGenRoots(), 13);
		addDecoration(new WorldGenDecorationOnBlock((s) -> s.getMaterial() == Material.water, GenesisBlocks.cobbania.getDefaultState()), 2.8F);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(8.1F);
		
		addTree(new WorldGenTreeLaurophyllum(3, 4, false), 1500);
		addTree(new WorldGenTreeFicus(4, 8, false), 125);
		addTree(new WorldGenTreeGinkgo(8, 13, false), 166);
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2), 45);
		addTree(new WorldGenTreeDryophyllum(11, 15, false), 6000);
		addTree(new WorldGenTreeDryophyllum(13, 17, false).setType(TreeTypes.TYPE_2), 100);
		addTree(new WorldGenTreeMetasequoia(12, 24, true), 1000);
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.DRYOPHYLLUM, true), 1200);
		addTree(new WorldGenDeadLog(3, 6, EnumTree.METASEQUOIA, true), 200);
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
