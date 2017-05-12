package genesis.world.iworldgenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.block.tileentity.TileEntityStorageBox;
import genesis.common.GenesisBiomes;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisLoot;
import genesis.world.iworldgenerators.WorldGenStructureHelper.StructureType;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldGenMetaHouse extends WorldGenStructureBase
{
	@Override
	public int getRarity()
	{
		return 99;
	}
	
	@Override
	public List<Biome> getAllowedBiomes()
	{
		List<Biome> biomes = new ArrayList<Biome>();
		
		//biomes.add(GenesisBiomes.metaForest);
		//biomes.add(GenesisBiomes.metaForestM);
		
		return biomes;
	}
	
	@Override
	public List<Block> getSurfaceBlocks()
	{
		List<Block> blocks = new ArrayList<Block>();
		
		blocks.add(Blocks.DIRT);
		blocks.add(GenesisBlocks.MOSS);
		
		return blocks;
	}
	
	@Override
	public GenerationType getGenerationType()
	{
		return GenerationType.FINDGROUND;
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		boolean generated = false;
		BlockPos curPos = pos;
		
		StructureType house = StructureType.META_HOUSE;
		
		EnumFacing offset = house.getOffse();
		
		Vec3d secOffset = house.getSecondOffset();
		
		if (secOffset != null)
			curPos = curPos.add(secOffset.xCoord, secOffset.yCoord, secOffset.zCoord);
		
		generated = this.checkSurface(
				world, 
				curPos, 
				(int)(MathHelper.abs_max(house.getBounds().xCoord, house.getBounds().zCoord) * 0.85D), 
				(int)(house.getBounds().yCoord * 0.7D));
		
		generated = WorldGenStructureHelper.spawnStructure(
				world, 
				((offset == null)? curPos : curPos.offset(offset)), 
				house,
				house.getMirror()[rand.nextInt(house.getMirror().length)], 
				house.getRotation()[rand.nextInt(house.getRotation().length)]);
		
		if (world.isAirBlock(curPos.up()))
			curPos = curPos.down();
		
		if (generated)
		{
			BlockPos storagePos = this.findBlockInArea(world, curPos, 8, 5, GenesisBlocks.STORAGE_BOX.getDefaultState(), true);
			
			if (storagePos != null)
			{
				TileEntity te = world.getTileEntity(storagePos);
				
				if (te instanceof TileEntityStorageBox)
				{
					TileEntityStorageBox storageBox = (TileEntityStorageBox)te;
					storageBox.setLootTable(GenesisLoot.STORAGE_BOX_META_HOUSE, rand.nextLong());
				}
			}
		}
		
		return generated;
	}
}
