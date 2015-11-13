package genesis.world.overworld;

import genesis.common.GenesisConfig;
import genesis.portal.GenesisPortal;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenTaiga;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * Created by Vorquel on 11/5/15.
 */
public class GenesisPortalGenerator implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		int dimension = world.provider.getDimensionId();
		if (dimension == 0)
		{
			genPortal(random, chunkX, chunkZ, world, 5, true);
		}
		else if (dimension == GenesisConfig.genesisDimId)
		{
			genPortal(random, chunkX, chunkZ, world, 8, false);
		}
	}
	
	private void genPortal(Random random, int chunkX, int chunkZ, World world, int bits, boolean tiagaOnly)
	{
		BlockPos center = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);
		
		if (tiagaOnly && !(world.getBiomeGenForCoords(center) instanceof BiomeGenTaiga))
		{
			return;
		}
		
		int zoneX = chunkX >> bits;
		int zoneZ = chunkZ >> bits;
		int localX = (int) Math.floor((Math.sin(zoneX * 1017 + zoneZ * 8808) + 1) * (1 << bits - 1));
		int localZ = (int) Math.floor((Math.sin(zoneX * 4594 + zoneZ * 9628) + 1) * (1 << bits - 1));
		int nearX = (zoneX << bits) + localX;
		int nearZ = (zoneZ << bits) + localZ;
		
		if (nearX != chunkX || nearZ != chunkZ)
		{
			return;
		}
		
		GenesisPortal newPortal = GenesisPortal.fromCenterBlock(world, center);
		
		if (newPortal.setPlacementPosition(world))
		{
			newPortal.makePortal(world, random);
		}
	}
}
