package genesis.world.overworld;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenTaiga;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.List;
import java.util.Random;

import static genesis.world.gen.OverworldGeneration.menhirHutChest;

/**
 * Created by Vorquel on 10/27/15.
 */
public class WorldGenMenhirActivators implements IWorldGenerator
{
	private List<MenhirStructure> structures = Lists.newArrayList();
	private List<Block> topBlocks = Lists.newArrayList(Blocks.dirt, Blocks.grass, Blocks.stone);
	private List<Block> bottomBlocks = Lists.newArrayList(Blocks.stone);
	
	{
		MenhirStructure hut = new MenhirStructure();
		
		List<BlockPos> rubble = Lists.newArrayList();
		for(int x = -7; x <= 7; ++x)
			for(int z = -7; z <= 7; ++ z)
				if(x * x + z * z < 64)
					rubble.add(new BlockPos(x, 0, z));
		hut.add(rubble, StructureType.RUBBLE, 1d/12d);
		
		List<BlockPos> air = Lists.newArrayList();
		List<BlockPos> gestalt = Lists.newArrayList();
		for(int y = -1; y <= 3; ++y)
			for(int x = -3; x <= 3; ++x)
				for(int z = -3; z <= 3; ++z)
				{
					int d2 = x * x + y * y + z * z;
					if(d2 < 8 || x == 3 && y <= 0 && z == 0)
						air.add(new BlockPos(x, y + 2, z));
					else if(d2 < 12)
						gestalt.add(new BlockPos(x, y + 2, z));
				}
		hut.add(air, StructureType.AIR, 1d);
		hut.add(gestalt, StructureType.GESTALT, 11d/12d);
		
		structures.add(hut);
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
		MenhirStructure structure = structures.get(random.nextInt(structures.size()));
		BlockPos center = getValidPos(structure, random, world, chunkPos);
		if(center == null)
			return;
		
		for(BlockPos offset : structure.get(random, StructureType.RUBBLE))
		{
			BlockPos pos = getRealHeight(world, center.add(offset), topBlocks).up(offset.getY());
			if (random.nextInt(4) == 0)
				world.setBlockState(pos, Blocks.mossy_cobblestone.getDefaultState());
			else
				world.setBlockState(pos, Blocks.cobblestone.getDefaultState());
		}
		
		for(BlockPos offset : structure.get(random, StructureType.GESTALT))
		{
			BlockPos pos = center.add(offset);
			if (random.nextInt(4) == 0)
				world.setBlockState(pos, Blocks.mossy_cobblestone.getDefaultState());
			else
				world.setBlockState(pos, Blocks.cobblestone.getDefaultState());
		}
		
		for(BlockPos offset : structure.get(random, StructureType.AIR))
			world.setBlockToAir(center.add(offset));
		
		BlockPos chestPos = center.add(0, -1, 0);
		world.setBlockState(chestPos, Blocks.chest.getDefaultState());
		IInventory inventory = (IInventory) world.getTileEntity(chestPos);
		
		int count = menhirHutChest.getCount(random);
		List<ItemStack> loot = Lists.newArrayList();
		for(int i = 0; i < count; ++i)
		{
			ItemStack nextStack;
			do
			{
				nextStack = menhirHutChest.getOneItem(random);
			}
			while(loot.contains(nextStack));
			loot.add(nextStack);
		}
		
		for(ItemStack stack : loot)
		{
			int nextPos;
			do
			{
				nextPos = random.nextInt(inventory.getSizeInventory());
			}
			while(inventory.getStackInSlot(nextPos) != null);
			inventory.setInventorySlotContents(nextPos, stack);
		}
	}
	
	private BlockPos getValidPos(MenhirStructure structure, Random random, World world, BlockPos chunkPos)
	{
		chunkPos = new BlockPos(chunkPos.getX(), 0, chunkPos.getZ());
		BlockPos candidate;
		for(int i=0; i<20; ++i)
		{
			candidate = chunkPos.add(random.nextInt(16), 0, random.nextInt(16));
			int min = 255;
			int max = 0;
			for(BlockPos offset : structure.getFoundation())
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
		return null;
	}
	
	private BlockPos getRealHeight(World world, BlockPos blockPos, List<Block> blockList)
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
	
	private static class MenhirStructure
	{
		List<List<BlockPos>> structures = Lists.newArrayList();
		List<StructureType> types = Lists.newArrayList();
		List<Double> chances = Lists.newArrayList();
		
		List<BlockPos> foundation;
		
		void add(List<BlockPos> structure, StructureType type, double chance)
		{
			structures.add(structure);
			types.add(type);
			chances.add(chance);
		}
		
		List<BlockPos> get(Random random, StructureType type)
		{
			List<BlockPos> posList = Lists.newArrayList();
			for(int i = 0; i < types.size(); ++i)
				if(types.get(i) == type)
					for(BlockPos pos : structures.get(i))
					{
						double chance = chances.get(i);
						if(random.nextDouble() < chance)
							posList.add(pos);
					}
			return posList;
		}
		
		List<BlockPos> getFoundation()
		{
			if(foundation == null)
			{
				foundation = Lists.newArrayList();
				for(int i = 0; i < types.size(); ++i)
					if(types.get(i) == StructureType.GESTALT)
						for(BlockPos pos : structures.get(i))
							if(pos.getY() == 1)
								foundation.add(pos);
			}
			return foundation;
		}
	}
	
	private enum StructureType
	{
		RUBBLE, AIR, GESTALT
	}
}
