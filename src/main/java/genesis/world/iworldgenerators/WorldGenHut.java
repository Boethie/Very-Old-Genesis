package genesis.world.iworldgenerators;

import java.util.Random;

import genesis.block.tileentity.BlockStorageBox;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
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

	
	static IBlockState nullRow[] = {null , null , null, null, null, null, null, null};
	
	//Materials
			static IBlockState base = GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.LEPIDODENDRON);
			
			static IBlockState baseZ = base.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z);
			
			static IBlockState baseY = base.withProperty(BlockLog.LOG_AXIS, EnumAxis.Y);
			
			static IBlockState wattle = GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.WATTLE_FENCE, EnumTree.LEPIDODENDRON);
			
			static IBlockState roof = GenesisBlocks.calamites_roof.getDefaultState();
			
			//0-West Bottom, 1 - East Top, 2 - EastBottom, 3 - WestTop
			static IBlockState[] rf = new IBlockState[]{roof.withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT), roof.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, EnumHalf.TOP).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT), roof.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT), roof.withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, EnumHalf.TOP).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT)};
			
			static IBlockState bundle = GenesisBlocks.calamites_bundle.getDefaultState();
	
	//The main building matrix
	
					 //y x z
	static IBlockState matrix[][][] = new IBlockState[][][]{
	{
		nullRow,
		{null, wattle, null, null, null, null, wattle, null},
		nullRow,
		nullRow,
		{null, wattle, null, baseY, null, null, wattle, null},
		nullRow
	},
	{
		nullRow,
		{null, baseZ, baseZ, baseZ, baseZ, baseZ, baseZ, null},
		{null, base, baseZ, baseZ, baseZ, baseZ, base, null},
		{null, base, baseZ, baseZ, baseZ, baseZ, base, null},
		{null, baseZ, baseZ, baseZ, baseZ, baseZ, baseZ, null},
		nullRow
	},
	{
		nullRow,
		{null, baseY, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), baseY, null},
		{null, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), GenesisBlocks.storage_box.getDefaultState().withProperty(BlockStorageBox.FACING, EnumFacing.EAST),null, null, null, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null},
		{null, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null,null,null, GenesisBlocks.workbench.getDefaultState(), bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null},
		{null, baseY, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null, baseY, baseY, baseY, null},
		nullRow
	},
	{
		nullRow,
		{null, baseY, wattle, wattle, wattle, wattle, baseY, null},
		{null, wattle, null, null, null, null, wattle, null},
		{null, wattle, null, null, null, null, wattle, null},
		{null, baseY, baseY, null, baseY, wattle, baseY, null},
		nullRow
	},
	{
		{rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2]},
		{rf[3], baseZ, baseZ, baseZ, baseZ, baseZ, baseZ, rf[3]},
		{null, base, null,null,null, /*TODO Add a rack*/null, base, null},
		{null, base, null,null,null, /*TODO Add a rack*/null, base, null},
		{rf[1], baseZ, baseZ, baseZ, baseZ, baseZ, baseZ, rf[1]},
		{rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0]}
	},
	{
		nullRow,
		{rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2]},
		{rf[3],wattle, null,null,null,null, wattle, rf[3]},
		{rf[1],wattle, null,null,null,null, wattle, rf[1]},
		{rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0]},
		nullRow
	},
	{
		nullRow,
		nullRow,
		{rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2]},
		{rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0]},
		nullRow,
		nullRow
	}

	};

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (!GenesisDimensions.isGenesis(world))return;
		
		if(random.nextInt(25) != 0)return;
		
		BlockPos start = new BlockPos(chunkX * 16 + 4, 0, chunkZ * 16 + 4);
		
		if (!(world.getBiomeGenForCoords(start) instanceof BiomeGenRainforest))
			return;
		
		start = WorldGenHelper.findSurface(world, start);
		
		AxisAlignedBB aabb = new AxisAlignedBB(start, start.add(5, 7, 7));
		
		//Clear the place
		for(int x = (int) aabb.minX; x<= (int)aabb.maxX; x++)
			for(int y = (int) aabb.minY; y<=(int)aabb.maxY; y++)
				for(int z = (int) aabb.minZ; y<=(int)aabb.maxZ; z++)
				{
					world.setBlockToAir(new BlockPos(x,y,z));
				}
		
		
		
		//Starting generation
		
		//System.out.println("Starting generation of the hut at "+start.toString());
		
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
		
		//System.out.println("Hut has been generated at "+start.toString());
	}

}
