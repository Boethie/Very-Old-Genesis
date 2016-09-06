package genesis.world.iworldgenerators;

import genesis.common.GenesisBlocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.*;

import static genesis.world.WorldGenerators.menhirActivatorsOverworld;

public class WorldGenMenhirActivators implements IWorldGenerator
{
	private final List<MenhirStructure> structures;
	private final Set<Block> topBlocks = ImmutableSet.of(Blocks.DIRT, Blocks.GRASS, Blocks.STONE);
	private final Set<Block> bottomBlocks = ImmutableSet.of(Blocks.STONE);

	public WorldGenMenhirActivators()
	{
		ImmutableList.Builder<MenhirStructure> builder = ImmutableList.builder();

		MenhirStructure hut = new MenhirStructure();

		List<BlockPos> rubble = new ArrayList<>();
		for (int x = -7; x <= 7; ++x)
		{
			for (int z = -7; z <= 7; ++ z)
			{
				if (x * x + z * z < 64)
					rubble.add(new BlockPos(x, 0, z));
			}
		}
		hut.add(rubble, StructureType.RUBBLE, 0.25);

		List<BlockPos> air = new ArrayList<>();
		List<BlockPos> gestalt = new ArrayList<>();
		for (int y = -1; y <= 3; ++y)
		{
			for (int x = -3; x <= 3; ++x)
			{
				for (int z = -3; z <= 3; ++z)
				{
					int d2 = x * x + y * y + z * z;
					if (d2 < 8 || x == 3 && y <= 0 && z == 0)
						air.add(new BlockPos(x, y + 2, z));
					else if (d2 < 12)
						gestalt.add(new BlockPos(x, y + 2, z));
				}
			}
		}
		hut.add(air, StructureType.AIR, 1d);
		hut.add(gestalt, StructureType.GESTALT, 0.9);

		builder.add(hut);

		MenhirStructure tumulus = new MenhirStructure();

		rubble = new ArrayList<>();
		for (int x = -5; x <= 5; ++x)
		{
			for (int z = -5; z <= 5; ++ z)
			{
				int d2 = x * x + z * z;
				if (d2 < 27 && d2 > 18)
					rubble.add(new BlockPos(x, 1, z));
			}
		}
		tumulus.add(rubble, StructureType.RUBBLE, 0.5);

		rubble = new ArrayList<>();
		for (int x = -5; x <= 5; ++x)
		{
			for (int z = -5; z <= 5; ++ z)
			{
				if (x * x + z * z < 27)
					rubble.add(new BlockPos(x, 0, z));
			}
		}
		tumulus.add(rubble, StructureType.RUBBLE, 0.25);

		gestalt = new ArrayList<>();
		gestalt.add(new BlockPos( 0, 1,  0));
		gestalt.add(new BlockPos(-1, 1,  0));
		gestalt.add(new BlockPos( 1, 1,  0));
		gestalt.add(new BlockPos( 0, 1, -1));
		gestalt.add(new BlockPos( 0, 1,  1));
		gestalt.add(new BlockPos( 0, 2,  0));
		tumulus.add(gestalt, StructureType.GESTALT, 1);

		gestalt = new ArrayList<>();
		gestalt.add(new BlockPos(-1, 1, -1));
		gestalt.add(new BlockPos(-1, 1,  1));
		gestalt.add(new BlockPos( 1, 1, -1));
		gestalt.add(new BlockPos( 1, 1,  1));
		tumulus.add(gestalt, StructureType.GESTALT, 0.25);

		builder.add(tumulus);

		structures = builder.build();
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		BlockPos chunkPos = new BlockPos(chunkX << 4, 0, chunkZ << 4);

		int menhirHutChance = 300;
		if (random.nextInt(menhirHutChance) == 0 && world.getBiome(chunkPos.add(8, 0, 8)) instanceof BiomeTaiga)
			generateHut(random, world, chunkPos);
	}

