package genesis.world.overworld;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenTaiga;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.Random;

import static genesis.world.gen.OverworldGeneration.menhirHutChest;

/**
 * Created by Vorquel on 10/27/15.
 */
public class WorldGenMenhirHut implements IWorldGenerator
{
	private static ArrayList<Block> topBlocks = Lists.newArrayList(Blocks.dirt, Blocks.grass, Blocks.stone);
	private static ArrayList<Block> bottomBlocks = Lists.newArrayList(Blocks.stone);
	private ArrayList<BlockPos> foundationOffsets = Lists.newArrayList(
			new BlockPos(-3, 0, 0), new BlockPos(3, 0, 0), new BlockPos(0, 0, -3), new BlockPos(0, 0, 3));
	private ArrayList<BlockPos> hutOffsets = new ArrayList<>();
	{
		for(int i=-1; i<=1; ++i)
		{
			for(int j=-1; j<=1; ++j)
			{
				hutOffsets.add(new BlockPos( i, 5,  j));
				hutOffsets.add(new BlockPos( i, j+2, -3));
				hutOffsets.add(new BlockPos( i, j+2,  3));
				hutOffsets.add(new BlockPos(-3, i+2,  j));
				hutOffsets.add(new BlockPos( 3, i+2,  j));
			}
			for(int j : new int[]{-2,2})
			{
				hutOffsets.add(new BlockPos(i, 4, j));
				hutOffsets.add(new BlockPos(j, 4, i));
			}
		}
		for(int i = 1; i <= 3; ++i)
			for(int j : new int[]{-2,2})
				for(int k : new int[]{-2,2})
					hutOffsets.add(new BlockPos(j, i, k));
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		BlockPos chunkPos = new BlockPos(chunkX << 4, 0, chunkZ << 4);
		
		int menhirHutChance = 500;
		if(random.nextInt(menhirHutChance) == 0 && world.getBiomeGenForCoords(chunkPos.add(8, 0, 8)) instanceof BiomeGenTaiga)
			generateHut(random, world, chunkPos);
	}
	
	public void generateHut(Random random, World world, BlockPos chunkPos)
	{
		BlockPos center = getValidPos(random, world, chunkPos);
		int rubbleCount = 20 + random.nextInt(11);
		for(int i=0; i<rubbleCount; ++i)
		{
			BlockPos pos = getRubblePos(random, world, center);
			if (random.nextInt(4) == 0)
				world.setBlockState(pos, Blocks.mossy_cobblestone.getDefaultState());
			else
				world.setBlockState(pos, Blocks.cobblestone.getDefaultState());
		}
		for(BlockPos offset : hutOffsets)
		{
			BlockPos pos = center.add(offset);
			Block block = world.getBlockState(pos).getBlock();
			if(!(block == Blocks.log || block == Blocks.leaves))
				if(random.nextInt(12) < 11)
				{
					if (random.nextInt(4) == 0)
						world.setBlockState(pos, Blocks.mossy_cobblestone.getDefaultState());
					else
						world.setBlockState(pos, Blocks.cobblestone.getDefaultState());
				}
		}
		BlockPos chestPos = center.add(0, -1, 0);
		world.setBlockState(chestPos, Blocks.chest.getDefaultState());
		IInventory inventory = (IInventory) world.getTileEntity(chestPos);
		WeightedRandomChestContent.generateChestContents(random, menhirHutChest.getItems(random), inventory, menhirHutChest.getCount(random));
		int offset = 3 * (random.nextBoolean() ? 1 : -1);
		if(random.nextBoolean())
		{
			world.setBlockToAir(center.add(offset, 1, 0));
			world.setBlockToAir(center.add(offset, 2, 0));
		}
		else
		{
			world.setBlockToAir(center.add(0, 1, offset));
			world.setBlockToAir(center.add(0, 2, offset));
		}
	}
	
	private BlockPos getValidPos(Random random, World world, BlockPos chunkPos)
	{
		chunkPos = new BlockPos(chunkPos.getX(), 0, chunkPos.getZ());
		BlockPos candidate = chunkPos;
		for(int i=0; i<20; ++i)
		{
			candidate = chunkPos.add(random.nextInt(16), 0, random.nextInt(16));
			int min = 255;
			int max = 0;
			for(BlockPos offset : foundationOffsets)
			{
				int lo = getRealHeight(world, candidate.add(offset), topBlocks).getY();
				min = lo < min ? lo : min;
				int hi = getRealHeight(world, candidate.add(offset), bottomBlocks).getY();
				max = hi > max ? hi : max;
			}
			candidate = candidate.add(0, min, 0);
			if(max <= min)
				return candidate;
		}
		return candidate;
	}
	
	private BlockPos getRubblePos(Random random, World world, BlockPos center)
	{
		float f = random.nextFloat();
		float rho = 7.99F * (1 - f * f);
		float theta = random.nextFloat();
		int x = (int) (rho * Math.cos(2 * Math.PI * theta));
		int z = (int) (rho * Math.sin(2 * Math.PI * theta));
		BlockPos bp = center.add(x, 0, z);
		return getRealHeight(world, bp, topBlocks);
	}
	
	private BlockPos getRealHeight(World world, BlockPos blockPos, ArrayList<Block> blockList)
	{
		int top = world.getChunkFromBlockCoords(blockPos).getTopFilledSegment() + 15;
		int x = blockPos.getX();
		int z = blockPos.getZ();
		for(int y = top; y > 0; --y)
		{
			BlockPos temp = new BlockPos(x, y, z);
			if(blockList.contains(world.getBlockState(temp).getBlock()))
				return temp;
		}
		return new BlockPos(x, 0, z);
	}
}
