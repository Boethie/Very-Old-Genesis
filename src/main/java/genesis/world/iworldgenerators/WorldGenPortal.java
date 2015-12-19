package genesis.world.iworldgenerators;

import com.google.common.collect.Lists;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisConfig;
import genesis.portal.GenesisPortal;
import genesis.world.iworldgenerators.WorldGenHelper.RandomStates;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenTaiga;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.Random;

import static genesis.world.iworldgenerators.WorldGenHelper.findSurface;
import static genesis.world.iworldgenerators.WorldGenHelper.isGround;

/**
 * Created by Vorquel on 11/5/15
 */
public class WorldGenPortal implements IWorldGenerator
{
	private static RandomStates genesisRocks = new RandomStates(
			new WorldGenHelper.RandomState(GenesisBlocks.granite.getDefaultState(), 3),
			new WorldGenHelper.RandomState(GenesisBlocks.mossy_granite.getDefaultState(), 1));
	private static RandomStates vanillaRocks = new RandomStates(
			new WorldGenHelper.RandomState(Blocks.cobblestone.getDefaultState(), 3),
			new WorldGenHelper.RandomState(Blocks.mossy_cobblestone.getDefaultState(), 1));
	
	private static ArrayList<BlockPos> posts = Lists.newArrayList(
			new BlockPos(-1, 0, -8), new BlockPos( 1, 0, -8), new BlockPos(-4, 0, -7), new BlockPos( 4, 0, -7), new BlockPos(0, -6, -6),
			new BlockPos(-1, 0,  8), new BlockPos( 1, 0,  8), new BlockPos(-4, 0,  7), new BlockPos( 4, 0,  7), new BlockPos(0, -6,  6),
			new BlockPos(-8, 0, -1), new BlockPos(-8, 0,  1), new BlockPos(-7, 0, -4), new BlockPos(-7, 0,  4), new BlockPos(0,  6, -6),
			new BlockPos( 8, 0, -1), new BlockPos( 8, 0,  1), new BlockPos( 7, 0, -4), new BlockPos( 7, 0,  4), new BlockPos(0,  6,  6));
	
	private static ArrayList<BlockPos> buds = Lists.newArrayList(
			new BlockPos(-4, 0, -4), new BlockPos(4, 0, -4), new BlockPos(-4, 0, 4), new BlockPos(4, 0, 4));
	
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
	
	protected void genPortal(Random random, int chunkX, int chunkZ, World world, int bits, boolean tiagaOnly)
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
			newPortal.makePortal(world, random, false);
		}
	}
	
	public static void genStructure(World world, BlockPos center, boolean isNew)
	{
		RandomStates rocks = isNew ? genesisRocks : vanillaRocks;
		
		for (BlockPos post : posts)
		{
			BlockPos ground = findSurface(world, center.add(post));
			int height = ground.getY() + 3 - (isNew ? 0 : world.rand.nextInt(2));
			while (ground.getY() < height)
			{
				world.setBlockState(ground, rocks.getState(world.rand));
				ground = ground.up();
			}
		}
		if (isNew)
		{
			for (BlockPos bud : buds)
			{
				world.setBlockState(findSurface(world, center.add(bud)), rocks.getState(world.rand));
			}
		}
	}
}
