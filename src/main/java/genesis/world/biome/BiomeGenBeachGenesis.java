package genesis.world.biome;

import net.minecraft.util.Vec3;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenBeachGenesis extends BiomeGenBaseGenesis
{
	public BiomeGenBeachGenesis (int id)
	{
		super(id);
		setBiomeName("Beach");
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT);
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(25));
		
		addDecoration(new WorldGenRockBoulders().setRarity(85).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
	}
	
	@Override
	public float getFogDensity(int x, int y, int z)
	{
		return 1.0F;
	}
	
	@Override
	public Vec3 getFogColor()
	{
		float red = 0.533333333F;
		float green = 0.647058824F;
		float blue = 0.474509804F;
		
		return new Vec3(red, green, blue);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
