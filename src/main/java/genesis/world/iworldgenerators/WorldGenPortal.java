package genesis.world.iworldgenerators;

import genesis.common.GenesisDimensions;
import genesis.portal.GenesisPortal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenPortal implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if (GenesisDimensions.isGenesis(world))
		{
			genPortal(random, chunkX, chunkZ, world, 8, false);
		}
		else
		{
			genPortal(random, chunkX, chunkZ, world, 5, true);
		}
	}
	
	protected void genPortal(Random random, int chunkX, int chunkZ, World world, int bits, boolean tiagaOnly)
	{
		BlockPos center = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);
		
		if (tiagaOnly && !(world.getBiome(center) instanceof BiomeTaiga))
		{
			return;
		}
		
		int zoneX = chunkX >> bits;
		int zoneZ = chunkZ >> bits;
		int localX = (int) ((Math.sin(zoneX * 1017 + zoneZ * 8808) + 1) * (1 << bits - 1));
		int localZ = (int) ((Math.sin(zoneX * 4594 + zoneZ * 9628) + 1) * (1 << bits - 1));
		int nearX = (zoneX << bits) + localX;
		int nearZ = (zoneZ << bits) + localZ;
		
		if (nearX != chunkX || nearZ != chunkZ)
		{
			return;
		}
		
		GenesisPortal newPortal = GenesisPortal.fromCenterBlock(world, center);
		
		if (newPortal.setPlacementPosition(world))
		{
			newPortal.makePortal(world, random, false);
		}
	}
}
