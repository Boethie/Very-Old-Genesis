package genesis.world.iworldgenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.common.GenesisBiomes;
import genesis.world.iworldgenerators.WorldGenStructureHelper.StructureType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldGenHutNBT extends WorldGenStructureBase
{
	protected int rarity = 10;
	
	@Override
	public List<Biome> getAllowedBiomes()
	{
		List<Biome> biomes = new ArrayList<Biome>();
		
		biomes.add(GenesisBiomes.rainforest);
		biomes.add(GenesisBiomes.rainforestHills);
		biomes.add(GenesisBiomes.rainforestIslands);
		biomes.add(GenesisBiomes.rainforestM);
		
		return biomes;
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
		
		StructureType hut = StructureType.HUT1;
		
		if (!world.isAirBlock(curPos))
			curPos = curPos.up();
		/*
		generated = this.checkSurface(
				world, 
				curPos.add(MathHelper.abs_max(hut.getBounds().xCoord, hut.getBounds().zCoord), 0, MathHelper.abs_max(hut.getBounds().xCoord, hut.getBounds().zCoord)), 
				(int)(MathHelper.abs_max(hut.getBounds().xCoord, hut.getBounds().zCoord) * 0.45D), 
				(int)(hut.getBounds().yCoord * 0.7D));
		*/
		if (generated)
		{
			EnumFacing offset = hut.getOffse();
			
			WorldGenStructureHelper.spawnStructure(
					world, 
					((offset == null)? curPos : curPos.offset(offset)), 
					hut,
					Mirror.NONE, //.values()[rand.nextInt(Mirror.values().length)]
					Rotation.NONE); //.values()[rand.nextInt(Rotation.values().length)]
		}
		
		return generated;
	}
}