	public void generateHut(Random random, World world, BlockPos chunkPos)
	{
		MenhirStructure structure = structures.get(random.nextInt(structures.size()));
		BlockPos center = getValidPos(structure, random, world, chunkPos);
		if (center == null)
			return;

		for (BlockPos offset : structure.get(random, StructureType.RUBBLE))
		{
			BlockPos pos = getRealHeight(world, center.add(offset), topBlocks).up(offset.getY());
			if (random.nextInt(4) == 0)
				world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
			else
				world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
		}

		for (BlockPos offset : structure.get(random, StructureType.GESTALT))
		{
			BlockPos pos = center.add(offset);
			if (random.nextInt(4) == 0)
				world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
			else
				world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
		}

		for (BlockPos offset : structure.get(random, StructureType.AIR))
			world.setBlockToAir(center.add(offset));

		ArrayList<ItemStack> loot = menhirActivatorsOverworld.stream().map(ItemStack::copy).collect(Collectors.toCollection(ArrayList::new));

		int count = 2 + random.nextInt(2);
		while (loot.size() > count)
			loot.remove(random.nextInt(loot.size()));

		BlockPos chestPos = center.add(0, -1, 0);
		//noinspection ToArrayCallWithZeroLengthArrayArgument
		GenesisBlocks.rotten_storage_box.placeWithItems(world, chestPos, loot.toArray(new ItemStack[0]));
	}

	protected BlockPos getValidPos(MenhirStructure structure, Random random, World world, BlockPos chunkPos)
	{
		chunkPos = new BlockPos(chunkPos.getX(), 0, chunkPos.getZ());
		BlockPos candidate;
		for (int i=0; i<20; ++i)
		{
			candidate = chunkPos.add(random.nextInt(16), 0, random.nextInt(16));
			int min = 255;
			int max = 0;
			for (BlockPos offset : structure.getFoundation())
			{
				int lo = getRealHeight(world, candidate.add(offset), topBlocks).getY();
				min = lo < min ? lo : min;
				int hi = getRealHeight(world, candidate.add(offset), bottomBlocks).getY();
				max = hi > max ? hi : max;
			}
			candidate = candidate.add(0, min, 0);
			if (max <= min)
				return candidate;
		}
		return null;
	}

	protected BlockPos getRealHeight(World world, BlockPos blockPos, Set<Block> blockList)
	{
		int top = world.getChunkFromBlockCoords(blockPos).getTopFilledSegment() + 15;
		int x = blockPos.getX();
		int z = blockPos.getZ();
		for (int y = top; y > 0; --y)
		{
			BlockPos temp = new BlockPos(x, y, z);
			if (blockList.contains(world.getBlockState(temp).getBlock()))
				return temp;
		}
		return new BlockPos(x, 0, z);
	}

	private static class MenhirStructure
	{
		List<List<BlockPos>> structures = new ArrayList<>();
		List<StructureType> types = new ArrayList<>();
		List<Double> chances = new ArrayList<>();

		List<BlockPos> foundation;

		void add(List<BlockPos> structure, StructureType type, double chance)
		{
			structures.add(structure);
			types.add(type);
			chances.add(chance);
		}

		List<BlockPos> get(Random random, StructureType type)
		{
			List<BlockPos> posList = new ArrayList<>();

			for (int i = 0; i < types.size(); ++i)
			{
				if (types.get(i) == type)
				{
					for (BlockPos pos : structures.get(i))
					{
						double chance = chances.get(i);
						if (random.nextDouble() < chance)
							posList.add(pos);
					}
				}
			}

			return posList;
		}

		List<BlockPos> getFoundation()
		{
			if (foundation == null)
			{
				foundation = new ArrayList<>();
				for (int i = 0; i < types.size(); ++i)
					if (types.get(i) == StructureType.GESTALT)
						foundation.addAll(structures.get(i).stream().filter(pos -> pos.getY() == 1).collect(Collectors.toList()));
			}
			return foundation;
		}
	}

	private enum StructureType
	{
		RUBBLE, AIR, GESTALT
	}
}
