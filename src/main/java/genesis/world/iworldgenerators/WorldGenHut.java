package genesis.world.iworldgenerators;

import java.util.Random;

import genesis.block.tileentity.BlockRack;
import genesis.block.tileentity.BlockStorageBox;
import genesis.block.tileentity.TileEntityRack;
import genesis.block.tileentity.TileEntityStorageBox;
import genesis.combo.ClothingItems;
import genesis.combo.ToolItems;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumClothing;
import genesis.combo.variant.EnumToolMaterial;
import genesis.combo.variant.EnumTree;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import genesis.common.GenesisItems;
import genesis.common.GenesisLoot;
import genesis.world.biome.BiomeGenRainforest;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenTaiga;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHut implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if(world.isRemote)return;
		
		if(world.rand.nextInt(Math.round(1/GenesisConfig.hutChance)) != 0)return;
		
		if (!GenesisDimensions.isGenesis(world))return;
		
		BlockPos start = new BlockPos(chunkX * 16 + 4, 0, chunkZ * 16 + 4);
		
		if (!(world.getBiomeGenForCoords(start) instanceof BiomeGenRainforest))
			return;
		
		start = WorldGenHelper.findSurface(world, start);

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
		
		BlockPos legsPos[] = new BlockPos[]{start.add(1, 0, 1), start.add(1, 0, 6), start.add(4, 0, 1), start.add(4, 0, 6)};
		
		for(BlockPos bp : legsPos)
		{
			while(!WorldGenHelper.isGround(world, bp.down()))
			{
				bp = bp.down();
				world.setBlockState(bp, WGHDB.wattle);
			}
		}
		
		BlockPos l = start.add(4, 0, 3);
		
		while(!WorldGenHelper.isGround(world, l.down()))
		{
			l = l.down();
			world.setBlockState(l, WGHDB.baseY);
		}
		
		BlockPos lad = start.add(5, 1, 3);
		
		while(!WorldGenHelper.isGround(world, lad.down()))
		{
			lad = lad.down();
			world.setBlockState(lad, GenesisBlocks.rope_ladder.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.EAST));
		}
		
		TileEntityRack rack1 = BlockRack.getTileEntity(world, start.add(2, 4, 5));
		TileEntityRack rack2 = BlockRack.getTileEntity(world, start.add(3, 4, 5));
		
		if(rack1 != null && rack2 != null)
		{
			rack1.setStackInSide(EnumFacing.SOUTH, GenesisItems.clothing.getHelmet(EnumClothing.CHITIN));
			rack2.setStackInSide(EnumFacing.SOUTH, GenesisItems.tools.getBadStack(ToolItems.KNIFE, EnumToolMaterial.QUARTZ));
		}
		
		BlockPos boxPos = start.add(2, 2, 2);
		
		TileEntityStorageBox tesb = (TileEntityStorageBox) world.getTileEntity(boxPos);
		
		if(tesb != null)
		{
			tesb.setLoot(GenesisLoot.HUT, System.currentTimeMillis());
		}
		else
			Genesis.logger.warn("TileEntityStorageBox has not been found at "+boxPos.toString()+" during the hut generation!!! The loot won't be spawned!!!");
		
		System.out.println("Hut has been generated at "+start.toString());
	}
}
