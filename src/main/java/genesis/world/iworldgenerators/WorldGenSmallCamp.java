package genesis.world.iworldgenerators;

import java.util.Random;

import genesis.block.tileentity.TileEntityStorageBox;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import genesis.common.GenesisLoot;
import genesis.world.biome.BiomeGenAuxForest;
import genesis.world.biome.BiomeGenMetaForest;
import genesis.world.biome.BiomeGenWoodlands;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenSmallCamp implements IWorldGenerator
{
	public static final IBlockState[][] BLOCKS;
	
	static
	{
		IBlockState campfire = GenesisBlocks.campfire.getDefaultState();
		
		IBlockState box = GenesisBlocks.storage_box.getDefaultState();
		
		IBlockState work = GenesisBlocks.workbench.getDefaultState();
		
		IBlockState logX = GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.DEAD_LOG, EnumTree.ARAUCARIOXYLON).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);
		
		IBlockState logZ = GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.DEAD_LOG, EnumTree.ARAUCARIOXYLON).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);
		
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
		
		BiomeGenBase biome = world.getBiomeGenForCoords(start);
		
		if (!(biome instanceof BiomeGenMetaForest || biome instanceof BiomeGenAuxForest || biome instanceof BiomeGenWoodlands))
			return;
		
		if (world.rand.nextInt(GenesisConfig.smallCampChance) != 0)
			return;
		
		start = WorldGenHelper.findSurface(world, start);
		
		Genesis.logger.debug("Starting generation of the small camp at " + start.toString());
		
		BlockPos boxPos = null;
		
		for (int x = 0; x < BLOCKS.length; x++)
		{
			for (int z = 0; z < BLOCKS[x].length; z++)
			{
				IBlockState state = BLOCKS[x][z];
				
				BlockPos pos = WorldGenHelper.findSurface(world, start.add(x, 0, z));
				
				if (state != null)
				{
					if (state.getBlock() == GenesisBlocks.storage_box)
						boxPos = pos;
					
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
				
				if (biome instanceof BiomeGenMetaForest)
					loot = GenesisLoot.CHESTS_CAMP_META_FOREST;
				
				if (biome instanceof BiomeGenAuxForest)
					loot = GenesisLoot.CHESTS_CAMP_AUX_FOREST;
				
				if (biome instanceof BiomeGenWoodlands)
					loot = GenesisLoot.CHESTS_CAMP_WOODLANDS;
				
				((TileEntityStorageBox) te).setLoot(loot, System.currentTimeMillis());
			}
			else
			{
				Genesis.logger.warn("TileEntityStorageBox has not been found at "  + boxPos.toString() + " during the hut generation!!! The loot won't be spawned!!!");
			}
		}
	}

}
