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
		
		topBlock = GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		
		addDecorations();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenPebbles(), 20);
		
		getDecorator().setFlowerCount(3.5F);
		addFlower(WorldGenPlant.create(EnumPlant.LEPACYCLOTES).setNextToWater(true).setPatchCount(4), 1);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
