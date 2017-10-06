package genesis.world.biome;

import genesis.combo.variant.EnumTree;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

public class BiomeSwampRainforestM extends BiomeSwampRainforest
{
	public BiomeSwampRainforestM(BiomeProperties properties)
	{
		super(properties);
		
		super.addTrees();
	}
	
	@Override
	protected void addTrees()
	{
		getDecorator().setTreeCount(13);
		
		addTree(new WorldGenTreeCordaites(12, 17, true).generateVine(6), 16);
		addTree(new WorldGenTreeLepidodendron(17, 20, true).generateVine(5), 17);
		addTree(new WorldGenTreeSigillaria(10, 14, true).generateVine(5), 2);
		
		addTree(new WorldGenDeadLog(5, 8, EnumTree.LEPIDODENDRON, true), 4);
		addTree(new WorldGenDeadLog(4, 7, EnumTree.SIGILLARIA, true), 1);
		addTree(new WorldGenDeadLog(5, 8, EnumTree.CORDAITES, true).setCanGrowInWater(true), 5);
	}
}
