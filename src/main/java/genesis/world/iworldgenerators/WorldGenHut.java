package genesis.world.iworldgenerators;

import java.util.Random;

import genesis.block.tileentity.BlockRack;
import genesis.block.tileentity.TileEntityRack;
import genesis.block.tileentity.TileEntityStorageBox;
import genesis.combo.ToolItems;
import genesis.combo.variant.EnumClothing;
import genesis.combo.variant.EnumToolMaterial;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import genesis.common.GenesisItems;
import genesis.common.GenesisLoot;
import genesis.world.biome.BiomeGenRainforest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHut implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.isRemote)
			return;
		
		if (world.rand.nextInt(GenesisConfig.hutChance) != 0)
			return;
		
		if (!GenesisDimensions.isGenesis(world))
			return;
		
		BlockPos start = new BlockPos(chunkX * 16 + 4, 0, chunkZ * 16 + 4);
		
		if (!(world.getBiomeGenForCoords(start) instanceof BiomeGenRainforest))
			return;
		
		start = WorldGenHelper.findSurface(world, start);
		
		for (BlockPos pos : BlockPos.getAllInBox(start.add(-3, 0, -3), start.add(9, 0, 11)))
		{
			if (!(world.getBiomeGenForCoords(start) instanceof BiomeGenRainforest))
				return;
		}
		
		for (BlockPos pos : BlockPos.getAllInBox(start.add(-3, -3, -3), start.add(9, 30, 11)))
		{
			WorldGenHelper.deleteTree(world, pos);
		}
		
		//Starting generation
		
		Genesis.logger.debug("Starting generation of the hut at " + start.toString());
		
		IBlockState[][][] matrix = WGHDB.matrix;
		
		for (int y = 0; y < matrix.length; y++)
		{
			for (int x = 0; x < matrix[y].length; x++)
			{
				for (int z = 0; z < matrix[y][x].length; z++)
				{
					IBlockState state = matrix[y][x][z];
					
					BlockPos pos = start.add(x, y, z);
					
					if (state == null)
					{
						world.setBlockToAir(pos);
					}
					else
					{
						world.setBlockState(pos, state);
					}
				}
			}
		}
		
		BlockPos legsPos[] = { start.add(1, 0, 1), start.add(1, 0, 6), start.add(4, 0, 1), start.add(4, 0, 6) };
		
		for (BlockPos legPos : legsPos)
		{
			while (!WorldGenHelper.isGround(world, legPos.down()))
			{
				legPos = legPos.down();
				world.setBlockState(legPos, WGHDB.wattle);
			}
		}
		
		BlockPos basePos = start.add(4, 0, 3);
		
		while (!WorldGenHelper.isGround(world, basePos.down()))
		{
			basePos = basePos.down();
			world.setBlockState(basePos, WGHDB.baseY);
		}
		
		BlockPos ladderPos = start.add(5, 2, 3);
		
		while (!WorldGenHelper.isGround(world, ladderPos.down()))
		{
			ladderPos = ladderPos.down();
			world.setBlockState(ladderPos, GenesisBlocks.rope_ladder.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.EAST));
		}
		
		TileEntityRack rack1 = BlockRack.getTileEntity(world, start.add(2, 4, 5));
		TileEntityRack rack2 = BlockRack.getTileEntity(world, start.add(3, 4, 5));
		
		if (rack1 != null && rack2 != null)
		{
			rack1.setStackInSide(EnumFacing.SOUTH, GenesisItems.clothing.getHelmet(EnumClothing.CHITIN));
			rack2.setStackInSide(EnumFacing.SOUTH, GenesisItems.tools.getBadStack(ToolItems.KNIFE, EnumToolMaterial.QUARTZ));
		}
		
		BlockPos boxPos = start.add(2, 2, 2);
		
		TileEntity te = world.getTileEntity(boxPos);
		
		if (te instanceof TileEntityStorageBox)
		{
			((TileEntityStorageBox) te).setLoot(GenesisLoot.CHESTS_HUT, System.currentTimeMillis());
		}
		else
		{
			Genesis.logger.warn("TileEntityStorageBox has not been found at "  + boxPos.toString() + " during the hut generation!!! The loot won't be spawned!!!");
		}
		
		Genesis.logger.debug("Hut has been generated at " + start.toString());
	}
}
