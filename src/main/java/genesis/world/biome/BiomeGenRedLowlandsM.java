package genesis.world.biome;

import genesis.combo.PlantBlocks;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenTreeVoltzia;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenRedLowlandsM extends BiomeGenRedLowlands
{
	public BiomeGenRedLowlandsM(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		//setBiomeName("Red Lowlands M");
		//setHeight(2.2F, 0.4F);
	}
	
	@Override
	protected void addDecorations()
	{
		getDecorator().setFlowerCount(2);
		addFlower(WorldGenPlant.create(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.AETHOPHYLLUM), 1);
		addFlower(WorldGenPlant.create(EnumPlant.APOLDIA), 1);
		
		addDecoration(
				new WorldGenRockBoulders(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT))
						.setWaterRequired(false)
						.setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)),
				0.333F);
	}
	
	@Override
	protected void addTrees()
	{
		getDecorator().setTreeCount(10);
		addTree(new WorldGenTreeVoltzia(5, 10, true), 1);
	}
}
