package genesis.world.biome;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenPlant;
import net.minecraft.world.biome.Biome;

public class BiomeBeachGenesis extends BiomeGenesis
{
	public BiomeBeachGenesis(Biome.BiomeProperties properties)
	{
		super(properties);
		
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		
		addDecorations();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenPebbles(), 20);
		
		getDecorator().setFlowerCount(4);
		addFlower(WorldGenPlant.create(EnumPlant.LEPACYCLOTES).setNextToWater(true).setPatchCount(4), 10);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
