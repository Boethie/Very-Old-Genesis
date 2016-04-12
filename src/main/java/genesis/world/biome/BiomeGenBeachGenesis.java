package genesis.world.biome;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenPlant;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenBeachGenesis extends BiomeGenBaseGenesis
{
	public BiomeGenBeachGenesis(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		
		addDecorations();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenPebbles(), 25);
		
		getDecorator().setFlowerCount(10);
		addFlower(WorldGenPlant.create(EnumPlant.LEPACYCLOTES).setNextToWater(true).setPatchCount(4), 10);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
