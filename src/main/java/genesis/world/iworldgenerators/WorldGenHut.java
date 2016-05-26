package genesis.world.iworldgenerators;

import java.util.Random;

import genesis.block.tileentity.BlockStorageBox;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import genesis.world.biome.BiomeGenRainforest;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenTaiga;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHut implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if(world.rand.nextInt(Math.round(1/GenesisConfig.hutChance)) != 0)return;
		
		if (!GenesisDimensions.isGenesis(world))return;
		
		BlockPos start = new BlockPos(chunkX * 16 + 4, 0, chunkZ * 16 + 4);
		
		if (!(world.getBiomeGenForCoords(start) instanceof BiomeGenRainforest))
			return;
		
		start = WorldGenHelper.findSurface(world, start);
		/*
		AxisAlignedBB aabb = new AxisAlignedBB(start, start.add(5, 7, 7));
		
		//Clear the place
		for(int x = (int) aabb.minX; x<= (int)aabb.maxX; x++)
			for(int y = (int) aabb.minY; y<=(int)aabb.maxY; y++)
				for(int z = (int) aabb.minZ; y<=(int)aabb.maxZ; z++)
				{
					world.setBlockToAir(new BlockPos(x,y,z));
				}
		*/
		
		
		//Starting generation
		
		System.out.println("Starting generation of the hut at "+start.toString());
		
		IBlockState[][][] matrix = WGHDB.matrix;
		
		for(int y = 0; y < matrix.length; y++)
			for(int x = 0; x < matrix[y].length; x++)
				for(int z = 0; z < matrix[y][x].length; z++)
				{
					IBlockState bs = matrix[y][x][z];
					
					BlockPos pos = start.add(x, y, z);
					
					if(bs == null)
					{
						world.setBlockToAir(pos);
					}
					else
					{
						world.setBlockState(pos, bs);
					}
				}
		
		System.out.println("Hut has been generated at "+start.toString());
	}

}
