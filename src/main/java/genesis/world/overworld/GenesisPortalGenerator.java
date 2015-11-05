package genesis.world.overworld;

import genesis.block.tileentity.portal.GenesisPortal;
import genesis.common.GenesisConfig;
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
		BlockPos pos = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);
		if (random.nextInt(800) == 0)
			if (world.provider.getDimensionId() == 0 && world.getBiomeGenForCoords(pos) instanceof BiomeGenTaiga
					|| world.provider.getDimensionId() == GenesisConfig.genesisDimId)
			{
				GenesisPortal newPortal = GenesisPortal.fromCenterBlock(world, pos);
				newPortal.setPlacementPosition(world);
				newPortal.makePortal(world, random);
			}
	}
}
