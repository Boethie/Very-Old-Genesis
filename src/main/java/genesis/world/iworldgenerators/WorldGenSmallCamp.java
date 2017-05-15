package genesis.world.iworldgenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.block.BlockGenesisLogs;
import genesis.block.tileentity.TileEntityStorageBox;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import genesis.common.GenesisLoot;
import genesis.util.WorldUtils;
import genesis.world.biome.BiomeAuxForest;
import genesis.world.biome.BiomeMetaForest;
import genesis.world.biome.BiomeWoodlands;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenSmallCamp implements IWorldGenerator
{
	public static final IBlockState[][] BLOCKS;

	static
	{
		IBlockState campfire = GenesisBlocks.CAMPFIRE.getDefaultState();

		IBlockState box = GenesisBlocks.STORAGE_BOX.getDefaultState();

		IBlockState work = GenesisBlocks.WORKBENCH.getDefaultState();

		IBlockState logX = GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.LOG, EnumTree.ARAUCARIOXYLON).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);

		IBlockState logZ = GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.LOG, EnumTree.ARAUCARIOXYLON).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);

		IBlockState[] nullRow = {null, null, null, null, null, null, null, null};
											//x,z
		BLOCKS = new IBlockState[][]
				{
					{null, null, null, logZ, logZ, logZ, null, null},
					{null, null, null, null, null, null, null, box},
					{null, logX, null, null, null, null, null, null},
					{null, logX, null, null, campfire, null, null, null},
					{null, logX, null, null, null, null, null, null},
					nullRow,
					nullRow,
					{work, null, null, null, null, null, null, null}
				};
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.isRemote)
			return;

		if (!GenesisDimensions.isGenesis(world))
			return;

		BlockPos start = new BlockPos(chunkX * 16 + 4, 0, chunkZ * 16 + 4);

		Biome biome = world.getBiome(start);

		if (!(biome instanceof BiomeMetaForest || biome instanceof BiomeAuxForest || biome instanceof BiomeWoodlands))
			return;

		if (world.rand.nextInt(GenesisConfig.smallCampChance) != 0)
			return;

		start = WorldGenHelper.findSurface(world, start);

		if (WorldUtils.waterInRange(world, start, 16, 16))
			return;

		Genesis.logger.debug("Starting generation of the small camp at " + start.toString());

		for (BlockPos bp : BlockPos.getAllInBox(start, start.add(8, 0, 8)))
		{
			Biome bBiome = world.getBiome(bp);

			if (BiomeDictionary.isBiomeOfType(bBiome, Type.HILLS) || BiomeDictionary.isBiomeOfType(bBiome, Type.MOUNTAIN))
				return;
		}

		for (BlockPos bp : BlockPos.getAllInBox(start, start.add(8, 9, 8)))
			WorldGenHelper.deleteTree(world, bp);

		BlockPos boxPos = null;
		//BlockPos campPos = null;

		List<BlockPos> logsPos = new ArrayList<>();

		for (int x = 0; x < BLOCKS.length; x++)
		{
			for (int z = 0; z < BLOCKS[x].length; z++)
			{
				IBlockState state = BLOCKS[x][z];

				BlockPos pos = WorldGenHelper.findSurface(world, start.add(x, 0, z));

				if (state != null)
				{
					if (state.getBlock() == GenesisBlocks.STORAGE_BOX)
						boxPos = pos;

					//if (state.getBlock() == GenesisBlocks.CAMPFIRE)
						//campPos = pos;

					if (state.getBlock() instanceof BlockGenesisLogs)
						logsPos.add(pos);

					world.setBlockState(pos, state);
				}
			}
		}

		if (boxPos != null)
		{
			TileEntity te = world.getTileEntity(boxPos);

			if (te instanceof TileEntityStorageBox)
			{
				ResourceLocation loot = null;

				if (biome instanceof BiomeMetaForest)
					loot = GenesisLoot.STORAGE_BOX_CAMP_META_FOREST;

				if (biome instanceof BiomeAuxForest)
					loot = GenesisLoot.STORAGE_BOX_CAMP_AUX_FOREST;

				if (biome instanceof BiomeWoodlands)
					loot = GenesisLoot.STORAGE_BOX_CAMP_WOODLANDS;

				((TileEntityStorageBox) te).setLootTable(loot, random.nextLong());
			}
			else
			{
				Genesis.logger.warn("TileEntityStorageBox has not been found at "  + boxPos.toString() + " during the camp generation!!! The loot won't be spawned!!!");
			}
		}

		EnumTree tree = null;

		if (biome instanceof BiomeAuxForest)
		{
			tree = EnumTree.ARAUCARIOXYLON;
		}
		else if (biome instanceof BiomeMetaForest)
		{
			tree = EnumTree.METASEQUOIA;
		}
		else if (biome instanceof BiomeWoodlands)
		{
			tree = EnumTree.DRYOPHYLLUM;
		}

		int highestYX = 0;
		int highestYZ = 0;

		for (BlockPos pos : logsPos)
		{
			if (world.getBlockState(pos).getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.X)
			{
				if (pos.getY() > highestYX)
				{
					highestYX = pos.getY();
				}
			}

			if (world.getBlockState(pos).getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.Z)
			{
				if (pos.getY() > highestYZ)
				{
					highestYZ = pos.getY();
				}
			}
		}

		if (tree != null)
			for (BlockPos pos : logsPos)
			{
				IBlockState state = GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.LOG, tree).withProperty(BlockLog.LOG_AXIS, world.getBlockState(pos).getValue(BlockLog.LOG_AXIS));

				world.setBlockToAir(pos);

				pos = new BlockPos(pos.getX(), state.getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.X ? highestYX : highestYZ, pos.getZ());

				world.setBlockState(pos, state);
			}
	}
}
