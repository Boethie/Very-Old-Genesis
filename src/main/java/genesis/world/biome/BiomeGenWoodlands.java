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
		getDecorator().setGrassCount(12);
		addGrass(WorldGenPlant.create(EnumPlant.ONOCLEA).setPatchCount(9), 1);
		
		getDecorator().setFlowerCount(1);
		addFlower(WorldGenPlant.create(EnumPlant.PALAEOASTER).setRarity(4).setPatchCount(48), 1);	// TODO: Patch count 48?!?!
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.zingiberopsis).setRarity(4).setPatchCount(3), 1);
		addDecoration(WorldGenCircleReplacement.getPeatGen(), 1);
		addDecoration(new WorldGenDebris(), 20);
		addDecoration(new WorldGenDebris(EnumDebrisOther.TYRANNOSAURUS_FEATHER).setPatchCount(1, 2), 0.1F);
		addDecoration(new WorldGenRockBoulders().setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 2);
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.5F);
		/*addDecoration(
				new WorldGenRockBoulders(
						GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.CARNIVORE))
								.setInGround(false)
								.setWaterRequired(false)
								.setRadius(2),
						0.085F);*/
		addDecoration(new WorldGenRoots(), 26);
		addDecoration(new WorldGenDecorationOnBlock((s) -> s.getMaterial() == Material.water, GenesisBlocks.cobbania.getDefaultState()), 10);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(12.8F);
		
		addTree(new WorldGenTreeLaurophyllum(3, 4, false), 2000);
		addTree(new WorldGenTreeFicus(4, 8, false), 125);
		addTree(new WorldGenTreeGinkgo(8, 13, false), 166);
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2), 45);
		addTree(new WorldGenTreeDryophyllum(11, 15, false), 6000);
		addTree(new WorldGenTreeDryophyllum(13, 17, false).setType(TreeTypes.TYPE_2), 100);
		addTree(new WorldGenTreeMetasequoia(12, 24, true), 2000);
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.DRYOPHYLLUM, true), 2000);
		addTree(new WorldGenDeadLog(3, 6, EnumTree.METASEQUOIA, true), 250);
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
